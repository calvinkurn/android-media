package com.tokopedia.tkpd.home.favorite.domain.interactor;

import android.support.annotation.NonNull;

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
                    public DataFavorite call(
                            DomainWishlist domainWishlist, FavoriteShop favoriteShop) {

                        DataFavorite dataFavorite = new DataFavorite();
                        dataFavorite.setWishListData(domainWishlist);
                        dataFavorite.setFavoriteShop(favoriteShop);
                        return dataFavorite;
                    }
                });
    }

    private Observable<FavoriteShop> getFavoriteShop() {
        return getFavoriteShopUsecase.createObservable(buildFavoriteParams());
    }

    private Observable<DomainWishlist> getWishlist() {
        return getWishlistUsecase.createObservable(buildWishlistTkpdMapParam());
    }

    @NonNull
    private RequestParams buildFavoriteParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(
                GetFavoriteShopUsecase.KEY_OPTION_LOCATION,
                GetFavoriteShopUsecase.DEFAULT_OPTION_LOCATION
        );
        requestParams.putString(
                GetFavoriteShopUsecase.KEY_OPTION_NAME, GetFavoriteShopUsecase.DEFAULT_OPTION_NAME);
        requestParams.putString(
                GetFavoriteShopUsecase.KEY_PER_PAGE, GetFavoriteShopUsecase.DEFAULT_PER_PAGE);
        requestParams.putString(
                GetFavoriteShopUsecase.KEY_PAGE, GetFavoriteShopUsecase.INITIAL_VALUE);

        return requestParams;
    }


    @NonNull
    private RequestParams buildWishlistTkpdMapParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(
                GetWishlistUsecase.KEY_COUNT, GetWishlistUsecase.DEFAULT_COUNT_VALUE);
        requestParams.putString(
                GetWishlistUsecase.KEY_PAGE, GetWishlistUsecase.DEFAULT_PAGE_VALUE);

        return requestParams;
    }


}
