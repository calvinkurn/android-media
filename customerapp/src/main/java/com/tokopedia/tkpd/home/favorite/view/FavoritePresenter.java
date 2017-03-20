package com.tokopedia.tkpd.home.favorite.view;

import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetFavoriteAndWishlistUsecase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetTopAdsShopUseCase;
import com.tokopedia.tkpd.home.favorite.domain.model.DataFavorite;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;

import javax.inject.Inject;

/**
 * @author Kulomady on 1/20/17.
 */

public class FavoritePresenter
        extends BaseDaggerPresenter<FavoriteContract.View> implements FavoriteContract.Presenter {
    private static final String TAG = "FavoritePresenter";

    private final GetFavoriteAndWishlistUsecase mFavoriteAndWishlistUsecase;
    private GetTopAdsShopUseCase mGetTopAdsShopUseCase;

    @Inject
    public FavoritePresenter(GetFavoriteAndWishlistUsecase favoriteAndWishlistUsecase,
                             GetTopAdsShopUseCase getTopAdsShopUseCase) {
        mFavoriteAndWishlistUsecase = favoriteAndWishlistUsecase;
        mGetTopAdsShopUseCase = getTopAdsShopUseCase;
    }

    @Override
    public void attachView(FavoriteContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        mFavoriteAndWishlistUsecase.unsubscribe();
    }

    @Override
    public void loadDataWishlistAndFavorite() {
        mFavoriteAndWishlistUsecase.execute(
                RequestParams.EMPTY, new FavoriteANdWishlistSubscriber());
    }

    @Override
    public void loadDataTopAdsShop() {
        mGetTopAdsShopUseCase.execute(buildTopAdsShopParams(), new TopAdsShopSubscriber());
    }

    private RequestParams buildTopAdsShopParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(
                GetTopAdsShopUseCase.KEY_PAGE, GetTopAdsShopUseCase.TOPADS_PAGE_DEFAULT_VALUE);
        requestParams.putString(
                GetTopAdsShopUseCase.KEY_ITEM, GetTopAdsShopUseCase.TOPADS_ITEM_DEFAULT_VALUE);
        requestParams.putString(
                GetTopAdsShopUseCase.KEY_SRC, GetTopAdsShopUseCase.SRC_FAV_SHOP_VALUE);
        return requestParams;
    }

    private class FavoriteANdWishlistSubscriber extends rx.Subscriber<DataFavorite> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: ", e);
        }

        @Override
        public void onNext(DataFavorite dataFavorite) {
            Log.d(TAG, "onNext() called with: dataFavorite = [" + new Gson().toJson(dataFavorite) + "]");
            getView().loadWishlistAndFavoriteShop();
        }
    }


    private class TopAdsShopSubscriber extends rx.Subscriber<TopAdsShop> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: ", e);
        }

        @Override
        public void onNext(TopAdsShop topAdsShop) {
            Log.d(TAG, "onNext() called with: topAdsShop = [" + new Gson().toJson(topAdsShop) + "]");
            getView().loadTopAdsShop();
        }
    }
}
