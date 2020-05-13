package com.tokopedia.favorite.data.source.local;

import android.content.Context;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.favorite.data.mapper.TopAdsShopMapper;
import com.tokopedia.favorite.domain.model.TopAdsShop;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author Kulomady on 2/13/17.
 */
public class LocalTopAdsShopDataSource {

    public static final String CACHE_KEY_TOP_ADS_SHOP = "TOP_ADS_SHOP";

    private final Context context;
    private final Gson gson;

    public LocalTopAdsShopDataSource(Context context, Gson gson) {
        this.context = context;
        this.gson = gson;
    }

    public Observable<TopAdsShop> getTopAdsShop() {
        Response<String> data
                = Response.success(PersistentCacheManager.instance.getString(CACHE_KEY_TOP_ADS_SHOP, null));
        return Observable.just(data)
                .map(new TopAdsShopMapper(context, gson))
                .onErrorReturn(nullResponse());
    }

    @NonNull
    private Func1<Throwable, TopAdsShop> nullResponse() {
        return new Func1<Throwable, TopAdsShop>() {
            @Override
            public TopAdsShop call(Throwable throwable) {
                return null;
            }
        };
    }
}
