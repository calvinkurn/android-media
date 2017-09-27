package com.tokopedia.core.cache.util;

import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.cache.constant.HTTPMethodDef;

import java.io.EOFException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;

/**
 * Created by nathan on 9/26/17.
 */

public class CacheApiUtils {
    private static final long DAFAULT_MAX_CONTENT_LENGTH = 250000L;

    private static final String HTTPS = "https://";
    private static final String COM_WITH_SLASH = ".com/";
    private static final String COM1 = ".com";

    public static String getFullRequestURL(Request request){
        String s = "";
        if (HTTPMethodDef.TYPE_POST.equalsIgnoreCase(request.method())) {
            RequestBody requestBody = request.body();
            if (requestBody instanceof FormBody) {
                int size = ((FormBody) requestBody).size();
                for (int i = 0; i < size; i++) {
                    String key = ((FormBody) requestBody).encodedName(i);
                    if (key.equals("hash") || key.equals("device_time")) {
                        continue;
                    }

                    String value = ((FormBody) requestBody).encodedValue(i);
                    if (TextUtils.isEmpty(s)) {
                        s += "?";
                    } else {
                        s += "&";
                    }
                    s += key + "=" + value;
                }
            }
        }
        return request.url().toString() + s;
    }

    public static String generateCacheHost(String host) {
        return (host.replace(HTTPS, "").replace(COM_WITH_SLASH, COM1));
    }

    public static String generateCachePath(String path) {
        if (!path.startsWith("/")) {
            return "/" + path;
        } else {
            return path;
        }
    }

    public static String getDomain(String fullPath) {
        try {
            URL url = new URL(fullPath);
            return  url.getAuthority();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPath(String fullPath) {
        try {
            URL url = new URL(fullPath);
            return  url.getPath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readFromBuffer(Buffer buffer, Charset charset) {
        long bufferSize = buffer.size();
        long maxBytes = Math.min(bufferSize, DAFAULT_MAX_CONTENT_LENGTH);
        String body = "";
        try {
            body = buffer.readString(maxBytes, charset);
        } catch (EOFException e) {
            e.printStackTrace();
        }
        return body;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    public static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    public static BufferedSource getNativeSource(Response response) throws IOException {
        if (bodyGzipped(response.headers())) {
            BufferedSource source = response.peekBody(DAFAULT_MAX_CONTENT_LENGTH).source();
            if (source.buffer().size() < DAFAULT_MAX_CONTENT_LENGTH) {
                return getNativeSource(source, true);
            } else {
                CommonUtils.dumper("gzip encoded response was too long");
            }
        }
        return response.body().source();
    }

    private static boolean bodyGzipped(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return "gzip".equalsIgnoreCase(contentEncoding);
    }

    private static BufferedSource getNativeSource(BufferedSource input, boolean isGzipped) {
        if (isGzipped) {
            GzipSource source = new GzipSource(input);
            return Okio.buffer(source);
        } else {
            return input;
        }
    }
}
