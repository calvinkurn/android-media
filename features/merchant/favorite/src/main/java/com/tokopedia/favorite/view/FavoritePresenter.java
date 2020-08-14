package com.tokopedia.favorite.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.favorite.domain.interactor.GetInitialDataPageUsecase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.favorite.domain.interactor.GetAllDataFavoriteUseCase;
import com.tokopedia.favorite.domain.interactor.GetFavoriteShopUsecase;
import com.tokopedia.favorite.domain.interactor.GetTopAdsShopUseCase;
import com.tokopedia.favorite.domain.model.DataFavorite;
import com.tokopedia.favorite.domain.model.FavoriteShop;
import com.tokopedia.favorite.domain.model.FavoriteShopItem;
import com.tokopedia.favorite.domain.model.TopAdsShop;
import com.tokopedia.favorite.view.viewmodel.DataFavoriteMapper;
import com.tokopedia.favorite.view.viewmodel.FavoriteShopViewModel;
import com.tokopedia.favorite.view.viewmodel.TopAdsShopItem;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import timber.log.Timber;

/**
 * @author Kulomady on 1/20/17.
 */

public class FavoritePresenter
        extends BaseDaggerPresenter<FavoriteContract.View> implements FavoriteContract.Presenter {

    private static final String TAG = "FavoritePresenter";

    private final GetInitialDataPageUsecase getInitialDataPageUsecase;
    private final GetTopAdsShopUseCase getTopAdsShopUseCase;
    private final ToggleFavouriteShopUseCase toggleFavouriteShopUseCase;
    private final GetAllDataFavoriteUseCase getAllDataFavoriteUseCase;
    private final GetFavoriteShopUsecase getFavoriteShopUsecase;
    private DataFavoriteMapper favoriteMapper;
    private PagingHandler pagingHandler;

    @Inject
    FavoritePresenter(GetInitialDataPageUsecase getInitialDataPageUsecase,
                      GetTopAdsShopUseCase getTopAdsShopUseCase,
                      ToggleFavouriteShopUseCase toggleFavouriteShopUseCase,
                      GetAllDataFavoriteUseCase getAllDataFavoriteUseCase,
                      GetFavoriteShopUsecase getFavoriteShopUsecase,
                      DataFavoriteMapper favoriteMapper) {

        this.getInitialDataPageUsecase = getInitialDataPageUsecase;
        this.getTopAdsShopUseCase = getTopAdsShopUseCase;
        this.toggleFavouriteShopUseCase = toggleFavouriteShopUseCase;
        this.getAllDataFavoriteUseCase = getAllDataFavoriteUseCase;
        this.getFavoriteShopUsecase = getFavoriteShopUsecase;
        this.favoriteMapper = favoriteMapper;

        pagingHandler = new PagingHandler();
    }

    @Override
    public void attachView(FavoriteContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        getInitialDataPageUsecase.unsubscribe();
        getTopAdsShopUseCase.unsubscribe();
        getAllDataFavoriteUseCase.unsubscribe();
        getFavoriteShopUsecase.unsubscribe();
        toggleFavouriteShopUseCase.unsubscribe();
    }

    @Override
    public void loadInitialData() {
        getInitialDataPageUsecase.execute(
                RequestParams.EMPTY, new InitialDataSubscriber());
    }

    @Override
    public void addFavoriteShop(View view, TopAdsShopItem shopItem) {
        toggleFavouriteShopUseCase.execute(ToggleFavouriteShopUseCase.createRequestParam(shopItem.shopId), new AddFavoriteShopSubscriber(view, shopItem));
    }

    @Override
    public void refreshAllDataFavoritePage() {
        getAllDataFavoriteUseCase.execute(RequestParams.EMPTY, new RefreshFavoriteSubscriber());
    }

    @Override
    public void loadMoreFavoriteShop() {
        if (pagingHandler.CheckNextPage() && !getView().isLoading()) {
            pagingHandler.nextPage();
            getView().showLoadMoreLoading();
            RequestParams params = GetFavoriteShopUsecase.Companion.getDefaultParams();
            String currentPage = String.valueOf(pagingHandler.getPage());
            params.putString(GetFavoriteShopUsecase.KEY_PAGE, currentPage);
            getFavoriteShopUsecase.execute(params, new LoadMoreSubscriber());
        }
    }

    @Override
    public void onSaveDataBeforeRotate(Bundle outState) {

    }

    @Override
    public void onViewStateRestored(Bundle outState) {

    }

    private void setNextPaging(PagingHandler.PagingHandlerModel pagingModel) {
        if (pagingModel != null) {
            pagingHandler.setHasNext(PagingHandler.CheckHasNext(pagingModel));
        } else {
            pagingHandler.setHasNext(false);
            getView().stopLoadingFavoriteShop();
        }
    }

    private void validateFavoriteShopErrorNetwork(DataFavorite dataFavorite) {
        if (dataFavorite.favoriteShop.isNetworkError()) {
            getView().showFavoriteShopFailedMessage();
        } else {
            getView().dismissFavoriteShopFailedMessage();
        }

    }

    private void validateNetworkTopAdsShop(TopAdsShop topAdsShop) {
        if (topAdsShop != null) {
            if (topAdsShop.isNetworkError()) {
                getView().showTopadsShopFailedMessage();
            } else {
                getView().dismissTopadsShopFailedMessage();
            }
            getView().validateMessageError();
        }
    }

    private void addFavoriteShop(DataFavorite dataFavorite, List<Visitable<?>> dataFavoriteItemList) {
        if (dataFavorite != null
                && dataFavorite.favoriteShop != null) {

            validateFavoriteShopErrorNetwork(dataFavorite);
            if (dataFavorite.favoriteShop.data != null) {
                if (dataFavorite.favoriteShop.pagingModel != null) {
                    setNextPaging(dataFavorite.favoriteShop.pagingModel);
                }

                if (dataFavorite.favoriteShop.data.size() > 0) {

                    for (FavoriteShopItem favoriteShopItem
                            : dataFavorite.favoriteShop.data) {

                        favoriteShopItem.setIsFav(true);
                        dataFavoriteItemList.add(
                                favoriteMapper.prepareDataFavoriteShop(favoriteShopItem));
                    }
                }
            }

        }
    }

    private void addTopAdsShop(DataFavorite dataFavorite, List<Visitable<?>> dataFavoriteItemList) {
        if (dataFavorite != null) {
            validateNetworkTopAdsShop(dataFavorite.topAdsShop);
            if (dataFavorite.topAdsShop != null
                    && dataFavorite.topAdsShop.topAdsShopItemList != null
                    && dataFavorite.topAdsShop.topAdsShopItemList.size() > 0) {

                dataFavoriteItemList
                        .add(favoriteMapper
                                .prepareDataTopAdsShop(dataFavorite.topAdsShop));
            }
        }
    }

    private void returnPagingHandlerToPreviousPage() {
        final int firstPage = 1;
        int currentPage = pagingHandler.getPage();
        if (currentPage <= firstPage) {
            pagingHandler.resetPage();
        } else {
            pagingHandler.setPage(currentPage - 1);
        }
    }

    boolean hasNextPage() {
        return pagingHandler.CheckNextPage();
    }

    private class InitialDataSubscriber extends Subscriber<DataFavorite> {

        @Override
        public void onStart() {
            super.onStart();
            getView().showRefreshLoading();
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: ", e);
            if (isViewAttached()) {
                getView().hideRefreshLoading();
                getView().showErrorLoadData();
            }
        }

        @Override
        public void onNext(DataFavorite dataFavorite) {
            getView().hideRefreshLoading();
            getView().showInitialDataPage(getDataFavoriteViewModel(dataFavorite));
            getView().validateMessageError();
            getView().stopTracePerformanceMonitoring();
        }

        @NonNull
        private List<Visitable<?>> getDataFavoriteViewModel(DataFavorite dataFavorite) {
            List<Visitable<?>> elementList = new ArrayList<>();
            addTopAdsShop(dataFavorite, elementList);
            addFavoriteShop(dataFavorite, elementList);

            return elementList;
        }
    }

    private class RefreshFavoriteSubscriber extends Subscriber<DataFavorite> {
        @Override
        public void onStart() {
            super.onStart();
            getView().showRefreshLoading();
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: ", e);
            getView().hideRefreshLoading();
            getView().showErrorLoadData();
        }

        @Override
        public void onNext(DataFavorite dataFavorite) {
            List<Visitable<?>> dataFavoriteItemList = new ArrayList<>();
            addTopAdsShop(dataFavorite, dataFavoriteItemList);
            addFavoriteShop(dataFavorite, dataFavoriteItemList);
            getView().refreshDataFavorite(dataFavoriteItemList);
            getView().hideRefreshLoading();
            getView().validateMessageError();
            pagingHandler.resetPage();
        }
    }

    private class LoadMoreSubscriber extends Subscriber<FavoriteShop> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            returnPagingHandlerToPreviousPage();
            getView().showErrorLoadMore();
        }

        @Override
        public void onNext(FavoriteShop favoriteShop) {
            if (favoriteShop.isDataValid()) {
                setNextPaging(favoriteShop.pagingModel);
                List<Visitable<?>> elementList = favoriteMapper.prepareListFavoriteShop(favoriteShop);
                getView().showMoreDataFavoriteShop(elementList);
            } else {
                setNextPaging(favoriteShop.pagingModel);
            }
        }

    }

    private class AddFavoriteShopSubscriber extends Subscriber<Boolean> {

        private final View view;
        private TopAdsShopItem shopItem;

        AddFavoriteShopSubscriber(View view, TopAdsShopItem shopItem) {
            this.view = view;
            this.shopItem = shopItem;
        }

        @Override
        public void onStart() {
            view.setEnabled(false);
        }

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "onError: ");
            getView().showErrorAddFavoriteShop();
        }

        @Override
        public void onNext(Boolean isValid) {
            view.clearAnimation();
            if (isValid) {
                FavoriteShopViewModel favoriteShopViewModel = new FavoriteShopViewModel();
                favoriteShopViewModel.shopId = shopItem.shopId;
                favoriteShopViewModel.shopName = shopItem.shopName;
                favoriteShopViewModel.shopAvatarImageUrl = shopItem.shopImageUrl;
                favoriteShopViewModel.shopLocation = shopItem.shopLocation;
                favoriteShopViewModel.setFavoriteShop(shopItem.isFav());
                getView().addFavoriteShop(favoriteShopViewModel);
                getView().sendFavoriteShopImpression(shopItem.shopClickUrl);
            }
        }
    }
}
