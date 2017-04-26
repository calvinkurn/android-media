package com.tokopedia.tkpd.home.favorite.data.source.cloud;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.TopAdsService;
import com.tokopedia.core.base.utils.HttpResponseValidator;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.home.favorite.data.mapper.TopAdsShopMapper;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;

import retrofit2.Response;
import rx.Observable;

/**
 * @author Kulomady on 1/19/17.
 */
public class CloudTopAdsShopDataSource {
    private Context context;
    private Gson gson;
    private TopAdsService topAdsService;

    public CloudTopAdsShopDataSource(Context context, Gson gson, TopAdsService topAdsService) {
        this.context = context;
        this.gson = gson;
        this.topAdsService = topAdsService;
    }

    public Observable<TopAdsShop> getTopAdsShop(TKPDMapParam<String, Object> param) {
        return topAdsService.getShopTopAds(param)
                .doOnNext(HttpResponseValidator
                        .validate(new HttpResponseValidator.HttpValidationListener() {
                            @Override
                            public void OnPassValidation(Response<String> response) {
                                saveResponseToCache(response);
                            }
                        }))
                .map(new TopAdsShopMapper(context, gson));
    }

    private void saveResponseToCache(Response<String> stringResponse) {
        new GlobalCacheManager()
                .setKey(TkpdCache.Key.TOP_ADS_SHOP)
                .setValue(stringResponse.body())
                .store();
    }

}
