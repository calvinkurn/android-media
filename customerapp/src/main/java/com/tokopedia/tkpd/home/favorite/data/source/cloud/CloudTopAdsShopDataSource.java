package com.tokopedia.tkpd.home.favorite.data.source.cloud;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.TopAdsService;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.home.favorite.data.mapper.TopAdsShopMapper;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

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
                .doOnNext(validateError())
                .map(new TopAdsShopMapper(context, gson));
    }

    private Action1<Response<String>> validateError() {
        return new Action1<Response<String>>() {
            @Override
            public void call(Response<String> stringResponse) {
                if (stringResponse.code() >= 500 && stringResponse.code() < 600) {
                    throw new RuntimeException("Server Error!");
                } else if (stringResponse.code() >= 400 && stringResponse.code() < 500) {
                    throw new RuntimeException("Client Error!");
                } else {
                    saveResponseToCache(stringResponse);
                }
            }
        };
    }

    private void saveResponseToCache(Response<String> stringResponse) {
        int tenMinute = 600000;
        new GlobalCacheManager()
                .setKey(TkpdCache.Key.TOP_ADS_SHOP)
                .setCacheDuration(tenMinute)
                .setValue(stringResponse.body())
                .store();
    }

}
