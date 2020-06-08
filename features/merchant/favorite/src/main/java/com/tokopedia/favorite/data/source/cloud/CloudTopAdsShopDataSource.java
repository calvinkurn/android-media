package com.tokopedia.favorite.data.source.cloud;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.favorite.data.mapper.TopAdsShopMapper;
import com.tokopedia.favorite.data.source.apis.service.TopAdsService;
import com.tokopedia.favorite.data.source.local.LocalTopAdsShopDataSource;
import com.tokopedia.favorite.domain.model.TopAdsShop;
import com.tokopedia.favorite.utils.HttpResponseValidator;

import java.util.HashMap;

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

    public Observable<TopAdsShop> getTopAdsShop(HashMap<String, Object> param) {
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
        PersistentCacheManager.instance.put(LocalTopAdsShopDataSource.CACHE_KEY_TOP_ADS_SHOP, stringResponse.body(), - System.currentTimeMillis());
    }

}
