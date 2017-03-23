package com.tokopedia.tkpd.home.favorite.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.favorite.domain.model.DataFavorite;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;

import rx.Observable;
import rx.functions.Func2;


/**
 * @author Kulomady on 1/19/17.
 */

public class GetFavoriteAndWishlistUsecase extends UseCase<DataFavorite> {

    private GetFavoriteShopUsecase getFavoriteShopUsecase;
    private GetWishlistUsecase getWishlistUsecase;

    public GetFavoriteAndWishlistUsecase(ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread,
                                         GetFavoriteShopUsecase getFavoriteShopUsecase,
                                         GetWishlistUsecase getWishlistUsecase) {
        super(threadExecutor, postExecutionThread);
        this.getFavoriteShopUsecase = getFavoriteShopUsecase;
        this.getWishlistUsecase = getWishlistUsecase;
    }

    @Override
    public Observable<DataFavorite> createObservable(RequestParams requestParams) {
        return Observable.zip(getWishlist(), getFavoriteShop(),
                new Func2<DomainWishlist, FavoriteShop, DataFavorite>() {

                    @Override
                    public DataFavorite call(DomainWishlist domainWishlist, FavoriteShop favoriteShop) {
                        DataFavorite dataFavorite = new DataFavorite();
                        dataFavorite.setWishListData(domainWishlist);
                        dataFavorite.setFavoriteShop(favoriteShop);
                        return dataFavorite;
                    }
                });
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


}
