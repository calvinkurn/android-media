package com.tokopedia.shop.product.view.presenter.newpresenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.shop.analytic.ShopPageTrackingConstant;
import com.tokopedia.shop.common.util.PagingListUtils;
import com.tokopedia.shop.common.util.TextApiUtils;
import com.tokopedia.shop.product.domain.interactor.GetShopProductLimitedUseCase;
import com.tokopedia.shop.product.domain.interactor.GetShopProductListWithAttributeNewUseCase;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils;
import com.tokopedia.shop.product.view.listener.newlistener.ShopProductListView;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;
import com.tokopedia.shop.product.view.model.ShopProductHomeViewModelOld;
import com.tokopedia.shop.product.view.model.ShopProductLimitedEtalaseTitleViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModelOld;
import com.tokopedia.shop.product.view.model.ShopProductLimitedPromoViewModel;
import com.tokopedia.shop.product.view.model.ShopProductMoreViewModel;
import com.tokopedia.shop.product.view.model.ShopProductTitleFeaturedViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductPromoViewModel;
import com.tokopedia.shop.product.view.model.newmodel.ShopProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.wishlist.common.domain.interactor.AddToWishListUseCase;
import com.tokopedia.wishlist.common.domain.interactor.RemoveFromWishListUseCase;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductListNewPresenter extends BaseDaggerPresenter<ShopProductListView> {

//    private final GetShopProductLimitedUseCase getShopProductLimitedUseCase;
    private final GetShopProductListWithAttributeNewUseCase productListWithAttributeNewUseCase;
    private final AddToWishListUseCase addToWishListUseCase;
    private final RemoveFromWishListUseCase removeFromWishListUseCase;
    private final UserSession userSession;

    @Inject
    public ShopProductListNewPresenter(GetShopProductListWithAttributeNewUseCase productListWithAttributeNewUseCase,
                                       AddToWishListUseCase addToWishListUseCase,
                                       RemoveFromWishListUseCase removeFromWishListUseCase,
                                       UserSession userSession) {
        this.productListWithAttributeNewUseCase = productListWithAttributeNewUseCase;
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
                                             boolean isOfficialStore, int page, boolean useAce,
                                             int itemPerPage){
        ShopProductRequestModel shopProductRequestModel = new ShopProductRequestModel(shopId, isShopClosed,
                isOfficialStore, page, useAce, itemPerPage);
        //TODO etalaseid, look up here ShopProductListPresenterOld
        productListWithAttributeNewUseCase.execute(
                GetShopProductListWithAttributeNewUseCase.createRequestParam(shopProductRequestModel),
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
/*
    public void getProductList(String shopId, boolean goldMerchantStore, boolean officialStore, final int page, boolean isShopClosed) {
        getShopProductLimitedUseCase.execute(GetShopProductLimitedUseCase.createRequestParam(shopId, goldMerchantStore, officialStore, page, isShopClosed),
                new Subscriber<PagingList<ShopProductBaseViewModel>>() {
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
                    public void onNext(PagingList<ShopProductBaseViewModel> shopProductBaseViewModelList) {

                        getView().showGetListError(new RuntimeException());

//                        boolean hasNextPage;
////                        if(GlobalConfig.isSellerApp()){
////                            hasNextPage = false;
////                        }else{
//                        hasNextPage = PagingListUtils.checkNextPage(shopProductBaseViewModelList);
////                        }
//                        if (page == FIRST_LOAD) {
//                            boolean shopHasProduct = shopProductBaseViewModelList.getList().size() > 0;
////                            ShopProductPromoViewModel shopProductPromoViewModel = getProductPromoModel(officialWebViewUrl);
////                            if (shopProductPromoViewModel!=null){
////                                shopProductBaseViewModelList.getList().add(0, shopProductPromoViewModel);
////                            }
//                            if (shopHasProduct) {
//                                for(int i = 0; i < shopProductBaseViewModelList.getList().size(); i++){
//                                    ShopProductBaseViewModel shopProductBaseViewModel = shopProductBaseViewModelList.getList().get(i);
//                                    if(shopProductBaseViewModel instanceof ShopProductHomeViewModelOld) {
//                                        shopProductBaseViewModelList.getList().add(i, new ShopProductLimitedEtalaseTitleViewModel());
//                                        break;
//                                    }
//                                }
//                                for(int i = 0; i < shopProductBaseViewModelList.getList().size(); i++){
//                                    ShopProductBaseViewModel shopProductBaseViewModel = shopProductBaseViewModelList.getList().get(i);
//                                    if(shopProductBaseViewModel instanceof ShopProductLimitedFeaturedViewModelOld) {
//                                        shopProductBaseViewModelList.getList().add(i, new ShopProductTitleFeaturedViewModel());
//                                        break;
//                                    }
//                                }
//
//                                if(GlobalConfig.isSellerApp() && shopProductBaseViewModelList.getList().size() >= ShopPageTrackingConstant.DEFAULT_PER_PAGE){
//                                    shopProductBaseViewModelList.getList().add(new ShopProductMoreViewModel());
//                                }
//                            }
//                            //TODO
//                            hasNextPage = false;
//                            getView().renderProductList(new ArrayList<>(), hasNextPage);
//                        } else {
//                            //TODO
//                            hasNextPage = false;
//                            getView().renderProductList(new ArrayList<>(), hasNextPage);
//                        }
                    }


                     // Use Presenter.getProductPromoModel() instead.

//                    @Deprecated
//                    private ShopProductLimitedPromoViewModel getProductPromoModel() {
//                        ShopProductLimitedPromoViewModel shopProductLimitedPromoViewModel = new ShopProductLimitedPromoViewModel();
//                        shopProductLimitedPromoViewModel.setUserId(userSession.getUserId());
//                        shopProductLimitedPromoViewModel.setLogin(userSession.isLoggedIn());
//                        String url = promotionWebViewUrl;
//                        if (userSession.isLoggedIn()) {
//                            url = ShopProductOfficialStoreUtils.getLogInUrl(url, userSession.getDeviceId(), userSession.getUserId());
//                        }
//                        CommonUtils.dumper(url);
//                        shopProductLimitedPromoViewModel.setUrl(url);
//                        return shopProductLimitedPromoViewModel;
//                    }
                });
    }
*/

    public void loadProductPromoModel(String promotionWebViewUrl){
        if (!TextApiUtils.isTextEmpty(promotionWebViewUrl)) {
            ShopProductPromoViewModel shopProductPromoViewModel = new ShopProductPromoViewModel();
            shopProductPromoViewModel.setUserId(userSession.getUserId());
            shopProductPromoViewModel.setLogin(userSession.isLoggedIn());
            String url = promotionWebViewUrl;
            if (userSession.isLoggedIn()) {
                url = ShopProductOfficialStoreUtils.getLogInUrl(url, userSession.getDeviceId(), userSession.getUserId());
            }
            shopProductPromoViewModel.setUrl(url);
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

    @Override
    public void detachView() {
        super.detachView();
        if (productListWithAttributeNewUseCase != null) {
            productListWithAttributeNewUseCase.unsubscribe();
        }
    }
}