
package com.tokopedia.core.cache.domain.interactor;

import android.net.Uri;
import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.UrlEncodedQueryString;
import com.tokopedia.core.cache.data.repository.ApiCacheRepositoryImpl;
import com.tokopedia.core.cache.data.source.cache.ApiCacheDataSource;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist;

import java.io.EOFException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Calendar;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import rx.Observable;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class ApiCacheInterceptorUseCase extends UseCase<CacheApiData> {

    public static final String METHOD = "METHOD";
    public static final String FULL_URL = "FULL_URL";
    private static final String TAG = "ApiCacheInterceptorUseC";
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private String method;
    private String url;
    private ApiCacheDataSource cacheHelper = new ApiCacheDataSource();
    private boolean isInWhiteList;
    private CacheApiWhitelist whiteList;
    private boolean isEmptyData, isExpiredData;
    private CacheApiData tempData;
    private long maxContentLength = 250000L;
    private CacheApiData cacheApiData;
    private ApiCacheRepositoryImpl apiCacheRepository;

    public ApiCacheInterceptorUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ApiCacheRepositoryImpl apiCacheRepository) {
        super(threadExecutor, postExecutionThread);
        this.apiCacheRepository = apiCacheRepository;
    }

    public ApiCacheInterceptorUseCase(ApiCacheRepositoryImpl apiCacheRepository) {
        this.apiCacheRepository = apiCacheRepository;
    }

    @Override
    public Observable<CacheApiData> createObservable(RequestParams requestParams) {
        method = requestParams.getString(METHOD, "");
        url = requestParams.getString(FULL_URL, "");

        cacheApiData = new CacheApiData();
        cacheApiData.setMethod(method); // get method
        cacheApiData = setUrl(cacheApiData, url);
        if (apiCacheRepository.isInWhiteList(cacheApiData.getHost(), cacheApiData.getPath())) {
            tempData = cacheHelper.queryDataFrom(cacheApiData.getHost(), cacheApiData.getPath(), cacheApiData.getRequestParam());
            isEmptyData = (tempData == null);

            if (!isEmptyData) {

                cacheApiData.setResponseDate(tempData.responseDate);
                cacheApiData.setExpiredDate(tempData.expiredDate);
                cacheApiData.setResponseBody(tempData.responseBody);
            }

        }
        return Observable.just(cacheApiData);
    }

    public boolean isInWhiteList() {
        return isInWhiteList;
    }

    public CacheApiWhitelist isInWhiteListRaw() {
        return whiteList;
    }

    /**
     * set host, path and request param to {@link CacheApiData} objet and remove param that is not unique.
     *
     * @param cacheApiData
     * @param url
     * @return
     */
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
//            Log.d(TAG, "sample : " + queryString);
            cacheApiData.setRequestParam(((queryString != null) ? "?" + queryString.toString().trim() : ""));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return cacheApiData;
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
                Log.w(TAG, "gzip encoded response was too long");
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

    public void updateResponse(Response response) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, (int) whiteList.getExpiredTime());
        cacheApiData.setResponseDate(System.currentTimeMillis() / 1000L);
        cacheApiData.setExpiredDate(instance.getTimeInMillis() / 1000L);

        try {
            putResponseBody(cacheApiData, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        cacheApiData.save();
    }
}
