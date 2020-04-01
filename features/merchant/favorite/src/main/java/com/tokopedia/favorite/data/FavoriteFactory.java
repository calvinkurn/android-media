package com.tokopedia.favorite.data;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import com.tokopedia.favorite.data.source.apis.service.TopAdsService;
import com.tokopedia.favorite.data.source.cloud.CloudFavoriteShopDataSource;
import com.tokopedia.favorite.data.source.cloud.CloudTopAdsShopDataSource;
import com.tokopedia.favorite.data.source.local.LocalFavoriteShopDataSource;
import com.tokopedia.favorite.data.source.local.LocalTopAdsShopDataSource;
import com.tokopedia.favorite.domain.model.FavShop;
import com.tokopedia.favorite.domain.model.FavoriteShop;
import com.tokopedia.favorite.domain.model.TopAdsShop;

import java.util.HashMap;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author Kulomady on 1/18/17.
 */

public class FavoriteFactory {
    private Context context;
    private Gson gson;
    private TopAdsService topAdsService;

    public FavoriteFactory(Context context, Gson gson,
                           TopAdsService topAdsService) {

        this.context = context;
        this.gson = gson;
        this.topAdsService = topAdsService;
    }

    Observable<FavoriteShop> getFavoriteShop(HashMap<String, String> param) {

        return new CloudFavoriteShopDataSource(
                context, gson).getFavorite(param, false);
    }


    Observable<FavoriteShop> getFavoriteShopFirstPage(HashMap<String, String> params) {
        return getCloudFavoriteObservable(params)
                .onExceptionResumeNext(
                        getLocalFavoriteObservable().doOnNext(setFavoriteErrorNetwork()));
    }


    Observable<TopAdsShop> getTopAdsShop(HashMap<String, Object> params) {
        return Observable.concat(
                getLocalTopAdsShopObservable(),
                getCloudTopAdsShopObservable(params))
                .first(isLocalTopAdsShopValid());

    }

    Observable<TopAdsShop> getFreshTopAdsShop(HashMap<String, Object> params) {
        CloudTopAdsShopDataSource topAdsShopDataSource
                = new CloudTopAdsShopDataSource(context, gson, topAdsService);
        return topAdsShopDataSource.getTopAdsShop(params)
                .onExceptionResumeNext(
                        getLocalTopAdsShopObservable().doOnNext(setTopAdsShopErrorNetwork()));
    }

    private Observable<TopAdsShop> getCloudTopAdsShopObservable(HashMap<String, Object> params) {
        CloudTopAdsShopDataSource topAdsShopDataSource
                = new CloudTopAdsShopDataSource(context, gson, topAdsService);

        return topAdsShopDataSource.getTopAdsShop(params);
    }


    private Observable<TopAdsShop> getLocalTopAdsShopObservable() {
        return new LocalTopAdsShopDataSource(context, gson).getTopAdsShop();
    }


    private Observable<FavoriteShop> getLocalFavoriteObservable() {
        return new LocalFavoriteShopDataSource(context, gson).getFavorite();
    }

    private Observable<FavoriteShop> getCloudFavoriteObservable(HashMap<String, String> param) {
        return new CloudFavoriteShopDataSource(
                context, gson).getFavorite(param, true);
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
