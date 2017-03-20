package com.tokopedia.tkpd.home.favorite.data.source.cloud;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.ServiceV4;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.home.favorite.data.FavoriteShopMapper;
import com.tokopedia.tkpd.home.favorite.data.SetFavoriteShopMapper;
import com.tokopedia.tkpd.home.favorite.domain.interactor.ShopItemParam;
import com.tokopedia.tkpd.home.favorite.domain.model.FavShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * @author Kulomady on 1/19/17.
 */
public class CloudFavoriteShopDataSource {
    private Context mContext;
    private Gson mGson;
    private ServiceV4 mServiceVersion4;

    public CloudFavoriteShopDataSource(Context context, Gson gson, ServiceV4 serviceVersion4) {
        mContext = context;
        mGson = gson;
        mServiceVersion4 = serviceVersion4;
    }

    public Observable<FavoriteShop> getFavorite(TKPDMapParam<String, String> param) {
        TKPDMapParam<String, String> paramWithAuth = AuthUtil.generateParamsNetwork(mContext, param);
        return mServiceVersion4.getFavoriteShop(paramWithAuth)
                .doOnNext(saveToCache())
                .map(new FavoriteShopMapper(mContext, mGson));
    }

    public Observable<FavShop> postFavoriteShop(TKPDMapParam<String, String> param, ShopItemParam shopItem) {
        TKPDMapParam<String, String> paramWithAuth = AuthUtil.generateParamsNetwork(mContext, param);
        return mServiceVersion4.postFavoriteShop(paramWithAuth)
                .map(new SetFavoriteShopMapper(mContext, mGson, shopItem));
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
