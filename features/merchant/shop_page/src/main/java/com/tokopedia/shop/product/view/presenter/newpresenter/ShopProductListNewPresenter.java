package com.tokopedia.shop.product.view.presenter.newpresenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.shop.analytic.ShopPageTrackingConstant;
import com.tokopedia.shop.common.util.PagingListUtils;
import com.tokopedia.shop.common.util.TextApiUtils;
import com.tokopedia.shop.etalase.data.source.cloud.model.EtalaseModel;
import com.tokopedia.shop.etalase.data.source.cloud.model.PagingListOther;
import com.tokopedia.shop.etalase.domain.interactor.GetShopEtalaseUseCase;
import com.tokopedia.shop.etalase.domain.model.ShopEtalaseRequestModel;
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel;
import com.tokopedia.shop.product.domain.interactor.GetShopProductFeaturedWithAttributeNewUseCase;
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
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductListNewPresenter extends BaseDaggerPresenter<ShopProductListView> {

    //    private final GetShopProductLimitedUseCase getShopProductLimitedUseCase;
    private final GetShopProductFeaturedWithAttributeNewUseCase getShopProductFeaturedWithAttributeNewUseCase;
    private final GetShopProductListWithAttributeNewUseCase productListWithAttributeNewUseCase;
    private final GetShopEtalaseUseCase getShopEtalaseUseCase;
    private final AddToWishListUseCase addToWishListUseCase;
    private final RemoveFromWishListUseCase removeFromWishListUseCase;
    private final UserSession userSession;

    @Inject
    public ShopProductListNewPresenter(GetShopProductListWithAttributeNewUseCase productListWithAttributeNewUseCase,
                                       GetShopProductFeaturedWithAttributeNewUseCase getShopProductFeaturedWithAttributeNewUseCase,
                                       GetShopEtalaseUseCase getShopEtalaseUseCase,
                                       AddToWishListUseCase addToWishListUseCase,
                                       RemoveFromWishListUseCase removeFromWishListUseCase,
                                       UserSession userSession) {
        this.getShopProductFeaturedWithAttributeNewUseCase = getShopProductFeaturedWithAttributeNewUseCase;
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
                                             boolean isOfficialStore, int page, boolean useAce,
                                             int itemPerPage,
                                             String etalaseId) {
        ShopProductRequestModel shopProductRequestModel = new ShopProductRequestModel(shopId, isShopClosed,
                isOfficialStore, page, useAce, itemPerPage);
        if (!TextUtils.isEmpty(etalaseId)) {
            shopProductRequestModel.setEtalaseId(etalaseId);
        }
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

    public void getProductFeatureListWithAttributes(String shopId, boolean isOfficialStore) {
        getShopProductFeaturedWithAttributeNewUseCase.execute(
                GetShopProductFeaturedWithAttributeNewUseCase.createRequestParam(shopId, isOfficialStore),
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
                    getView().onSuccessGetEtalaseList(mergeListOther(pagingListOther, selectedEtalaseIdList, limit));
                }
            }
        });
    }

    private List<ShopEtalaseViewModel> mergeListOther(PagingListOther<EtalaseModel> pagingListOther,
                                                      final ArrayList<ShopEtalaseViewModel> selectedEtalaseIdList, final int limit) {
        if (pagingListOther.getList() != null && !pagingListOther.getList().isEmpty()) {
            pagingListOther.getListOther().addAll(pagingListOther.getList());
        }
        if (pagingListOther.getListOther().size() == 0) {
            return new ArrayList<>();
        }
        List<ShopEtalaseViewModel> shopEtalaseViewModels = new ArrayList<>();
        // loop to convert to view model, only get until limit.
        for (EtalaseModel etalaseModel : pagingListOther.getListOther()) {
            // add to primary list
            if (shopEtalaseViewModels.size() < limit) {
                ShopEtalaseViewModel model = new ShopEtalaseViewModel(etalaseModel);
                shopEtalaseViewModels.add(model);
            }
        }
        // replace the first with selected id list
        // loop all selected etalase.
        for (int i = selectedEtalaseIdList.size()-1; i >= 0; i--) {
            ShopEtalaseViewModel selectedShopEtalaseViewModel = selectedEtalaseIdList.get(i);
            // loop the shop view model list to check if already in there.
            // if there, continue; otherwise, add new object to list
            boolean isExistInCurrentList = false;
            for (ShopEtalaseViewModel shopEtalaseViewModel : shopEtalaseViewModels) {
                if (shopEtalaseViewModel.getEtalaseId().equalsIgnoreCase(selectedShopEtalaseViewModel.getEtalaseId())) {
                    isExistInCurrentList = true;
                    break;
                }
            }
            if (!isExistInCurrentList) { // add in index 1 and so on
                int indexToReplace = shopEtalaseViewModels.size() >= 1? 1: 0;
                shopEtalaseViewModels.add(indexToReplace, selectedShopEtalaseViewModel);
            }
        }
        return shopEtalaseViewModels;

    }

    @Override
    public void detachView() {
        super.detachView();
        if (productListWithAttributeNewUseCase != null) {
            productListWithAttributeNewUseCase.unsubscribe();
        }
        if (getShopProductFeaturedWithAttributeNewUseCase != null) {
            getShopProductFeaturedWithAttributeNewUseCase.unsubscribe();
        }
    }
}