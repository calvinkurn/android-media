package com.tokopedia.tkpd.home.favorite.domain.interactor;

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
 * @author by erry on 01/02/17.
 */

public class GetAllDataFavoriteUseCase extends UseCase<DataFavorite> {

    private GetFavoriteShopUsecase mGetFavoriteShopUsecase;
    private GetWishlistUsecase mGetWishlistUseCase;
    private GetTopAdsShopUseCase mGetTopAdsShopUseCase;

    public GetAllDataFavoriteUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     GetFavoriteShopUsecase mGetFavoriteShopUsecase,
                                     GetWishlistUsecase mGetWishlistUseCase,
                                     GetTopAdsShopUseCase mGetTopAdsShopUseCase) {
        super(threadExecutor, postExecutionThread);
        this.mGetFavoriteShopUsecase = mGetFavoriteShopUsecase;
        this.mGetWishlistUseCase = mGetWishlistUseCase;
        this.mGetTopAdsShopUseCase = mGetTopAdsShopUseCase;
    }

    @Override
    public Observable<DataFavorite> createObservable(RequestParams requestParams) {
        return Observable.zip(getWishlist(),
                getTopAdsShop(),
                getFavoriteShopList(),
                new Func3<DomainWishlist, TopAdsShop, FavoriteShop, DataFavorite>() {
                    @Override
                    public DataFavorite call(DomainWishlist domainWishlist,
                                             TopAdsShop adsShop,
                                             FavoriteShop favoriteShop) {

                        DataFavorite dataFavorite = new DataFavorite();
                        dataFavorite.setWishListData(domainWishlist);
                        dataFavorite.setTopAdsShop(adsShop);
                        dataFavorite.setFavoriteShop(favoriteShop);
                        return dataFavorite;
                    }
                });
    }

    private Observable<TopAdsShop> getTopAdsShop() {
        return mGetTopAdsShopUseCase.createObservable(GetTopAdsShopUseCase.getDefaultParams());
    }

    private Observable<DomainWishlist> getWishlist() {
        return mGetWishlistUseCase.createObservable(GetWishlistUsecase.getDefaultParams());
    }

    private Observable<FavoriteShop> getFavoriteShopList() {
        return mGetFavoriteShopUsecase.createObservable(GetFavoriteShopUsecase.getDefaultParams());
    }

}
