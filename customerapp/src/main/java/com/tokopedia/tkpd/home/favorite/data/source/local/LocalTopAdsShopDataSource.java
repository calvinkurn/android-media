package com.tokopedia.tkpd.home.favorite.data.source.local;

import android.content.Context;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.home.favorite.data.mapper.TopAdsShopMapper;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author Kulomady on 2/13/17.
 */
public class LocalTopAdsShopDataSource {

    private final Context context;
    private final Gson gson;
    private final GlobalCacheManager cacheManager;

    public LocalTopAdsShopDataSource(Context context, Gson gson, GlobalCacheManager cacheManager) {
        this.context = context;
        this.gson = gson;
        this.cacheManager = cacheManager;
    }

    public Observable<TopAdsShop> getTopAdsShop() {
        Response<String> data
                = Response.success(cacheManager.getValueString(TkpdCache.Key.TOP_ADS_SHOP));
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
