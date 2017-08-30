package com.tokopedia.core.cache.domain.interactor;

import android.net.Uri;
import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.cache.UrlEncodedQueryString;
import com.tokopedia.core.cache.data.source.cache.CacheHelper;
import com.tokopedia.core.cache.data.source.db.CacheApiData;
import com.tokopedia.core.cache.data.source.db.CacheApiWhitelist;

import java.net.URI;
import java.net.URISyntaxException;

import rx.Observable;

/**
 * Created by normansyahputa on 8/30/17.
 */

public class ApiCacheInterceptorUseCase extends UseCase<CacheApiData> {

    private static final String TAG = "ApiCacheInterceptorUseC";
    private String method;
    private String url;
    private CacheHelper cacheHelper = new CacheHelper();
    private boolean isInWhiteList;
    private CacheApiWhitelist whiteList;
    private boolean isEmptyData, isExpiredData;
    private CacheApiData tempData;


    public ApiCacheInterceptorUseCase(String method, String url) {
        this.method = method;
        this.url = url;
    }

    @Override
    public Observable<CacheApiData> createObservable(RequestParams requestParams) {
        CacheApiData cacheApiData = new CacheApiData();
        cacheApiData.setMethod(method);
        cacheApiData = setUrl(cacheApiData, url);
        if (isInWhiteList(cacheApiData)) {
            tempData = cacheHelper.queryDataFrom(cacheApiData.getHost(), cacheApiData.getPath(), cacheApiData.getRequestParam());
            isEmptyData = (tempData == null);

            if (!isEmptyData) {
                isExpiredData = (System.currentTimeMillis() / 1000L) - tempData.getResponseDate() > whiteList.getExpiredTime();
                if (isExpiredData) {
                    tempData.delete();
                }
            }

        }
        return Observable.just(cacheApiData);
    }

    public CacheApiData getTempData() {
        return tempData;
    }

    public boolean isExpiredData() {
        return isExpiredData;
    }

    public boolean isEmptyData() {
        return isEmptyData;
    }

    public boolean isInWhiteList(CacheApiData cacheApiData) {
        whiteList = cacheHelper.queryFromRaw(cacheApiData.getHost(), cacheApiData.getPath());
        return isInWhiteList = (whiteList != null);
    }

    public boolean isInWhiteList() {
        return isInWhiteList;
    }

    public CacheApiWhitelist isInWhiteListRaw() {
        return whiteList;
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
            Log.d(TAG, "sample : " + queryString);
            cacheApiData.setRequestParam(((queryString != null) ? "?" + queryString.toString().trim() : ""));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return cacheApiData;
    }
}
