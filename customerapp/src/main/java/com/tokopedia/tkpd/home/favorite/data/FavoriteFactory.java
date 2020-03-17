package com.tokopedia.tkpd.home.favorite.data;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.ServiceV4;
import com.tokopedia.core.base.common.service.TopAdsService;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.favorite.data.source.cloud.CloudFavoriteShopDataSource;
import com.tokopedia.tkpd.home.favorite.data.source.cloud.CloudTopAdsShopDataSource;
import com.tokopedia.tkpd.home.favorite.data.source.local.LocalFavoriteShopDataSource;
import com.tokopedia.tkpd.home.favorite.data.source.local.LocalTopAdsShopDataSource;
import com.tokopedia.tkpd.home.favorite.domain.model.FavShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author Kulomady on 1/18/17.
 */

public class FavoriteFactory {
    private Context context;
    private Gson gson;
    private ServiceV4 serviceV4;
    private TopAdsService topAdsService;
    private GlobalCacheManager cacheManager;

    public FavoriteFactory(Context context, Gson gson, ServiceV4 serviceVersion4,
                           TopAdsService topAdsService,
                           GlobalCacheManager cacheManager) {

        this.context = context;
        this.gson = gson;
        serviceV4 = serviceVersion4;
        this.topAdsService = topAdsService;
        this.cacheManager = cacheManager;
    }

    Observable<FavoriteShop> getFavoriteShop(TKPDMapParam<String, String> param) {

        return new CloudFavoriteShopDataSource(
                context, gson, serviceV4).getFavorite(param, false);
    }


    Observable<FavoriteShop> getFavoriteShopFirstPage(TKPDMapParam<String, String> params) {
        return getCloudFavoriteObservable(params)
                .onExceptionResumeNext(
                        getLocalFavoriteObservable().doOnNext(setFavoriteErrorNetwork()));
    }


    Observable<TopAdsShop> getTopAdsShop(TKPDMapParam<String, Object> params) {
        return Observable.concat(
                getLocalTopAdsShopObservable(),
                getCloudTopAdsShopObservable(params))
                .first(isLocalTopAdsShopValid());

    }

    Observable<TopAdsShop> getFreshTopAdsShop(TKPDMapParam<String, Object> params) {
        CloudTopAdsShopDataSource topAdsShopDataSource
                = new CloudTopAdsShopDataSource(context, gson, topAdsService);
        return topAdsShopDataSource.getTopAdsShop(params)
                .onExceptionResumeNext(
                        getLocalTopAdsShopObservable().doOnNext(setTopAdsShopErrorNetwork()));
    }

    Observable<FavShop> postFavShop(TKPDMapParam<String, String> param) {
        return new CloudFavoriteShopDataSource(context, gson, serviceV4).postFavoriteShop(param);
    }

    private Observable<TopAdsShop> getCloudTopAdsShopObservable(TKPDMapParam<String, Object> params) {
        CloudTopAdsShopDataSource topAdsShopDataSource
                = new CloudTopAdsShopDataSource(context, gson, topAdsService);

        return topAdsShopDataSource.getTopAdsShop(params);
    }


    private Observable<TopAdsShop> getLocalTopAdsShopObservable() {
        return new LocalTopAdsShopDataSource(context, gson, cacheManager).getTopAdsShop();
    }


    private Observable<FavoriteShop> getLocalFavoriteObservable() {
        return new LocalFavoriteShopDataSource(context, gson, cacheManager).getFavorite();
    }

    private Observable<FavoriteShop> getCloudFavoriteObservable(TKPDMapParam<String, String> param) {
        return new CloudFavoriteShopDataSource(
                context, gson, serviceV4).getFavorite(param, true);
    }

    private Action1<FavoriteShop> setFavoriteErrorNetwork() {
        return new Action1<FavoriteShop>() {
            @Override
            public void call(FavoriteShop favoriteShop) {
                favoriteShop.setNetworkError(true);
            }
        };
    }

    @NonNull
    private Action1<TopAdsShop> setTopAdsShopErrorNetwork() {
        return new Action1<TopAdsShop>() {
            @Override
            public void call(TopAdsShop topAdsShop) {
                topAdsShop.setNetworkError(true);
            }
        };
    }

    private Func1<TopAdsShop, Boolean> isLocalTopAdsShopValid() {
        return new Func1<TopAdsShop, Boolean>() {
            @Override
            public Boolean call(TopAdsShop topAdsShop) {
                return topAdsShop != null
                        && topAdsShop.isDataValid()
                        && topAdsShop.getTopAdsShopItemList() != null
                        && topAdsShop.getTopAdsShopItemList().size() > 0;
            }
        };
    }

}
