package com.tokopedia.core.cache.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.cache.constant.CacheApiConstant;
import com.tokopedia.core.cache.constant.HTTPMethodDef;
import com.tokopedia.core.network.retrofit.response.BaseResponseError;
import com.tokopedia.core.network.retrofit.response.TkpdV4ResponseError;
import com.tokopedia.core.network.retrofit.response.TopAdsResponseError;

import java.io.EOFException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;

/**
 * Created by nathan on 9/26/17.
 */

public class CacheApiUtils {

    private static final int BYTE_COUNT = 2048;
    private static final long DEFAULT_MAX_CONTENT_LENGTH = Long.MAX_VALUE;
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final String[] UNUSED_PARAM = {"hash", "device_time", "device_id"};
    private static final String PARAM_SEPARATOR = "-";
    private static final String SEPARATOR_PATH_URL = "/";
    private static final String PREFIX_UNUSED_REQUEST_PARAM_REGEX = "[?&]";
    private static final String SUFFIX_UNUSED_REQUEST_PARAM_REGEX = ".*?(?=&|\\?|$)";
    private static final int MAX_BUFFER_SIZE = 64;
    private static final int MAX_BYTE_READ_SAMPLE = 16;
    private static final String CONTENT_ENCODING_PARAM = "Content-Encoding";

    public static String generateCachePath(String path) {
        if (!path.startsWith(SEPARATOR_PATH_URL)) {
            return SEPARATOR_PATH_URL + path;
        } else {
            return path;
        }
    }

    public static String getHost(String fullUrl) {
        try {
            URL url = new URL(fullUrl);
            return url.getHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPath(String fullUrl) {
        try {
            URL url = new URL(fullUrl);
            return url.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get request param for get and post. If post has body it will be added at the back
     * eg:
     * get = '?a=1&b=2'
     * post = 'c=3'
     * request param = 'a=1&b=2 - c=3'
     *
     * @param request
     * @return
     */
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

    /**
     * Remove unused param
     * eg device_time, device_id
     *
     * @param url
     * @return
     */
    private static String getRemovedUnusedRequestParam(String url) {
        for (String param : UNUSED_PARAM) {
            url = url.replaceAll(PREFIX_UNUSED_REQUEST_PARAM_REGEX + param + SUFFIX_UNUSED_REQUEST_PARAM_REGEX, "");
        }
        return url;
    }

    /**
     * Check if response is valid to be cached or not
     *
     * @param response
     * @return
     */
    public static boolean isResponseValidToBeCached(Response response) {
        if (response.code() != CacheApiConstant.CODE_OK) {
            return false;
        }
        if (isStatusOkAndResponseError(response, TkpdV4ResponseError.class)) {
            return false;
        }
        if (isStatusOkAndResponseError(response, TopAdsResponseError.class)) {
            return false;
        }
        return true;
    }

    private static boolean isStatusOkAndResponseError(Response response, Class<? extends BaseResponseError> responseErrorClass) {
        try {
            Gson gson = new Gson();
            ResponseBody responseBody = response.peekBody(BYTE_COUNT);
            BaseResponseError responseError = gson.fromJson(responseBody.string(), responseErrorClass);
            return responseError.isResponseErrorValid();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get response body from response object
     *
     * @param response
     * @return
     */
    public static String getResponseBody(Response response) {
        try {
            ResponseBody responseBody = response.body();
            if (HttpHeaders.hasBody(response)) {
                BufferedSource source = CacheApiUtils.getNativeSource(response);
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.buffer();
                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                if (isPlaintext(buffer)) {
                    return readFromBuffer(buffer.clone(), charset);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String readFromBuffer(Buffer buffer, Charset charset) {
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
    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < MAX_BUFFER_SIZE ? buffer.size() : MAX_BUFFER_SIZE;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < MAX_BYTE_READ_SAMPLE; i++) {
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

    private static BufferedSource getNativeSource(Response response) throws IOException {
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
        String contentEncoding = headers.get(CONTENT_ENCODING_PARAM);
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