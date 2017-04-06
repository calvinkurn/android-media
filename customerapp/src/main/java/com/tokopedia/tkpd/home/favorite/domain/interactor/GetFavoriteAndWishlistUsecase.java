package com.tokopedia.tkpd.home.favorite.domain.interactor;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.favorite.domain.model.DataFavorite;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;

import rx.Observable;
import rx.functions.Func3;


/**
 * @author Kulomady on 1/19/17.
 */

public class GetFavoriteAndWishlistUsecase extends UseCase<DataFavorite> {

    private GetFavoriteShopUsecase getFavoriteShopUsecase;
    private GetWishlistUsecase getWishlistUsecase;
    private final GetTopAdsShopUseCase getTopAdsShopUseCase;

    public GetFavoriteAndWishlistUsecase(ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread,
                                         GetFavoriteShopUsecase getFavoriteShopUsecase,
                                         GetWishlistUsecase getWishlistUsecase,
                                         GetTopAdsShopUseCase getTopAdsShopUseCase) {
        super(threadExecutor, postExecutionThread);
        this.getFavoriteShopUsecase = getFavoriteShopUsecase;
        this.getWishlistUsecase = getWishlistUsecase;
        this.getTopAdsShopUseCase = getTopAdsShopUseCase;
    }

    @Override
    public Observable<DataFavorite> createObservable(RequestParams requestParams) {
//        return Observable.zip(getWishlist(), getFavoriteShop(),
//                new Func2<DomainWishlist, FavoriteShop, DataFavorite>() {
//
//                    @Override
//                    public DataFavorite call(DomainWishlist domainWishlist, FavoriteShop favoriteShop) {
//                        return validateDataFavorite(domainWishlist, favoriteShop);
//                    }
//                });
        return Observable.zip(getWishlist(), getTopAdsShop(), getFavoriteShop(),
                new Func3<DomainWishlist, TopAdsShop, FavoriteShop, DataFavorite>() {

                    @Override
                    public DataFavorite call(DomainWishlist domainWishlist,
                                             TopAdsShop adsShop, FavoriteShop favoriteShop) {

                        return validateDataFavorite(domainWishlist, adsShop, favoriteShop);
                    }
                });
    }


    @NonNull
    private DataFavorite validateDataFavorite(DomainWishlist domainWishlist,
                                              TopAdsShop adsShop, FavoriteShop favoriteShop) {

        if (domainWishlist.isNetworkError()
                && adsShop.isNetworkError()
                && favoriteShop.isNetworkError()
                && domainWishlist.getData() == null
                && adsShop.getTopAdsShopItemList() == null
                && favoriteShop.getData() == null) {

            throw new RuntimeException("all network error");
        }

        DataFavorite dataFavorite = new DataFavorite();
        dataFavorite.setWishListData(domainWishlist);
        dataFavorite.setTopAdsShop(adsShop);
        dataFavorite.setFavoriteShop(favoriteShop);
        return dataFavorite;
    }

    private Observable<FavoriteShop> getFavoriteShop() {
        RequestParams defaultParams = GetFavoriteShopUsecase.getDefaultParams();
        defaultParams.putBoolean(GetFavoriteShopUsecase.KEY_IS_FIRST_PAGE, true);
        return getFavoriteShopUsecase.createObservable(defaultParams);
    }

    private Observable<DomainWishlist> getWishlist() {
        RequestParams params = GetWishlistUsecase.getDefaultParams();
        params.putBoolean(GetWishlistUsecase.KEY_IS_FORCE_REFRESH, false);
        return getWishlistUsecase.createObservable(params);
    }

    private Observable<TopAdsShop> getTopAdsShop() {
        RequestParams requestParams = GetTopAdsShopUseCase.defaultParams();
        requestParams.putBoolean(GetTopAdsShopUseCase.KEY_IS_FORCE_REFRESH, false);
        return getTopAdsShopUseCase.createObservable(requestParams);
    }
}
