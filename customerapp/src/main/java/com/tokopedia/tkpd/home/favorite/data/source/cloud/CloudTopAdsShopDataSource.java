package com.tokopedia.tkpd.home.favorite.data.source.cloud;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.TopAdsService;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.home.favorite.data.TopAdsShopMapper;
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
                .doOnNext(saveToCache())
                .map(new TopAdsShopMapper(context, gson));
    }

    private Action1<? super Response<String>> saveToCache() {
        return new Action1<Response<String>>() {
            @Override
            public void call(Response<String> response) {
                int tenMinute = 600000;
                new GlobalCacheManager()
                        .setKey(TkpdCache.Key.TOP_ADS_SHOP)
                        .setCacheDuration(tenMinute)
                        .setValue(response.body())
                        .store();
            }
        };
    }
}
