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
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductLimitedListPresenter extends BaseDaggerPresenter<ShopProductListView> {

    private final GetShopProductFeaturedWithAttributeUseCase getShopProductFeaturedWithAttributeUseCase;
    private final GetShopProductListWithAttributeUseCase productListWithAttributeUseCase;
    private final GetShopEtalaseUseCase getShopEtalaseUseCase;
    private final AddWishListUseCase addWishListUseCase;
    private final RemoveWishListUseCase removeWishListUseCase;
    private final UserSession userSession;
    private WishListActionListener wishListActionListener;

    @Inject
    public ShopProductLimitedListPresenter(GetShopProductListWithAttributeUseCase productListWithAttributeUseCase,
                                           GetShopProductFeaturedWithAttributeUseCase getShopProductFeaturedWithAttributeUseCase,
                                           GetShopEtalaseUseCase getShopEtalaseUseCase,
                                           AddWishListUseCase addWishListUseCase,
                                           RemoveWishListUseCase removeWishListUseCase,
                                           UserSession userSession) {
        this.getShopProductFeaturedWithAttributeUseCase = getShopProductFeaturedWithAttributeUseCase;
        this.productListWithAttributeUseCase = productListWithAttributeUseCase;
        this.getShopEtalaseUseCase = getShopEtalaseUseCase;
        this.addWishListUseCase = addWishListUseCase;
        this.removeWishListUseCase = removeWishListUseCase;
        this.userSession = userSession;
    }

    public void attachView(ShopProductListView view, WishListActionListener wishlistListener) {
        this.wishListActionListener = wishlistListener;
        super.attachView(view);
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
                                             String etalaseId,
                                             final int etalaseLimit) {
        ShopProductRequestModel shopProductRequestModel = new ShopProductRequestModel(shopId, isShopClosed,
                isOfficialStore, page, true, itemPerPage);

        shopProductRequestModel.setEtalaseId(etalaseId);

        List<ShopEtalaseViewModel> shopEtalaseViewModelList = getView().getShopEtalaseViewModelList();
        if (shopEtalaseViewModelList.size() > 0) {
            if (!TextUtils.isEmpty(etalaseId)) {
                boolean isUseAce = isUseAce(shopEtalaseViewModelList, etalaseId);
                shopProductRequestModel.setUseAce(isUseAce);
            }
            getProductListWithAttributes(shopProductRequestModel);
        } else {
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
                        List<ShopEtalaseViewModel> shopEtalaseViewModelList =
                                ShopProductMapper.mergeEtalaseList(pagingListOther, null, etalaseLimit);
                        getView().onSuccessGetEtalaseList(shopEtalaseViewModelList);
                        if (!TextUtils.isEmpty(etalaseId)) {
                            boolean isUseAce = isUseAce(shopEtalaseViewModelList, etalaseId);
                            shopProductRequestModel.setUseAce(isUseAce);
                        }
                        getProductListWithAttributes(shopProductRequestModel);
                    }
                }
            });
        }
    }

    private boolean isUseAce(List<ShopEtalaseViewModel> etalaseViewModelList, String selectedEtalaseId){
        if (etalaseViewModelList!= null) {
            for (ShopEtalaseViewModel shopEtalaseViewModel : etalaseViewModelList) {
                if (shopEtalaseViewModel.getEtalaseId().equalsIgnoreCase(selectedEtalaseId)) {
                    return shopEtalaseViewModel.isUseAce();
                }
            }
        }
        return true;
    }

    private void getProductListWithAttributes(ShopProductRequestModel shopProductRequestModel) {
        productListWithAttributeUseCase.unsubscribe();
        productListWithAttributeUseCase.execute(
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
        addWishListUseCase.createObservable(productId, userSession.getUserId(), wishListActionListener);

    }

    public void removeFromWishList(final String productId) {
        if (!userSession.isLoggedIn() && isViewAttached()) {
            getView().onErrorAddToWishList(new UserNotLoginException());
            return;
        }
        removeWishListUseCase.createObservable(productId, userSession.getUserId(), wishListActionListener);

    }

    public void getShopEtalase(String shopId, final int limit) {
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
                            ShopProductMapper.mergeEtalaseList(pagingListOther, null, limit));
                }
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        productListWithAttributeUseCase.unsubscribe();
        getShopProductFeaturedWithAttributeUseCase.unsubscribe();
        getShopEtalaseUseCase.unsubscribe();
        addWishListUseCase.unsubscribe();
        removeWishListUseCase.unsubscribe();
    }
}