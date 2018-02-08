package com.tokopedia.tkpd.beranda.data.source;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.beranda.data.mapper.HomeCategoryMapper;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandsOfficialStoreResponseModel;
import com.tokopedia.tkpd.beranda.domain.model.category.HomeCategoryResponseModel;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;


/**
 * @author by errysuprayogi on 11/27/17.
 */

public class HomeCategoryDataSource {

    private final MojitoApi mojitoApi;
    private final HomeCategoryMapper homeCategoryMapper;
    private final SessionHandler sessionHandler;
    private final GlobalCacheManager cacheManager;
    private final Gson gson;

    public HomeCategoryDataSource(MojitoApi mojitoApi, HomeCategoryMapper homeCategoryMapper,
                                  SessionHandler sessionHandler, GlobalCacheManager cacheManager, Gson gson) {
        this.mojitoApi = mojitoApi;
        this.homeCategoryMapper = homeCategoryMapper;
        this.sessionHandler = sessionHandler;
        this.cacheManager = cacheManager;
        this.gson = gson;
    }

    public Observable<HomeCategoryResponseModel> getHomeCategory() {
        return getCloud();
    }

    @NonNull
    private Observable<HomeCategoryResponseModel> getCloud() {
        return mojitoApi.getHomeCategoryMenuV2(sessionHandler.getLoginID(), GlobalConfig.getPackageApplicationName())
                .map(homeCategoryMapper)
                .doOnNext(saveToCache());
    }

    @NonNull
    private Action1<HomeCategoryResponseModel> saveToCache() {
        return new Action1<HomeCategoryResponseModel>() {
            @Override
            public void call(HomeCategoryResponseModel homeCategoryResponseModel) {
                cacheManager.setKey(TkpdCache.Key.HOME_CATEGORY_CACHE);
                cacheManager.setValue(gson.toJson(homeCategoryResponseModel));
                cacheManager.store();
            }
        };
    }

    public Observable<HomeCategoryResponseModel> getCache() {
        return Observable.just(true).map(new Func1<Boolean, HomeCategoryResponseModel>() {
            @Override
            public HomeCategoryResponseModel call(Boolean aBoolean) {
                String cache = cacheManager.getValueString(TkpdCache.Key.HOME_CATEGORY_CACHE);
                if (cache != null)
                    return gson.fromJson(cache, HomeCategoryResponseModel.class);
                throw new RuntimeException("Cache is empty!!");
            }
        });
    }
}
