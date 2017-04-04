package com.tokopedia.tkpd.home.favorite.data;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.base.common.service.ServiceV4;
import com.tokopedia.core.base.common.service.TopAdsService;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.home.favorite.data.source.cloud.CloudFavoriteShopDataSource;
import com.tokopedia.tkpd.home.favorite.data.source.cloud.CloudTopAdsShopDataSource;
import com.tokopedia.tkpd.home.favorite.data.source.cloud.CloudWishlistDataStore;
import com.tokopedia.tkpd.home.favorite.data.source.local.LocalFavoriteShopDataSource;
import com.tokopedia.tkpd.home.favorite.data.source.local.LocalTopAdsShopDataSource;
import com.tokopedia.tkpd.home.favorite.data.source.local.LocalWishlistDataSource;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;
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
    private MojitoService mojitoService;
    private GlobalCacheManager cacheManager;

    public FavoriteFactory(Context context, Gson gson, ServiceV4 serviceVersion4,
                           TopAdsService topAdsService, MojitoService mojitoService,
                           GlobalCacheManager cacheManager) {

        this.context = context;
        this.gson = gson;
        serviceV4 = serviceVersion4;
        this.topAdsService = topAdsService;
        this.mojitoService = mojitoService;
        this.cacheManager = cacheManager;
    }

    Observable<DomainWishlist> getWishlist(TKPDMapParam<String, Object> param) {

        return cloudWishlistObservable(param)
                .onExceptionResumeNext(
                        localWishlistObservable().doOnNext(setWishlistNetworkError()));

    }



    Observable<DomainWishlist> getFreshWishlist(TKPDMapParam<String, Object> param) {
        return cloudWishlistObservable(param);
    }

    Observable<FavoriteShop> getFavoriteShop(TKPDMapParam<String, String> param) {

        return new CloudFavoriteShopDataSource(
                context, gson, serviceV4).getFavorite(param, false);
    }


    Observable<FavoriteShop> getFavoriteShopFirstPage(TKPDMapParam<String, String> params) {
        return getCloudFavoriteObservable(params)
                .onExceptionResumeNext(
                        getLocalFavoriteObservable().doOnNext(setFavoriteNetworkError()));
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
        return topAdsShopDataSource.getTopAdsShop(params);
    }

    Observable<FavShop> postFavShop(TKPDMapParam<String, String> param) {
        return new CloudFavoriteShopDataSource(context, gson, serviceV4).postFavoriteShop(param);
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

    private Observable<DomainWishlist> cloudWishlistObservable(TKPDMapParam<String, Object> param) {
        String userId = SessionHandler.getLoginID(context);
        return new CloudWishlistDataStore(context, gson, mojitoService).getWishlist(userId,param);
    }

    private Observable<DomainWishlist> localWishlistObservable() {
        return new LocalWishlistDataSource(context, gson, cacheManager).getWishlist();
    }

    private Action1<DomainWishlist> setWishlistNetworkError() {
        return new Action1<DomainWishlist>() {
            @Override
            public void call(DomainWishlist domainWishlist) {
                domainWishlist.setNetworkError(true);
            }
        };
    }

    private Action1<FavoriteShop> setFavoriteNetworkError() {
        return new Action1<FavoriteShop>() {
            @Override
            public void call(FavoriteShop favoriteShop) {
                favoriteShop.setNetworkError(true);
            }
        };
    }


}
