package com.tokopedia.shop.product.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException;
import com.tokopedia.shop.common.constant.ShopPageConstant;
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel;
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase;
import com.tokopedia.shop.common.util.PagingListUtils;
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
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductLimitedListPresenter extends BaseDaggerPresenter<ShopProductListView> {

    // THESE CONSTANSTS COME FROM API.
    private static final String SOLD_ETALASE = "sold";
    private static final String DISCOUNT_ETALASE = "discount";
    private static final int ORDER_BY_LAST_UPDATE = 3;
    private static final int ORDER_BY_MOST_SOLD = 8;

    private final GetShopProductFeaturedWithAttributeUseCase getShopProductFeaturedWithAttributeUseCase;
    private final GetShopProductListWithAttributeUseCase productListWithAttributeUseCase;
    private final GetShopEtalaseByShopUseCase getShopEtalaseByShopUseCase;
    private final AddWishListUseCase addWishListUseCase;
    private final RemoveWishListUseCase removeWishListUseCase;
    private final UserSessionInterface userSession;
    private WishListActionListener wishListActionListener;

    @Inject
    public ShopProductLimitedListPresenter(GetShopProductListWithAttributeUseCase productListWithAttributeUseCase,
                                           GetShopProductFeaturedWithAttributeUseCase getShopProductFeaturedWithAttributeUseCase,
                                           GetShopEtalaseByShopUseCase getShopEtalaseByShopUseCase,
                                           AddWishListUseCase addWishListUseCase,
                                           RemoveWishListUseCase removeWishListUseCase,
                                           UserSessionInterface userSession) {
        this.getShopProductFeaturedWithAttributeUseCase = getShopProductFeaturedWithAttributeUseCase;
        this.productListWithAttributeUseCase = productListWithAttributeUseCase;
        this.getShopEtalaseByShopUseCase = getShopEtalaseByShopUseCase;
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
                                             boolean isUseAce) {
        getProductListWithAttributes(generateShopProductRequestModel(shopId, isShopClosed,
                isOfficialStore, page, itemPerPage, etalaseId, isUseAce));
    }

    private ShopProductRequestModel generateShopProductRequestModel(String shopId, boolean isShopClosed,
                                                                    boolean isOfficialStore, int page,
                                                                    int itemPerPage,
                                                                    String etalaseId,
                                                                    boolean isUseAce) {
        ShopProductRequestModel shopProductRequestModel = new ShopProductRequestModel(shopId, isShopClosed,
                isOfficialStore, page, isUseAce, itemPerPage);
        shopProductRequestModel.setEtalaseId(etalaseId);
        return shopProductRequestModel;
    }

    public void getProductListHighlight(String shopId, boolean isShopClosed,
                                        boolean isOfficialStore,
                                        List<ShopEtalaseViewModel> shopEtalaseViewModelList) {
        List<RequestParams> requestParamList = new ArrayList<>();
        for (ShopEtalaseViewModel shopEtalaseViewModel : shopEtalaseViewModelList) {
            ShopProductRequestModel shopProductRequestModel = generateShopProductRequestModel(shopId, isShopClosed,
                    isOfficialStore, 1, ShopPageConstant.ETALASE_HIGHLIGHT_COUNT,
                    shopEtalaseViewModel.getEtalaseId(), shopEtalaseViewModel.isUseAce());
            if (shopEtalaseViewModel.getEtalaseId().equals(SOLD_ETALASE)) {
                shopProductRequestModel.setOrderBy(ORDER_BY_MOST_SOLD);
            } else if (shopEtalaseViewModel.getEtalaseId().equals(DISCOUNT_ETALASE)){
                shopProductRequestModel.setOrderBy(ORDER_BY_LAST_UPDATE);
            }
            requestParamList.add(GetShopProductListWithAttributeUseCase.createRequestParam(shopProductRequestModel));
        }
        productListWithAttributeUseCase.executeList(requestParamList, new Subscriber<List<List<ShopProductViewModel>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorGetEtalaseHighlight(e);
                }
            }

            @Override
            public void onNext(List<List<ShopProductViewModel>> lists) {
                if (isViewAttached()) {
                    getView().onSuccessGetEtalaseHighlight(lists);
                }
            }
        });
    }

    public void getShopEtalaseListByShop(String shopId, boolean isOwner) {
        RequestParams params = GetShopEtalaseByShopUseCase.createRequestParams(shopId, true, false, isOwner);
        getShopEtalaseByShopUseCase.execute(params, new Subscriber<ArrayList<ShopEtalaseModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorGetEtalaseListByShop(e);
                }
            }

            @Override
            public void onNext(ArrayList<ShopEtalaseModel> shopEtalaseModels) {
                if (isViewAttached()) {
                    ArrayList<ShopEtalaseViewModel> shopEtalaseViewModelList = ShopProductMapper.map(shopEtalaseModels);
                    getView().onSuccessGetEtalaseListByShop(shopEtalaseViewModelList);
                }
            }
        });
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

    public void clearCache(){
        getShopEtalaseByShopUseCase.clearCache();
    }

    @Override
    public void detachView() {
        super.detachView();
        productListWithAttributeUseCase.unsubscribe();
        getShopProductFeaturedWithAttributeUseCase.unsubscribe();
        addWishListUseCase.unsubscribe();
        removeWishListUseCase.unsubscribe();
    }
}