package com.tokopedia.tkpd.home.favorite.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.tkpd.home.favorite.domain.interactor.AddFavoriteShopUseCase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetAllDataFavoriteUseCase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetInitialDataPageUsecase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetFavoriteShopUsecase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetTopAdsShopUseCase;
import com.tokopedia.tkpd.home.favorite.domain.model.DataFavorite;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.FavShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShopItem;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.DataFavoriteMapper;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.EmptyWishlistViewModel;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.FavoriteShopViewModel;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.TopAdsShopItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author Kulomady on 1/20/17.
 */

public class FavoritePresenter
        extends BaseDaggerPresenter<FavoriteContract.View> implements FavoriteContract.Presenter {

    private static final String TAG = "FavoritePresenter";

    private final GetInitialDataPageUsecase getInitialDataPageUsecase;
    private final GetTopAdsShopUseCase getTopAdsShopUseCase;
    private final AddFavoriteShopUseCase addFavoriteShopUseCase;
    private final GetAllDataFavoriteUseCase getAllDataFavoriteUseCase;
    private final GetFavoriteShopUsecase getFavoriteShopUsecase;
    private DataFavoriteMapper favoriteMapper;
    private PagingHandler pagingHandler;

    @Inject
    FavoritePresenter(GetInitialDataPageUsecase getInitialDataPageUsecase,
                      GetTopAdsShopUseCase getTopAdsShopUseCase,
                      AddFavoriteShopUseCase addFavoriteShopUseCase,
                      GetAllDataFavoriteUseCase getAllDataFavoriteUseCase,
                      GetFavoriteShopUsecase getFavoriteShopUsecase,
                      DataFavoriteMapper favoriteMapper) {

        this.getInitialDataPageUsecase = getInitialDataPageUsecase;
        this.getTopAdsShopUseCase = getTopAdsShopUseCase;
        this.addFavoriteShopUseCase = addFavoriteShopUseCase;
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
        addFavoriteShopUseCase.unsubscribe();
    }

    @Override
    public void loadInitialData() {
        getInitialDataPageUsecase.execute(
                RequestParams.EMPTY, new InitialDataSubscriber());
    }

    @Override
    public void addFavoriteShop(View view, TopAdsShopItem shopItem) {
        RequestParams params = RequestParams.create();
        params.putString(AddFavoriteShopUseCase.KEY_AD, shopItem.getAdKey());
        params.putString(AddFavoriteShopUseCase.KEY_SHOP_ID, shopItem.getShopId());
        params.putString(AddFavoriteShopUseCase.KEY_SRC, AddFavoriteShopUseCase.DEFAULT_VALUE_SRC);
        addFavoriteShopUseCase.execute(params, new AddFavoriteShopSubscriber(view, shopItem));
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
            RequestParams params = GetFavoriteShopUsecase.getDefaultParams();
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
        pagingHandler.setHasNext(PagingHandler.CheckHasNext(pagingModel));
    }

    private void validateFavoriteShopErrorNetwork(DataFavorite dataFavorite) {
        if (dataFavorite.getFavoriteShop().isNetworkError()) {
            getView().showFavoriteShopFailedMessage();
        } else {
            getView().dismissFavoriteShopFailedMessage();
        }

    }

    private void validateWishlistErrorNetwork(DomainWishlist domainWishlist) {
        if (domainWishlist.isNetworkError()) {
            getView().showWishlistFailedMessage();
        } else {
            getView().dismissWishlistFailedMessage();
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

    private void addFavoriteShop(DataFavorite dataFavorite, List<Visitable> dataFavoriteItemList) {
        if (dataFavorite != null
                && dataFavorite.getFavoriteShop() != null) {

            validateFavoriteShopErrorNetwork(dataFavorite);
            if (dataFavorite.getFavoriteShop().getData() != null) {
                if (dataFavorite.getFavoriteShop().getPagingModel() != null) {
                    setNextPaging(dataFavorite.getFavoriteShop().getPagingModel());
                }

                if (dataFavorite.getFavoriteShop().getData().size() > 0) {

                    for (FavoriteShopItem favoriteShopItem
                            : dataFavorite.getFavoriteShop().getData()) {

                        favoriteShopItem.setIsFav(true);
                        dataFavoriteItemList.add(
                                favoriteMapper.prepareDataFavoriteShop(favoriteShopItem));
                    }
                }
            }

        }
    }

    private void addTopAdsShop(DataFavorite dataFavorite, List<Visitable> dataFavoriteItemList) {
        if (dataFavorite != null) {
            validateNetworkTopAdsShop(dataFavorite.getTopAdsShop());
            if (dataFavorite.getTopAdsShop() != null
                    && dataFavorite.getTopAdsShop().getTopAdsShopItemList() != null
                    && dataFavorite.getTopAdsShop().getTopAdsShopItemList().size() > 0) {

                dataFavoriteItemList
                        .add(favoriteMapper
                                .prepareDataTopAdsShop(dataFavorite.getTopAdsShop()));
            }
        }
    }

    private void addWishlist(DataFavorite dataFavorite, List<Visitable> dataFavoriteItemList) {
        if (dataFavorite != null) {
            if (dataFavorite.getWishListData() != null) {
                validateWishlistErrorNetwork(dataFavorite.getWishListData());
                dataFavoriteItemList.add(
                        favoriteMapper.prepareDataWishlist(dataFavorite.getWishListData()));
            } else {
                dataFavoriteItemList.add(new EmptyWishlistViewModel());
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
        }

        @NonNull
        private List<Visitable> getDataFavoriteViewModel(DataFavorite dataFavorite) {
            List<Visitable> elementList = new ArrayList<>();
            addWishlist(dataFavorite, elementList);
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
            List<Visitable> dataFavoriteItemList = new ArrayList<>();
            addWishlist(dataFavorite, dataFavoriteItemList);
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
                setNextPaging(favoriteShop.getPagingModel());
                List<Visitable> elementList = favoriteMapper.prepareListFavoriteShop(favoriteShop);
                getView().showMoreDataFavoriteShop(elementList);
            }
        }

    }

    private class AddFavoriteShopSubscriber extends Subscriber<FavShop> {

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
            Log.e(TAG, "onError: ", e);
            getView().showErrorAddFavoriteShop();
        }

        @Override
        public void onNext(FavShop favShop) {
            view.clearAnimation();
            if (favShop.isValid()) {
                FavoriteShopViewModel favoriteShopViewModel = new FavoriteShopViewModel();
                favoriteShopViewModel.setShopId(shopItem.getShopId());
                favoriteShopViewModel.setShopName(shopItem.getShopName());
                favoriteShopViewModel.setShopAvatarImageUrl(shopItem.getShopImageUrl());
                favoriteShopViewModel.setShopLocation(shopItem.getShopLocation());
                favoriteShopViewModel.setFavoriteShop(shopItem.isFav());
                getView().addFavoriteShop(favoriteShopViewModel);
            }
        }
    }
}
