package com.tokopedia.core.cache.domain.interactor;

import android.net.Uri;
import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.UrlEncodedQueryString;
import com.tokopedia.core.cache.data.repository.ApiCacheRepositoryImpl;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.domain.ApiCacheRepository;

import java.net.URI;
import java.net.URISyntaxException;

import rx.Observable;

/**
 * Created by User on 8/30/2017.
 */

public abstract class BaseApiCacheInterceptor<E> extends UseCase<E> {

    public static final String METHOD = "METHOD";
    public static final String FULL_URL = "FULL_URL";
    private static final String TAG = "BaseApiCacheInterceptor";
    protected final ApiCacheRepository apiCacheRepository;
    protected String method;
    protected String url;
    protected CacheApiData cacheApiData;

    public BaseApiCacheInterceptor(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ApiCacheRepository apiCacheRepository) {
        super(threadExecutor, postExecutionThread);
        this.apiCacheRepository = apiCacheRepository;
    }

    public BaseApiCacheInterceptor(ApiCacheRepositoryImpl apiCacheRepository) {
        this.apiCacheRepository = apiCacheRepository;
    }

    @Override
    public final Observable<E> createObservable(RequestParams requestParams) {

        method = requestParams.getString(METHOD, "");
        url = requestParams.getString(FULL_URL, "");

        cacheApiData = new CacheApiData();
        cacheApiData.setMethod(method); // get method
        cacheApiData = setUrl(cacheApiData, url);

        return createChildObservable(requestParams);
    }

    public abstract Observable<E> createChildObservable(RequestParams requestParams);

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
            Log.d(TAG, "sample : " + queryString);
            cacheApiData.setRequestParam(((queryString != null) ? "?" + queryString.toString().trim() : ""));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return cacheApiData;
    }
}
