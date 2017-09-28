package com.tokopedia.core.cache.util;

import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.cache.constant.HTTPMethodDef;

import java.io.EOFException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;

/**
 * Created by nathan on 9/26/17.
 */

public class CacheApiUtils {
    private static final long DEFAULT_MAX_CONTENT_LENGTH = 250000L;

    private static final String[] UNUSED_PARAM = {"hash", "device_time", "device_id"};

    private static final String HTTPS = "https://";
    private static final String COM_WITH_SLASH = ".com/";
    private static final String COM1 = ".com";
    private static final String PARAM_SEPARATOR = "-";

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
            return url.getAuthority();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPath(String fullPath) {
        try {
            URL url = new URL(fullPath);
            return url.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getRequestParam(Request request) {
        String requestParam = "";
        try {
            URL url = new URL(request.url().toString());
            if (!TextUtils.isEmpty(url.getQuery())) {
                requestParam += url.getQuery();
            }
            if (HTTPMethodDef.TYPE_POST.equalsIgnoreCase(request.method()) && request.body() != null) {
                String bodyText = getBodyRequest(request);
                if (!TextUtils.isEmpty(requestParam) && !TextUtils.isEmpty(bodyText)) {
                    requestParam += PARAM_SEPARATOR;
                }
                requestParam += bodyText;
            }
            requestParam = getRemovedUnusedRequestParam(requestParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParam.trim();
    }

    private static String getBodyRequest(final Request request) throws Exception {
        final Request copy = request.newBuilder().build();
        final Buffer buffer = new Buffer();
        copy.body().writeTo(buffer);
        return buffer.readUtf8();
    }

    private static String getRemovedUnusedRequestParam(String url) {
        for (String param: UNUSED_PARAM) {
            url = url.replaceAll("[?&]" + param + ".*?(?=&|\\?|$)", "");
        }
        return url;
    }

    public static String readFromBuffer(Buffer buffer, Charset charset) {
        long bufferSize = buffer.size();
        long maxBytes = Math.min(bufferSize, DEFAULT_MAX_CONTENT_LENGTH);
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
            BufferedSource source = response.peekBody(DEFAULT_MAX_CONTENT_LENGTH).source();
            if (source.buffer().size() < DEFAULT_MAX_CONTENT_LENGTH) {
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
