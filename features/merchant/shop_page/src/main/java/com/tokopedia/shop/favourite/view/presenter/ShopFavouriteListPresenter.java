package com.tokopedia.shop.favourite.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.favourite.data.source.cloud.model.ShopFavouritePagingList;
import com.tokopedia.shop.favourite.data.source.cloud.model.ShopFavouriteUser;
import com.tokopedia.shop.favourite.domain.interactor.GetShopFavouriteUserUseCase;
import com.tokopedia.shop.favourite.domain.model.ShopFavouriteRequestModel;
import com.tokopedia.shop.favourite.view.listener.ShopFavouriteListView;
import com.tokopedia.shop.favourite.view.mapper.ShopFavouriteViewModelMapper;
import com.tokopedia.shop.page.domain.interactor.DeleteFavoriteListCacheUseCase;
import com.tokopedia.shop.page.domain.interactor.ToggleFavouriteShopAndDeleteCacheUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopFavouriteListPresenter extends BaseDaggerPresenter<ShopFavouriteListView> {

    private static final int SHOP_FAVOURITE_PER_PAGE = 12;

    private final GetShopFavouriteUserUseCase getShopFavouriteUserUseCase;
    private final ToggleFavouriteShopAndDeleteCacheUseCase toggleFavouriteShopAndDeleteCacheUseCase;
    private final ShopFavouriteViewModelMapper shopFavouriteViewModelMapper;
    private final GetShopInfoUseCase getShopInfoUseCase;
    private final UserSession userSession;
    private final DeleteFavoriteListCacheUseCase deleteFavoriteListCacheUseCase;

    @Inject
    public ShopFavouriteListPresenter(GetShopFavouriteUserUseCase getShopFavouriteUserUseCase,
                                      ShopFavouriteViewModelMapper shopFavouriteViewModelMapper,
                                      GetShopInfoUseCase getShopInfoUseCase, UserSession userSession,
                                      ToggleFavouriteShopAndDeleteCacheUseCase toggleFavouriteShopAndDeleteCacheUseCase,
                                      DeleteFavoriteListCacheUseCase deleteFavoriteListCacheUseCase) {
        this.getShopFavouriteUserUseCase = getShopFavouriteUserUseCase;
        this.toggleFavouriteShopAndDeleteCacheUseCase = toggleFavouriteShopAndDeleteCacheUseCase;
        this.shopFavouriteViewModelMapper = shopFavouriteViewModelMapper;
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.userSession = userSession;
        this.deleteFavoriteListCacheUseCase = deleteFavoriteListCacheUseCase;
    }

    public void getShopFavouriteList(String shopId, int page) {
        ShopFavouriteRequestModel shopFavouriteRequestModel = new ShopFavouriteRequestModel();
        shopFavouriteRequestModel.setShopId(shopId);
        shopFavouriteRequestModel.setUserId(userSession.getUserId());
        shopFavouriteRequestModel.setDeviceId(userSession.getDeviceId());
        shopFavouriteRequestModel.setPage(page);
        shopFavouriteRequestModel.setPerPage(SHOP_FAVOURITE_PER_PAGE);
        getShopFavouriteUserUseCase.execute(GetShopFavouriteUserUseCase.createRequestParam(shopFavouriteRequestModel), new Subscriber<ShopFavouritePagingList<ShopFavouriteUser>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(ShopFavouritePagingList<ShopFavouriteUser> shopFavouriteUserPagingList) {
                getView().renderList(shopFavouriteViewModelMapper.transform(shopFavouriteUserPagingList.getList()),
                        shopFavouriteUserPagingList.getPage() < shopFavouriteUserPagingList.getTotalPage());
            }
        });
    }

    public void clearFavoriteListCache(){
        deleteFavoriteListCacheUseCase.executeSync();
    }

    public void toggleFavouriteShop(String shopId) {
        if (!userSession.isLoggedIn()) {
            getView().onErrorToggleFavourite(new UserNotLoginException());
            return;
        }
        toggleFavouriteShopAndDeleteCacheUseCase.execute(
                ToggleFavouriteShopAndDeleteCacheUseCase.createRequestParam(shopId), new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().onErrorToggleFavourite(e);
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        getView().onSuccessToggleFavourite(aBoolean);
                    }
                });
    }

    public boolean isMyShop(String shopId) {
        return userSession.getShopId().equals(shopId);
    }

    public boolean isLoggedIn() {
        return userSession.isLoggedIn();
    }

    public void getShopInfo(String shopId) {
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(shopId), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                getView().onSuccessGetShopInfo(shopInfo);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopFavouriteUserUseCase.unsubscribe();
        toggleFavouriteShopAndDeleteCacheUseCase.unsubscribe();
        getShopInfoUseCase.unsubscribe();

    }
}