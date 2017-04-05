package com.tokopedia.tkpd.home.favorite.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.tkpd.home.favorite.domain.interactor.AddFavoriteShopUseCase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetAllDataFavoriteUseCase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetFavoriteAndWishlistUsecase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetFavoriteShopUsecase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetTopAdsShopUseCase;
import com.tokopedia.tkpd.home.favorite.domain.model.DataFavorite;
import com.tokopedia.tkpd.home.favorite.domain.model.DataWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.FavShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShopItem;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;
import com.tokopedia.tkpd.home.favorite.domain.model.WishListBadge;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.EmptyWishlistViewModel;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.FavoriteShopViewModel;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.TopAdsShopItem;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.TopAdsShopViewModel;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.WishlistItem;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.WishlistViewModel;

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

    private final GetFavoriteAndWishlistUsecase getFavoriteAndWishlistUsecase;
    private final GetTopAdsShopUseCase getTopAdsShopUseCase;
    private final AddFavoriteShopUseCase addFavoriteShopUseCase;
    private final GetAllDataFavoriteUseCase getAllDataFavoriteUseCase;
    private final GetFavoriteShopUsecase getFavoriteShopUsecase;
    private PagingHandler pagingHandler;

    @Inject
    FavoritePresenter(GetFavoriteAndWishlistUsecase getFavoriteAndWishlistUsecase,
                      GetTopAdsShopUseCase getTopAdsShopUseCase,
                      AddFavoriteShopUseCase addFavoriteShopUseCase,
                      GetAllDataFavoriteUseCase getAllDataFavoriteUseCase,
                      GetFavoriteShopUsecase getFavoriteShopUsecase) {

        this.getFavoriteAndWishlistUsecase = getFavoriteAndWishlistUsecase;
        this.getTopAdsShopUseCase = getTopAdsShopUseCase;
        this.addFavoriteShopUseCase = addFavoriteShopUseCase;
        this.getAllDataFavoriteUseCase = getAllDataFavoriteUseCase;
        this.getFavoriteShopUsecase = getFavoriteShopUsecase;

        pagingHandler = new PagingHandler();
    }

    @Override
    public void attachView(FavoriteContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        getFavoriteAndWishlistUsecase.unsubscribe();
        getTopAdsShopUseCase.unsubscribe();
        getAllDataFavoriteUseCase.unsubscribe();
        getFavoriteShopUsecase.unsubscribe();
        addFavoriteShopUseCase.unsubscribe();
    }

    @Override
    public void loadDataWishlistAndFavorite() {
        getFavoriteAndWishlistUsecase.execute(
                RequestParams.EMPTY, new FavoriteAndWishlistSubscriber());
    }

    @Override
    public void loadDataTopAdsShop() {
        RequestParams defaultParams = GetTopAdsShopUseCase.defaultParams();
        defaultParams.putBoolean(GetTopAdsShopUseCase.KEY_IS_FORCE_REFRESH, false);
        getTopAdsShopUseCase.execute(defaultParams, new TopAdsShopSubscriber());
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
        getAllDataFavoriteUseCase.execute(RequestParams.EMPTY, new DataFavoriteSubscriber());
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
        pagingHandler.onSavedInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle outState) {
        if (outState != null) {
            pagingHandler.onCreate(outState);
        }
    }

    private void setNextPaging(PagingHandler.PagingHandlerModel pagingModel) {
        pagingHandler.setHasNext(PagingHandler.CheckHasNext(pagingModel));
    }

    private FavoriteShopViewModel prepareDataFavoriteShop(FavoriteShopItem favoriteShop) {
        FavoriteShopViewModel favoriteShopViewModel = new FavoriteShopViewModel();
        favoriteShopViewModel.setShopId(favoriteShop.getId());
        favoriteShopViewModel.setShopName(favoriteShop.getName());
        favoriteShopViewModel.setShopAvatarImageUrl(favoriteShop.getIconUri());
        favoriteShopViewModel.setShopLocation(favoriteShop.getLocation());
        favoriteShopViewModel.setFavoriteShop(favoriteShop.isFav());
        return favoriteShopViewModel;
    }

    private WishlistViewModel prepareDataWishlist(DomainWishlist wishListData) {
        WishlistViewModel wishlistViewModel = new WishlistViewModel();
        ArrayList<WishlistItem> wishlistItems = new ArrayList<>();
        if (wishListData != null && wishListData.getData() != null) {
            for (DataWishlist dataWishlist : wishListData.getData()) {
                WishlistItem wishlistItem = new WishlistItem();
                wishlistItem.setName(dataWishlist.getName());
                wishlistItem.setPrice(dataWishlist.getPrice());
                wishlistItem.setShopName(dataWishlist.getShop_name());
                wishlistItem.setProductId(dataWishlist.getId());
                wishlistItem.setProductImage(dataWishlist.getProductImageUrl());
                ArrayList<String> badges = new ArrayList<>();
                for (WishListBadge badge : dataWishlist.getBadges()) {
                    badges.add(badge.getImgUrl());
                }
                wishlistItem.setBadgeImageUrl(badges);
                wishlistItems.add(wishlistItem);
            }
        }
        wishlistViewModel.setWishlistItems(wishlistItems);
        return wishlistViewModel;
    }

    private TopAdsShopViewModel prepareDataTopAdsShop(TopAdsShop adsShop) {
        TopAdsShopViewModel shopViewModel = new TopAdsShopViewModel();
        ArrayList<TopAdsShopItem> shopItems = new ArrayList<>();
        for (com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShopItem item
                : adsShop.getTopAdsShopItemList()) {
            TopAdsShopItem shopItem = new TopAdsShopItem();
            shopItem.setShopId(item.getShopId());
            shopItem.setShopDomain(item.getShopDomain());
            shopItem.setShopName(item.getShopName());
            shopItem.setAdKey(item.getAdRefKey());
            shopItem.setShopClickUrl(item.getShopClickUrl());
            shopItem.setShopCoverUrl(item.getShopImageCover());
            shopItem.setShopImageUrl(item.getShopImageUrl());
            shopItem.setShopLocation(item.getShopLocation());
            shopItems.add(shopItem);
        }
        shopViewModel.setAdsShopItems(shopItems);
        return shopViewModel;
    }

    private class FavoriteAndWishlistSubscriber extends Subscriber<DataFavorite> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: ", e);
            if (isViewAttached()) {
                getView().showErrorLoadData();
            }
        }

        @Override
        public void onNext(DataFavorite dataFavorite) {
            getView().showWishlistFavorite(getDataFavoriteViewModel(dataFavorite));
        }

        @NonNull
        private List<Visitable> getDataFavoriteViewModel(DataFavorite dataFavorite) {
            List<Visitable> elementList = new ArrayList<>();

            if (dataFavorite != null
                    && dataFavorite.getWishListData() != null) {

                if (dataFavorite.getWishListData().isNetworkError()) {
                    getView().showWishlistFailedMessage();
                } else {
                    getView().dismissWishlistFailedMessage();
                }

                if (dataFavorite.getWishListData().getData() != null) {
                    if (dataFavorite.getWishListData().getData().size() > 0) {
                        elementList.add(prepareDataWishlist(dataFavorite.getWishListData()));
                    } else {
                        elementList.add(new EmptyWishlistViewModel());
                    }
                }
            }

            if (dataFavorite != null
                    && dataFavorite.getFavoriteShop() != null) {

                if (dataFavorite.getFavoriteShop().isNetworkError()) {
                    getView().showFavoriteShopFailedMessage();
                } else {
                    getView().dismissFavoriteShopFailedMessage();
                }
                if (dataFavorite.getFavoriteShop().getData() != null) {
                    setNextPaging(dataFavorite.getFavoriteShop().getPagingModel());
                    if (dataFavorite.getFavoriteShop().getData().size() > 0) {
                        for (FavoriteShopItem favoriteShopItem
                                : dataFavorite.getFavoriteShop().getData()) {

                            favoriteShopItem.setIsFav(true);
                            elementList.add(prepareDataFavoriteShop(favoriteShopItem));
                        }
                    }
                }

            }
            return elementList;
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
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: ", e);
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


    private class TopAdsShopSubscriber extends Subscriber<TopAdsShop> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: ", e);
            getView().showTopAdsProductError();
        }

        @Override
        public void onNext(TopAdsShop topAdsShop) {
            if (topAdsShop != null) {
                if (topAdsShop.isNetworkError()) {
                    getView().showTopadsShopFailedMessage();
                } else {
                    getView().dismissTopadsShopFailedMessage();
                }
                getView().validateMessageError();
            }
            if (isTopAdsShopListNotEmpty(topAdsShop)) {
                getView().addTopAdsShop(prepareDataTopAdsShop(topAdsShop));
            }
        }

        private boolean isTopAdsShopListNotEmpty(TopAdsShop topAdsShop) {
            return topAdsShop.getTopAdsShopItemList() != null
                    && topAdsShop.getTopAdsShopItemList().size() > 0;
        }
    }

    private class DataFavoriteSubscriber extends Subscriber<DataFavorite> {
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

            if (dataFavorite != null
                    && dataFavorite.getWishListData() != null) {

                if (dataFavorite.getWishListData().isNetworkError()) {
                    getView().showWishlistFailedMessage();
                } else {
                    getView().dismissWishlistFailedMessage();
                }

                if (dataFavorite.getWishListData().getData() != null) {
                    if (dataFavorite.getWishListData().getData().size() > 0) {
                        dataFavoriteItemList.add(
                                prepareDataWishlist(dataFavorite.getWishListData()));
                    } else {
                        dataFavoriteItemList.add(new EmptyWishlistViewModel());
                    }
                }
            }

//            if (dataFavorite != null && dataFavorite.getWishListData() != null
//                    && dataFavorite.getWishListData().getData() != null) {
//
//                dataFavoriteItemList.add(prepareDataWishlist(dataFavorite.getWishListData()));
//            }

//            if (dataFavorite != null
//                    && dataFavorite.getTopAdsShop() != null
//                    && dataFavorite.getFavoriteShop().getData() != null) {
//                dataFavoriteItemList.add(prepareDataTopAdsShop(dataFavorite.getTopAdsShop()));
//            }

            if (dataFavorite != null && dataFavorite.getTopAdsShop() != null) {
                if (dataFavorite.getTopAdsShop().isNetworkError()) {
                    getView().showTopadsShopFailedMessage();
                } else {
                    getView().dismissTopadsShopFailedMessage();
                }
                getView().validateMessageError();

                if (dataFavorite.getTopAdsShop().getTopAdsShopItemList() != null) {
                    if (dataFavorite.getTopAdsShop().getTopAdsShopItemList().size() > 0) {

                        dataFavoriteItemList
                                .add(prepareDataTopAdsShop(dataFavorite.getTopAdsShop()));
                    }
                }
            }


//            if (dataFavorite != null
//                    && dataFavorite.getFavoriteShop() != null
//                    && dataFavorite.getFavoriteShop().getData() != null) {
//
//                setNextPaging(dataFavorite.getFavoriteShop().getPagingModel());
//
//                for (FavoriteShopItem favoriteShopItem : dataFavorite.getFavoriteShop().getData()) {
//                    favoriteShopItem.setIsFav(true);
//                    dataFavoriteItemList.add(prepareDataFavoriteShop(favoriteShopItem));
//                }
//            }

            if (dataFavorite != null
                    && dataFavorite.getFavoriteShop() != null) {

                if (dataFavorite.getFavoriteShop().isNetworkError()) {
                    getView().showFavoriteShopFailedMessage();
                } else {
                    getView().dismissFavoriteShopFailedMessage();
                }
                if (dataFavorite.getFavoriteShop().getData() != null) {
                    setNextPaging(dataFavorite.getFavoriteShop().getPagingModel());
                    if (dataFavorite.getFavoriteShop().getData().size() > 0) {
                        for (FavoriteShopItem favoriteShopItem
                                : dataFavorite.getFavoriteShop().getData()) {

                            favoriteShopItem.setIsFav(true);
                            dataFavoriteItemList.add(prepareDataFavoriteShop(favoriteShopItem));
                        }
                    }
                }

            }
            getView().refreshDataFavorite(dataFavoriteItemList);
            getView().hideRefreshLoading();
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
            getView().showErrorLoadMore();
        }

        @Override
        public void onNext(FavoriteShop favoriteShop) {
            if (favoriteShop.isDataValid()) {
                setNextPaging(favoriteShop.getPagingModel());

                List<Visitable> elementList = prepareListFavoriteShop(favoriteShop);
                getView().showMoreDataFavoriteShop(elementList);
            }
        }

        @NonNull
        private List<Visitable> prepareListFavoriteShop(FavoriteShop favoriteShop) {
            List<Visitable> elementList = new ArrayList<>();
            for (FavoriteShopItem favoriteShopItem : favoriteShop.getData()) {
                favoriteShopItem.setIsFav(true);
                elementList.add(prepareDataFavoriteShop(favoriteShopItem));
            }
            return elementList;
        }
    }
}
