package com.tokopedia.shop.product.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException;
import com.tokopedia.shop.common.util.PagingListUtils;
import com.tokopedia.shop.etalase.data.source.cloud.model.EtalaseModel;
import com.tokopedia.shop.etalase.data.source.cloud.model.PagingListOther;
import com.tokopedia.shop.etalase.domain.interactor.GetShopEtalaseUseCase;
import com.tokopedia.shop.etalase.domain.model.ShopEtalaseRequestModel;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.domain.interactor.GetShopProductFeaturedWithAttributeUseCase;
import com.tokopedia.shop.product.domain.interactor.GetShopProductListWithAttributeUseCase;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils;
import com.tokopedia.shop.product.view.listener.ShopProductListView;
import com.tokopedia.shop.product.view.mapper.ShopProductMapper;
import com.tokopedia.shop.product.view.model.ShopProductPromoViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.wishlist.common.domain.interactor.AddToWishListUseCase;
import com.tokopedia.wishlist.common.domain.interactor.RemoveFromWishListUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductLimitedListPresenter extends BaseDaggerPresenter<ShopProductListView> {

    //    private final GetShopProductLimitedUseCase getShopProductLimitedUseCase;
    private final GetShopProductFeaturedWithAttributeUseCase getShopProductFeaturedWithAttributeUseCase;
    private final GetShopProductListWithAttributeUseCase productListWithAttributeNewUseCase;
    private final GetShopEtalaseUseCase getShopEtalaseUseCase;
    private final AddToWishListUseCase addToWishListUseCase;
    private final RemoveFromWishListUseCase removeFromWishListUseCase;
    private final UserSession userSession;

    @Inject
    public ShopProductLimitedListPresenter(GetShopProductListWithAttributeUseCase productListWithAttributeNewUseCase,
                                           GetShopProductFeaturedWithAttributeUseCase getShopProductFeaturedWithAttributeUseCase,
                                           GetShopEtalaseUseCase getShopEtalaseUseCase,
                                           AddToWishListUseCase addToWishListUseCase,
                                           RemoveFromWishListUseCase removeFromWishListUseCase,
                                           UserSession userSession) {
        this.getShopProductFeaturedWithAttributeUseCase = getShopProductFeaturedWithAttributeUseCase;
        this.productListWithAttributeNewUseCase = productListWithAttributeNewUseCase;
        this.getShopEtalaseUseCase = getShopEtalaseUseCase;
        this.addToWishListUseCase = addToWishListUseCase;
        this.removeFromWishListUseCase = removeFromWishListUseCase;
        this.userSession = userSession;
    }

    public boolean isMyShop(String shopId) {
        return userSession.getShopId().equals(shopId);
    }

    public boolean isLogin() {
        return userSession.isLoggedIn();
    }

    public String getDeviceId() {
        return userSession.getDeviceId();
    }

    public String getUserId() {
        return userSession.getUserId();
    }

    public void getProductListWithAttributes(String shopId, boolean isShopClosed,
                                             boolean isOfficialStore, int page,
                                             int itemPerPage,
                                             String etalaseId) {
        ShopProductRequestModel shopProductRequestModel = new ShopProductRequestModel(shopId, isShopClosed,
                isOfficialStore, page, true, itemPerPage);
        if (!TextUtils.isEmpty(etalaseId)) {
            shopProductRequestModel.setEtalaseId(etalaseId);
        }
        productListWithAttributeNewUseCase.execute(
                GetShopProductListWithAttributeUseCase.createRequestParam(shopProductRequestModel),
                new Subscriber<PagingList<ShopProductViewModel>>() {
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
                    public void onNext(PagingList<ShopProductViewModel> shopProductViewModelPagingList) {
                        boolean hasNextPage = PagingListUtils.checkNextPage(shopProductViewModelPagingList);
                        getView().renderProductList(shopProductViewModelPagingList.getList(), hasNextPage);
                    }
                });
    }

    public void getProductFeatureListWithAttributes(String shopId, boolean isOfficialStore) {
        getShopProductFeaturedWithAttributeUseCase.execute(
                GetShopProductFeaturedWithAttributeUseCase.createRequestParam(shopId, isOfficialStore),
                new Subscriber<List<ShopProductViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().onErrorGetProductFeature(e);
                        }
                    }

                    @Override
                    public void onNext(List<ShopProductViewModel> shopProductViewModels) {
                        getView().onSuccessGetProductFeature(shopProductViewModels);
                    }
                });
    }

    public void loadProductPromoModel(String promotionWebViewUrl) {
        if (!TextUtils.isEmpty(promotionWebViewUrl)) {
            String url = promotionWebViewUrl;
            if (userSession.isLoggedIn()) {
                url = ShopProductOfficialStoreUtils.getLogInUrl(url, userSession.getDeviceId(), userSession.getUserId());
            }
            ShopProductPromoViewModel shopProductPromoViewModel = new ShopProductPromoViewModel(
                    url,
                    userSession.getUserId(),
                    userSession.getAccessToken(),
                    userSession.isLoggedIn()
            );
            getView().renderShopProductPromo(shopProductPromoViewModel);
        } else {
            getView().renderShopProductPromo(null);
        }
    }

    public void addToWishList(final String productId) {
        if (!userSession.isLoggedIn() && isViewAttached()) {
            getView().onErrorAddToWishList(new UserNotLoginException());
            return;
        }
        RequestParams requestParam = AddToWishListUseCase.createRequestParam(userSession.getUserId(), productId);
        addToWishListUseCase.execute(requestParam, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorAddToWishList(e);
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getView().onSuccessAddToWishList(productId, aBoolean);
            }
        });
    }

    public void removeFromWishList(final String productId) {
        if (!userSession.isLoggedIn() && isViewAttached()) {
            getView().onErrorAddToWishList(new UserNotLoginException());
            return;
        }
        RequestParams requestParam = AddToWishListUseCase.createRequestParam(userSession.getUserId(), productId);
        removeFromWishListUseCase.execute(requestParam, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorRemoveFromWishList(e);
                }
            }

            @Override
            public void onNext(Boolean value) {
                getView().onSuccessRemoveFromWishList(productId, value);
            }
        });
    }

    public void getShopEtalase(String shopId, final ArrayList<ShopEtalaseViewModel> selectedEtalaseIdList, final int limit) {
        ShopEtalaseRequestModel shopEtalaseRequestModel = new ShopEtalaseRequestModel(
                shopId, userSession.getUserId(), userSession.getDeviceId());
        RequestParams params = GetShopEtalaseUseCase.createParams(shopEtalaseRequestModel);
        getShopEtalaseUseCase.execute(params, new Subscriber<PagingListOther<EtalaseModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    getView().onErrorGetEtalaseList(throwable);
                }
            }

            @Override
            public void onNext(PagingListOther<EtalaseModel> pagingListOther) {
                if (isViewAttached()) {
                    getView().onSuccessGetEtalaseList(
                            ShopProductMapper.mergeEtalaseList(pagingListOther, selectedEtalaseIdList, limit));
                }
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        productListWithAttributeNewUseCase.unsubscribe();
        getShopProductFeaturedWithAttributeUseCase.unsubscribe();
        getShopEtalaseUseCase.unsubscribe();
        addToWishListUseCase.unsubscribe();
        removeFromWishListUseCase.unsubscribe();
    }
}