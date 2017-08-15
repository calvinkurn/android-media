package com.tokopedia.core.cache.interceptor;

import android.net.Uri;
import android.util.Log;

import com.tokopedia.core.cache.UrlEncodedQueryString;
import com.tokopedia.core.cache.data.source.cache.CacheHelper;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist;

import java.io.EOFException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;

/**
 * Created by normansyahputa on 8/9/17.
 */

public class ApiCacheInterceptor implements Interceptor {
    private static final String LOG_TAG = "ChuckInterceptor";
    private static final Charset UTF8 = Charset.forName("UTF-8");
    CacheHelper cacheHelper = new CacheHelper();
    private long maxContentLength = 250000L;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        CacheApiData cacheApiData = new CacheApiData();
        cacheApiData.setMethod(request.method());
        cacheApiData = setUrl(cacheApiData, request.url().toString());
        CacheApiWhitelist whiteList_ = cacheHelper.queryFromRaw(cacheApiData.getHost(), cacheApiData.getPath());
        boolean isInWhiteList = (boolean) (whiteList_ != null);
//        Log.d(LOG_TAG, "is in path !!"+isInWhiteList);

        if (isInWhiteList && whiteList_ != null) {
            CacheApiData tempData = cacheHelper.queryDataFrom(cacheApiData.getHost(), cacheApiData.getPath(), cacheApiData.getRequestParam());

            if (tempData == null) {
                Log.d(LOG_TAG, "null is here !!");
                Response response;
                try {
                    response = chain.proceed(request);
                } catch (Exception e) {
                    throw e;
                }

                cacheApiData.setResponseDate(System.currentTimeMillis() / 1000L);

                putResponseBody(cacheApiData, response);

                cacheApiData.save();

                return response;
            }

            if ((System.currentTimeMillis() / 1000L) - tempData.getResponseDate() > whiteList_.getExpiredTime()) {
                // delete row
                Log.d(LOG_TAG, "expired time !!");
                tempData.delete();


                Response response;
                try {
                    response = chain.proceed(request);
                } catch (Exception e) {
                    throw e;
                }

                cacheApiData.setResponseDate(System.currentTimeMillis() / 1000L);

                putResponseBody(cacheApiData, response);

                cacheApiData.save();

                return response;
            } else {

                Log.d(LOG_TAG, "already in here !!");
                Response.Builder builder = new Response.Builder();
                builder.request(request);
                builder.protocol(Protocol.HTTP_1_1);
                builder.code(200);
                builder.body(ResponseBody.create(MediaType.parse("application/json"), tempData.getResponseBody()));
                return builder.build();
            }
        }else{
            Log.d(LOG_TAG, "just hit another network !!");
            Response response;
            try {
                response = chain.proceed(request);
            } catch (Exception e) {
                throw e;
            }

            cacheApiData.setResponseDate(System.currentTimeMillis() / 1000L);

            putResponseBody(cacheApiData, response);

            cacheApiData.save();

            return response;
        }
    }

    private void putResponseBody(CacheApiData cacheApiData, Response response) throws IOException {
        ResponseBody responseBody = response.body();
        if (HttpHeaders.hasBody(response)) {
            BufferedSource source = getNativeSource(response);
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
//                    update(transaction, transactionUri);
                }
            }
            if (isPlaintext(buffer)) {
                cacheApiData.setResponseBody(readFromBuffer(buffer.clone(), charset));
            }
//            else {
//                transaction.setResponseBodyIsPlainText(false);
//            }
//            transaction.setResponseContentLength(buffer.size());
        }
    }

    private String readFromBuffer(Buffer buffer, Charset charset) {
        long bufferSize = buffer.size();
        long maxBytes = Math.min(bufferSize, maxContentLength);
        String body = "";
        try {
            body = buffer.readString(maxBytes, charset);
        } catch (EOFException e) {
//            body += context.getString(com.readystatesoftware.chuck.R.string.chuck_body_unexpected_eof);
        }
        if (bufferSize > maxContentLength) {
//            body += context.getString(com.readystatesoftware.chuck.R.string.chuck_body_content_truncated);
        }
        return body;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private boolean isPlaintext(Buffer buffer) {
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

    private boolean bodyGzipped(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return "gzip".equalsIgnoreCase(contentEncoding);
    }

    private BufferedSource getNativeSource(Response response) throws IOException {
        if (bodyGzipped(response.headers())) {
            BufferedSource source = response.peekBody(maxContentLength).source();
            if (source.buffer().size() < maxContentLength) {
                return getNativeSource(source, true);
            } else {
                Log.w(LOG_TAG, "gzip encoded response was too long");
            }
        }
        return response.body().source();
    }

    private BufferedSource getNativeSource(BufferedSource input, boolean isGzipped) {
        if (isGzipped) {
            GzipSource source = new GzipSource(input);
            return Okio.buffer(source);
        } else {
            return input;
        }
    }

    private CacheApiData setUrl(CacheApiData cacheApiData, String url) {
        Uri uri = Uri.parse(url);
        cacheApiData.setHost(uri.getHost());
        cacheApiData.setPath(uri.getPath());
        cacheApiData.setRequestParam(((uri.getQuery() != null) ? "?" + uri.getQuery().trim() : ""));

        URI uri2 = null;
        try {
            uri2 = new URI(url);
            UrlEncodedQueryString queryString = UrlEncodedQueryString.parse(uri2);
            queryString.remove("hash");
            queryString.remove("device_time");
            Log.d(LOG_TAG, "sample : "+queryString);
            cacheApiData.setRequestParam(((queryString != null) ? "?" + queryString.toString().trim() : ""));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return cacheApiData;
    }
}
