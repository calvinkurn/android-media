package com.tokopedia.shop.product.view.presenter;

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
import com.tokopedia.shop.product.util.ShopProductOfficialStoreUtils;
import com.tokopedia.shop.product.view.listener.ShopProductListLimitedView;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;
import com.tokopedia.shop.product.view.model.ShopProductHomeViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedEtalaseTitleViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedPromoViewModel;
import com.tokopedia.shop.product.view.model.ShopProductMoreViewModel;
import com.tokopedia.shop.product.view.model.ShopProductTitleFeaturedViewModel;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopProductListLimitedPresenter extends BaseDaggerPresenter<ShopProductListLimitedView> {

    private final GetShopProductLimitedUseCase getShopProductLimitedUseCase;
    private final AddWishListUseCase addWishListUseCase;
    private final RemoveWishListUseCase removeWishListUseCase;
    private final UserSession userSession;

    private static final String TAG = "ShopProductListLimitedP";
    private static final int FIRST_LOAD = 1;
    private WishListActionListener wishListActionListener;

    @Inject
    public ShopProductListLimitedPresenter(GetShopProductLimitedUseCase getShopProductLimitedUseCase,
                                           AddWishListUseCase addWishListUseCase,
                                           RemoveWishListUseCase removeWishListUseCase,
                                           UserSession userSession) {
        this.getShopProductLimitedUseCase = getShopProductLimitedUseCase;
        this.addWishListUseCase = addWishListUseCase;
        this.removeWishListUseCase = removeWishListUseCase;
        this.userSession = userSession;
    }

    public void attachView(ShopProductListLimitedView view, WishListActionListener wishlistListener) {
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

    public void getProductLimitedList(String shopId, boolean goldMerchantStore, boolean officialStore, final String promotionWebViewUrl, final int page) {
        getShopProductLimitedUseCase.execute(GetShopProductLimitedUseCase.createRequestParam(shopId, goldMerchantStore, officialStore, page),
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
                        boolean hasNextPage;
                        if(GlobalConfig.isSellerApp()){
                            hasNextPage = false;
                        }else{
                            hasNextPage = PagingListUtils.checkNextPage(shopProductBaseViewModelList);
                        }
                        if (page == FIRST_LOAD) {
                            boolean shopHasProduct = shopProductBaseViewModelList.getList().size() > 0;
                            if (!TextApiUtils.isTextEmpty(promotionWebViewUrl)) {
                                shopProductBaseViewModelList.getList().add(0, getProductPromoModel());
                            }
                            if (shopHasProduct) {
                                for (int i = 0; i < shopProductBaseViewModelList.getList().size(); i++) {
                                    ShopProductBaseViewModel shopProductBaseViewModel = shopProductBaseViewModelList.getList().get(i);
                                    if (shopProductBaseViewModel instanceof ShopProductHomeViewModel) {
                                        shopProductBaseViewModelList.getList().add(i, new ShopProductLimitedEtalaseTitleViewModel());
                                        break;
                                    }
                                }
                                for (int i = 0; i < shopProductBaseViewModelList.getList().size(); i++) {
                                    ShopProductBaseViewModel shopProductBaseViewModel = shopProductBaseViewModelList.getList().get(i);
                                    if (shopProductBaseViewModel instanceof ShopProductLimitedFeaturedViewModel) {
                                        shopProductBaseViewModelList.getList().add(i, new ShopProductTitleFeaturedViewModel());
                                        break;
                                    }
                                }

                                if(GlobalConfig.isSellerApp() && shopProductBaseViewModelList.getList().size() >= ShopPageTrackingConstant.DEFAULT_PER_PAGE){
                                    shopProductBaseViewModelList.getList().add(new ShopProductMoreViewModel());
                                }
                            }
                            getView().renderList(shopProductBaseViewModelList.getList(), hasNextPage, shopHasProduct);
                        } else {
                            getView().renderList(shopProductBaseViewModelList.getList(), hasNextPage);
                        }
                    }

                    private ShopProductLimitedPromoViewModel getProductPromoModel() {
                        ShopProductLimitedPromoViewModel shopProductLimitedPromoViewModel = new ShopProductLimitedPromoViewModel();
                        shopProductLimitedPromoViewModel.setUserId(userSession.getUserId());
                        shopProductLimitedPromoViewModel.setLogin(userSession.isLoggedIn());
                        String url = promotionWebViewUrl;
                        if (userSession.isLoggedIn()) {
                            url = ShopProductOfficialStoreUtils.getLogInUrl(url, userSession.getDeviceId(), userSession.getUserId());
                        }
                        CommonUtils.dumper(url);
                        shopProductLimitedPromoViewModel.setUrl(url);
                        return shopProductLimitedPromoViewModel;
                    }
                });
    }

    public void addToWishList(final String productId) {
        if (!userSession.isLoggedIn() && isViewAttached()) {
            getView().onUserNotLoginError(new UserNotLoginException());
            return;
        }
        addWishListUseCase.createObservable(productId, userSession.getUserId(), wishListActionListener);
    }

    public void removeFromWishList(final String productId) {
        if (!userSession.isLoggedIn() && isViewAttached()) {
            getView().onUserNotLoginError(new UserNotLoginException());
            return;
        }
        removeWishListUseCase.createObservable(productId, userSession.getUserId(), wishListActionListener);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (getShopProductLimitedUseCase != null) {
            getShopProductLimitedUseCase.unsubscribe();
        }
        if (removeWishListUseCase != null) {
            removeWishListUseCase.unsubscribe();
        }
        if (addWishListUseCase != null) {
            addWishListUseCase.unsubscribe();
        }
    }
}