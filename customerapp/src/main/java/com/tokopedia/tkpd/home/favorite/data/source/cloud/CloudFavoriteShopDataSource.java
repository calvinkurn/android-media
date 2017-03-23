package com.tokopedia.tkpd.home.favorite.data.source.cloud;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.ServiceV4;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.home.favorite.data.AddFavoriteShopMapper;
import com.tokopedia.tkpd.home.favorite.data.FavoriteShopMapper;
import com.tokopedia.tkpd.home.favorite.domain.model.FavShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * @author Kulomady on 1/19/17.
 */
public class CloudFavoriteShopDataSource {
    private Context context;
    private Gson gson;
    private ServiceV4 serviceV4;

    public CloudFavoriteShopDataSource(Context context, Gson gson, ServiceV4 serviceV4) {
        this.context = context;
        this.gson = gson;
        this.serviceV4 = serviceV4;
    }

    public Observable<FavoriteShop> getFavorite(
            TKPDMapParam<String, String> param, boolean isMustSaveToCache) {

        TKPDMapParam<String, String> paramWithAuth = AuthUtil.generateParamsNetwork(context, param);
        if (isMustSaveToCache) {
            return serviceV4.getFavoriteShop(paramWithAuth)
                    .doOnNext(saveToCache())
                    .map(new FavoriteShopMapper(context, gson));
        } else {
            return serviceV4.getFavoriteShop(paramWithAuth)
                    .map(new FavoriteShopMapper(context, gson));
        }
    }

    public Observable<FavShop> postFavoriteShop(TKPDMapParam<String, String> param) {
        TKPDMapParam<String, String> paramWithAuth = AuthUtil.generateParamsNetwork(context, param);
        return serviceV4.postFavoriteShop(paramWithAuth)
                .map(new AddFavoriteShopMapper(context, gson));
    }

    private Action1<Response<String>> saveToCache() {
        return new Action1<Response<String>>() {
            @Override
            public void call(Response<String> response) {
                int tenMinute = 600000;
                new GlobalCacheManager()
                        .setKey(TkpdCache.Key.FAVORITE_SHOP)
                        .setCacheDuration(tenMinute)
                        .setValue(response.body())
                        .store();
            }
        };
    }

}
