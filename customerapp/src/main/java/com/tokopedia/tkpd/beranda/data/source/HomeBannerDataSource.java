package com.tokopedia.tkpd.beranda.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.etc.apis.home.CategoryApi;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.beranda.data.mapper.HomeBannerMapper;
import com.tokopedia.tkpd.beranda.domain.model.banner.HomeBannerResponseModel;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;


/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeBannerDataSource {

    private static final String TAG = HomeBannerDataSource.class.getSimpleName();
    private final Context context;
    private final CategoryApi categoryApi;
    private final HomeBannerMapper homeBannerMapper;
    private final GlobalCacheManager cacheManager;
    private final Gson gson;

    public HomeBannerDataSource(Context context, CategoryApi categoryApi,
                                HomeBannerMapper homeBannerMapper,
                                GlobalCacheManager cacheManager,
                                Gson gson) {
        this.context = context;
        this.categoryApi = categoryApi;
        this.homeBannerMapper = homeBannerMapper;
        this.cacheManager = cacheManager;
        this.gson = gson;
    }

    public Observable<HomeBannerResponseModel> getHomeBanner(final RequestParams requestParams) {
        return getCloud(requestParams);
    }

    @NonNull
    private Observable<HomeBannerResponseModel> getCloud(RequestParams requestParams) {
        return categoryApi.getBanners(SessionHandler.getLoginID(context), requestParams.getParameters())
                .map(homeBannerMapper)
                .doOnNext(saveToCache());
    }

    private Action1<HomeBannerResponseModel> saveToCache() {
        return new Action1<HomeBannerResponseModel>() {
            @Override
            public void call(HomeBannerResponseModel homeBannerResponseModel) {
                cacheManager.setKey(TkpdCache.Key.HOME_BANNER_CACHE);
                cacheManager.setValue(gson.toJson(homeBannerResponseModel));
                cacheManager.store();
            }
        };
    }

    public Observable<HomeBannerResponseModel> getCache() {
        return Observable.just(true).map(new Func1<Boolean, HomeBannerResponseModel>() {
            @Override
            public HomeBannerResponseModel call(Boolean aBoolean) {
                String cache = cacheManager.getValueString(TkpdCache.Key.HOME_BANNER_CACHE);
                if (cache != null)
                    return gson.fromJson(cache, HomeBannerResponseModel.class);
                throw new RuntimeException("Cache is empty!!");
            }
        });
    }
}
