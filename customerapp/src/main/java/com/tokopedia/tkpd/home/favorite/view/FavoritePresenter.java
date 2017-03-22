package com.tokopedia.tkpd.home.favorite.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetAllDataFavoriteUseCase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetFavoriteAndWishlistUsecase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetFavoriteShopUsecase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetTopAdsShopUseCase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.PostFavoriteShopUseCase;
import com.tokopedia.tkpd.home.favorite.domain.model.DataFavorite;
import com.tokopedia.tkpd.home.favorite.domain.model.DataWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;
import com.tokopedia.tkpd.home.favorite.domain.model.FavShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShop;
import com.tokopedia.tkpd.home.favorite.domain.model.FavoriteShopItem;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;
import com.tokopedia.tkpd.home.favorite.domain.model.WishListBadge;
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

    private final GetFavoriteAndWishlistUsecase favoriteAndWishlistUsecase;
    private GetTopAdsShopUseCase getTopAdsShopUseCase;
    private PostFavoriteShopUseCase postFavoriteShopUseCase;
    private GetAllDataFavoriteUseCase getAllDataFavoriteUseCase;
    private PagingHandler pagingHandler;
    private GetFavoriteShopUsecase getFavoriteShopUsecase;

    @Inject
    public FavoritePresenter(GetFavoriteAndWishlistUsecase favoriteAndWishlistUsecase,
                             GetTopAdsShopUseCase getTopAdsShopUseCase,
                             PostFavoriteShopUseCase postFavoriteShopUseCase,
                             GetAllDataFavoriteUseCase getAllDataFavoriteUseCase,
                             GetFavoriteShopUsecase favoriteShopUsecase) {

        this.favoriteAndWishlistUsecase = favoriteAndWishlistUsecase;
        this.getTopAdsShopUseCase = getTopAdsShopUseCase;
        this.postFavoriteShopUseCase = postFavoriteShopUseCase;
        this.getAllDataFavoriteUseCase = getAllDataFavoriteUseCase;
        getFavoriteShopUsecase = favoriteShopUsecase;
        pagingHandler = new PagingHandler();
    }

    @Override
    public void attachView(FavoriteContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        favoriteAndWishlistUsecase.unsubscribe();
    }

    @Override
    public void loadDataWishlistAndFavorite() {

        favoriteAndWishlistUsecase.execute(
                RequestParams.EMPTY,new FavoriteAndWishlistSubscriber());
    }

    @Override
    public void loadDataTopAdsShop() {
        getTopAdsShopUseCase.execute(
                GetTopAdsShopUseCase.getDefaultParams(), new TopAdsShopSubscriber());
    }

    @Override
    public void setFavoriteShop(View view, TopAdsShopItem shopItem) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PostFavoriteShopUseCase.KEY_SHOP_ITEM, shopItem);
        postFavoriteShopUseCase.execute(requestParams, new SetFavoriteSubscriber(view));
    }

    @Override
    public void loadOnRefresh() {
        getAllDataFavoriteUseCase.execute(RequestParams.EMPTY, new DataFavoriteSubscriber());
    }

    @Override
    public void loadOnMore() {
        if (pagingHandler.CheckNextPage() && !getView().isLoading()) {
            pagingHandler.nextPage();
            getView().showLoading();
            RequestParams params = GetFavoriteShopUsecase.getDefaultParams();
            params.putString(
                    GetFavoriteShopUsecase.KEY_PAGE,String.valueOf(pagingHandler.getPage()));

            getFavoriteShopUsecase.execute(params, new LoadMoreSubscriber());
        }
    }

    @Override
    public void loadOnRetry() {

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

    private class FavoriteAndWishlistSubscriber extends Subscriber<DataFavorite> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: ", e);
            getView().showErrorLoadData();
        }

        @Override
        public void onNext(DataFavorite dataFavorite) {
            List<Visitable> elementList = new ArrayList<>();
            if (dataFavorite != null && dataFavorite.getWishListData() != null) {
                elementList.add(prepareDataWishlist(dataFavorite.getWishListData()));
            }
            if (dataFavorite != null
                    && dataFavorite.getFavoriteShop() != null
                    && dataFavorite.getFavoriteShop().getData() != null) {

                setPagingHandler(dataFavorite.getFavoriteShop().getPagingModel());

                for (FavoriteShopItem favoriteShopItem : dataFavorite.getFavoriteShop().getData()) {
                    favoriteShopItem.setIsFav(true);
                    elementList.add(prepareDataFavoriteShop(favoriteShopItem));
                }
            }

            getView().addAllDataFavorite(elementList, true);
        }
    }

    private void setPagingHandler(PagingHandler.PagingHandlerModel pagingModel) {
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
        wishlistViewModel.setWishlistItems(wishlistItems);
        return wishlistViewModel;
    }

    private class SetFavoriteSubscriber extends Subscriber<FavShop> {

        private final View view;

        public SetFavoriteSubscriber(View view) {
            this.view = view;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(FavShop favShop) {
            view.clearAnimation();
            if (favShop.ismIsValid()) {
                FavoriteShopItem favoriteShop = favShop.getFavoriteShopItem();
                FavoriteShopViewModel favoriteShopViewModel = new FavoriteShopViewModel();
                favoriteShopViewModel.setShopId(favoriteShop.getId());
                favoriteShopViewModel.setShopName(favoriteShop.getName());
                favoriteShopViewModel.setShopAvatarImageUrl(favoriteShop.getIconUri());
                favoriteShopViewModel.setShopLocation(favoriteShop.getLocation());
                favoriteShopViewModel.setFavoriteShop(favoriteShop.isFav());
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
        }

        @Override
        public void onNext(TopAdsShop topAdsShop) {
            if (topAdsShop.getTopAdsShopItemList() != null
                    && topAdsShop.getTopAdsShopItemList().size() > 0) {
                getView().addTopAdsShop(prepareDataTopAdsShop(topAdsShop));
            }
        }
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

    private class DataFavoriteSubscriber extends Subscriber<DataFavorite> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            getView().showErrorLoadData();
        }

        @Override
        public void onNext(DataFavorite dataFavorite) {
            List<Visitable> elementList = new ArrayList<>();
            if (dataFavorite != null && dataFavorite.getWishListData() != null) {
                elementList.add(prepareDataWishlist(dataFavorite.getWishListData()));
            }
            if (dataFavorite != null && dataFavorite.getTopAdsShop() != null) {
                elementList.add(prepareDataTopAdsShop(dataFavorite.getTopAdsShop()));
            }
            if (dataFavorite != null
                    && dataFavorite.getFavoriteShop() != null
                    && dataFavorite.getFavoriteShop().getData() != null) {

                for (FavoriteShopItem favoriteShopItem : dataFavorite.getFavoriteShop().getData()) {
                    favoriteShopItem.setIsFav(true);
                    elementList.add(prepareDataFavoriteShop(favoriteShopItem));
                }
            }
            getView().addAllDataFavorite(elementList, true);
            getView().setRefreshing(false);
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
                List<Visitable> elementList = new ArrayList<>();
                setPagingHandler(favoriteShop.getPagingModel());
                for (FavoriteShopItem favoriteShopItem : favoriteShop.getData()) {
                    favoriteShopItem.setIsFav(true);
                    elementList.add(prepareDataFavoriteShop(favoriteShopItem));
                }
                getView().addAllDataFavorite(elementList, false);
            }
        }
    }
}
