package com.tokopedia.tkpd.beranda.data.source;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.beranda.data.mapper.BrandsOfficialStoreMapper;
import com.tokopedia.tkpd.beranda.domain.model.banner.HomeBannerResponseModel;
import com.tokopedia.tkpd.beranda.domain.model.brands.BrandsOfficialStoreResponseModel;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public class BrandsOfficialStoreDataSource {

    private final MojitoApi mojitoApi;
    private final BrandsOfficialStoreMapper brandsOfficialStoreMapper;
    private final GlobalCacheManager cacheManager;
    private final Gson gson;

    public BrandsOfficialStoreDataSource(MojitoApi mojitoApi, BrandsOfficialStoreMapper brandsOfficialStoreMapper,
                                         GlobalCacheManager cacheManager, Gson gson) {
        this.mojitoApi = mojitoApi;
        this.brandsOfficialStoreMapper = brandsOfficialStoreMapper;
        this.cacheManager = cacheManager;
        this.gson = gson;
    }

    public Observable<BrandsOfficialStoreResponseModel> getBrandsOfficialStore() {
        return getCloud();
    }

    @NonNull
    private Observable<BrandsOfficialStoreResponseModel> getCloud() {
        return mojitoApi.getBrandsOfficialStore().map(brandsOfficialStoreMapper)
                .doOnNext(saveToCache());
    }

    private Action1<BrandsOfficialStoreResponseModel> saveToCache() {
        return new Action1<BrandsOfficialStoreResponseModel>() {
            @Override
            public void call(BrandsOfficialStoreResponseModel brandsOfficialStoreResponseModel) {
                cacheManager.setKey(TkpdCache.Key.HOME_BRAND_OS_CACHE);
                cacheManager.setValue(gson.toJson(brandsOfficialStoreResponseModel));
                cacheManager.store();
            }
        };
    }

    public Observable<BrandsOfficialStoreResponseModel> getCache() {
        return Observable.just(true).map(new Func1<Boolean, BrandsOfficialStoreResponseModel>() {
            @Override
            public BrandsOfficialStoreResponseModel call(Boolean aBoolean) {
                String cache = cacheManager.getValueString(TkpdCache.Key.HOME_BRAND_OS_CACHE);
                if (cache != null)
                    return gson.fromJson(cache, BrandsOfficialStoreResponseModel.class);
                throw new RuntimeException("Cache is empty!!");
            }
        }).onErrorResumeNext(getCloud());
    }
}
