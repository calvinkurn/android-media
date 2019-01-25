package com.tokopedia.checkout.view.feature.emptycart;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.checkout.domain.datamodel.recentview.RecentView;
import com.tokopedia.checkout.domain.usecase.CancelAutoApplyCouponUseCase;
import com.tokopedia.checkout.domain.usecase.GetCartListUseCase;
import com.tokopedia.checkout.domain.usecase.GetRecentViewUseCase;
import com.tokopedia.checkout.view.feature.emptycart.subscriber.CancelAutoApplySubscriber;
import com.tokopedia.checkout.view.feature.emptycart.subscriber.GetCartListSubscriber;
import com.tokopedia.checkout.view.feature.emptycart.subscriber.GetRecentViewSubscriber;
import com.tokopedia.checkout.view.feature.emptycart.subscriber.GetWishlistSubscriber;
import com.tokopedia.checkout.view.feature.emptycart.viewmodel.RecentViewViewModel;
import com.tokopedia.checkout.view.feature.emptycart.viewmodel.WishlistViewModel;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feed.ProductFeedViewModel;
import com.tokopedia.transactionanalytics.data.emptycart.EnhancedECommerceEmptyCartActionFieldData;
import com.tokopedia.transactionanalytics.data.emptycart.EnhancedECommerceEmptyCartClickData;
import com.tokopedia.transactionanalytics.data.emptycart.EnhancedECommerceEmptyCartData;
import com.tokopedia.transactionanalytics.data.emptycart.EnhancedECommerceEmptyCartProductData;
import com.tokopedia.transactiondata.utils.CartApiRequestParamGenerator;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist;
import com.tokopedia.wishlist.common.usecase.GetWishlistUseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Irfan Khoirul on 14/09/18.
 */

public class EmptyCartPresenter extends BaseDaggerPresenter<EmptyCartContract.View>
        implements EmptyCartContract.Presenter {

    private static final int LIST_SIZE = 2;
    private static final int EMPTY_CART_API_COUNT = 3;

    private final GetCartListUseCase getCartListUseCase;
    private final GetWishlistUseCase getWishlistUseCase;
    private final GetRecentViewUseCase getRecentViewUseCase;
    private final CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase;
    private final CartApiRequestParamGenerator cartApiRequestParamGenerator;
    private final CompositeSubscription compositeSubscription;
    private final UserSessionInterface userSessionInterface;
    private List<WishlistViewModel> wishlistViewModels = new ArrayList<>();
    private List<RecentViewViewModel> recentViewViewModels = new ArrayList<>();
    private List<Product> recommendationViewModels = new ArrayList<>();
    private Map<Integer, Boolean> loadApiStatusMap = new HashMap<>();

    @Inject
    public EmptyCartPresenter(GetCartListUseCase getCartListUseCase,
                              GetWishlistUseCase getWishlistUseCase,
                              GetRecentViewUseCase getRecentViewUseCase,
                              CancelAutoApplyCouponUseCase cancelAutoApplyCouponUseCase,
                              CartApiRequestParamGenerator cartApiRequestParamGenerator,
                              CompositeSubscription compositeSubscription,
                              UserSessionInterface userSessionInterface) {
        this.getCartListUseCase = getCartListUseCase;
        this.getWishlistUseCase = getWishlistUseCase;
        this.getRecentViewUseCase = getRecentViewUseCase;
        this.cancelAutoApplyCouponUseCase = cancelAutoApplyCouponUseCase;
        this.cartApiRequestParamGenerator = cartApiRequestParamGenerator;
        this.compositeSubscription = compositeSubscription;
        this.userSessionInterface = userSessionInterface;
    }

    @Override
    public void setLoadApiStatus(@EmptyCartApi int key, boolean status) {
        loadApiStatusMap.put(key, status);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeSubscription.unsubscribe();
        getWishlistUseCase.unsubscribe();
        getRecentViewUseCase.unsubscribe();
    }

    @Override
    public void processInitialGetCartData() {
        getView().showLoading();
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(
                GetCartListUseCase.PARAM_REQUEST_AUTH_MAP_STRING,
                getView().getGeneratedAuthParamNetwork(cartApiRequestParamGenerator.generateParamMapGetCartList(null))
        );
        compositeSubscription.add(getCartListUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new GetCartListSubscriber(getView(), this))
        );
    }

    @Override
    public void processGetWishlistData() {
        getWishlistUseCase.createObservable(new GetWishlistSubscriber(getView(), this));
    }

    @Override
    public void processGetRecentViewData(int userId) {
        getRecentViewUseCase.createObservable(userId, new GetRecentViewSubscriber(getView(), this));
    }

    @Override
    public void processCancelAutoApply() {
        Map<String, String> authParam = AuthUtil.generateParamsNetwork(
                userSessionInterface.getUserId(), userSessionInterface.getDeviceId(), new TKPDMapParam<>());

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CancelAutoApplyCouponUseCase.PARAM_REQUEST_AUTH_MAP_STRING, authParam);

        compositeSubscription.add(cancelAutoApplyCouponUseCase.createObservable(RequestParams.create())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new CancelAutoApplySubscriber(getView()))
        );

    }

    @Override
    public void setWishListViewModels(List<Wishlist> wishLists) {
        wishlistViewModels.clear();
        for (int i = 0; i < wishLists.size(); i++) {
            if (i < LIST_SIZE) {
                WishlistViewModel wishlistViewModel = new WishlistViewModel();
                wishlistViewModel.setWishlist(wishLists.get(i));
                wishlistViewModels.add(wishlistViewModel);
            }
        }
    }

    @Override
    public List<WishlistViewModel> getWishlistViewModels() {
        return wishlistViewModels;
    }

    @Override
    public void setRecentViewListModels(List<RecentView> recentViewList) {
        recentViewViewModels.clear();
        for (int i = 0; i < recentViewList.size(); i++) {
            if (i < LIST_SIZE) {
                RecentViewViewModel recentViewViewModel = new RecentViewViewModel();
                recentViewViewModel.setRecentView(recentViewList.get(i));
                recentViewViewModels.add(recentViewViewModel);
            }
        }
    }

    @Override
    public List<RecentViewViewModel> getRecentViewListModels() {
        return recentViewViewModels;
    }

    @Override
    public void setRecommendationList(List<Item> list) {
        recommendationViewModels.clear();
        for (Item item : list) {
            if (item instanceof ProductFeedViewModel) {
                ProductFeedViewModel productFeedViewModel = (ProductFeedViewModel) item;
                if (productFeedViewModel.getData() != null && productFeedViewModel.getData().getProduct() != null) {
                    recommendationViewModels.add(productFeedViewModel.getData().getProduct());
                }
            }
        }
    }

    @Override
    public Map<String, Object> generateEmptyCartAnalyticProductClickDataLayer(Wishlist wishlist, int index) {
        EnhancedECommerceEmptyCartProductData enhancedECommerceEmptyCartProductData =
                new EnhancedECommerceEmptyCartProductData();
        enhancedECommerceEmptyCartProductData.setBrand(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceEmptyCartProductData.setCategory(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceEmptyCartProductData.setPosition(String.valueOf(index));
        enhancedECommerceEmptyCartProductData.setPrice(String.valueOf(wishlist.getPrice()));
        enhancedECommerceEmptyCartProductData.setProductID(wishlist.getId());
        enhancedECommerceEmptyCartProductData.setProductName(wishlist.getName());
        enhancedECommerceEmptyCartProductData.setVariant(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
        List<Map<String, Object>> productsData = new ArrayList<>();
        productsData.add(enhancedECommerceEmptyCartProductData.getProduct());

        EnhancedECommerceEmptyCartData enhancedECommerceEmptyCart = getEnhancedECommerceEmptyCartData(
                productsData, EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_WISHLIST);

        return enhancedECommerceEmptyCart.getData();
    }

    @Override
    public Map<String, Object> generateEmptyCartAnalyticProductClickDataLayer(RecentView recentView, int index) {
        EnhancedECommerceEmptyCartProductData enhancedECommerceEmptyCartProductData =
                new EnhancedECommerceEmptyCartProductData();
        enhancedECommerceEmptyCartProductData.setBrand(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceEmptyCartProductData.setCategory(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceEmptyCartProductData.setPosition(String.valueOf(index));
        enhancedECommerceEmptyCartProductData.setPrice(String.valueOf(recentView.getProductPrice()
                .replace("Rp", "")
                .replace(".", "")
                .replace(" ", "")));
        enhancedECommerceEmptyCartProductData.setProductID(recentView.getProductId());
        enhancedECommerceEmptyCartProductData.setProductName(recentView.getProductName());
        enhancedECommerceEmptyCartProductData.setVariant(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
        List<Map<String, Object>> productsData = new ArrayList<>();
        productsData.add(enhancedECommerceEmptyCartProductData.getProduct());

        EnhancedECommerceEmptyCartData enhancedECommerceEmptyCart = getEnhancedECommerceEmptyCartData(
                productsData, EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW);

        return enhancedECommerceEmptyCart.getData();
    }

    @Override
    public Map<String, Object> generateEmptyCartAnalyticProductClickDataLayer(Product product, int index) {
        EnhancedECommerceEmptyCartProductData enhancedECommerceEmptyCartProductData =
                new EnhancedECommerceEmptyCartProductData();
        enhancedECommerceEmptyCartProductData.setBrand(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceEmptyCartProductData.setCategory(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
        enhancedECommerceEmptyCartProductData.setPosition(String.valueOf(index));
        enhancedECommerceEmptyCartProductData.setPrice(String.valueOf(product.getPriceFormat()
                .replace("Rp", "")
                .replace(".", "")
                .replace(" ", "")));
        enhancedECommerceEmptyCartProductData.setProductID(product.getId());
        enhancedECommerceEmptyCartProductData.setProductName(product.getName());
        enhancedECommerceEmptyCartProductData.setVariant(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
        List<Map<String, Object>> productsData = new ArrayList<>();
        productsData.add(enhancedECommerceEmptyCartProductData.getProduct());

        EnhancedECommerceEmptyCartData enhancedECommerceEmptyCart = getEnhancedECommerceEmptyCartData(
                productsData, EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_RECOMMENDATION);

        return enhancedECommerceEmptyCart.getData();
    }

    @NonNull
    private EnhancedECommerceEmptyCartData getEnhancedECommerceEmptyCartData(List<Map<String, Object>> productsData, String valueSectionName) {
        EnhancedECommerceEmptyCartActionFieldData enhancedECommerceEmptyCartActionFieldData =
                new EnhancedECommerceEmptyCartActionFieldData();
        enhancedECommerceEmptyCartActionFieldData.setList(valueSectionName);

        EnhancedECommerceEmptyCartClickData enhancedECommerceEmptyCartClickData =
                new EnhancedECommerceEmptyCartClickData();
        enhancedECommerceEmptyCartClickData.setActionField(enhancedECommerceEmptyCartActionFieldData.getData());
        enhancedECommerceEmptyCartClickData.setProducts(productsData);

        EnhancedECommerceEmptyCartData enhancedECommerceEmptyCart = new EnhancedECommerceEmptyCartData();
        enhancedECommerceEmptyCart.setClickData(enhancedECommerceEmptyCartClickData.getData());
        return enhancedECommerceEmptyCart;
    }

    @Override
    public Map<String, Object> generateEmptyCartAnalyticViewProductWishlistDataLayer() {
        List<Map<String, Object>> productsData = new ArrayList<>();
        for (int i = 0; i < wishlistViewModels.size(); i++) {
            EnhancedECommerceEmptyCartProductData enhancedECommerceEmptyCartProductData =
                    new EnhancedECommerceEmptyCartProductData();
            enhancedECommerceEmptyCartProductData.setBrand(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
            enhancedECommerceEmptyCartProductData.setCategory(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
            enhancedECommerceEmptyCartProductData.setPosition(String.valueOf(i + 1));
            enhancedECommerceEmptyCartProductData.setPrice(String.valueOf(wishlistViewModels.get(i).getWishlist().getPrice()));
            enhancedECommerceEmptyCartProductData.setProductID(wishlistViewModels.get(i).getWishlist().getId());
            enhancedECommerceEmptyCartProductData.setProductName(wishlistViewModels.get(i).getWishlist().getName());
            enhancedECommerceEmptyCartProductData.setVariant(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
            enhancedECommerceEmptyCartProductData.setList(EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_WISHLIST);
            productsData.add(enhancedECommerceEmptyCartProductData.getProduct());
        }

        EnhancedECommerceEmptyCartData enhancedECommerceEmptyCart = new EnhancedECommerceEmptyCartData();
        enhancedECommerceEmptyCart.setImpressionData(productsData);

        return enhancedECommerceEmptyCart.getData();
    }

    @Override
    public Map<String, Object> generateEmptyCartAnalyticViewProductRecentViewDataLayer() {
        List<Map<String, Object>> productsData = new ArrayList<>();
        for (int i = 0; i < recentViewViewModels.size(); i++) {
            EnhancedECommerceEmptyCartProductData enhancedECommerceEmptyCartProductData =
                    new EnhancedECommerceEmptyCartProductData();
            enhancedECommerceEmptyCartProductData.setBrand(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
            enhancedECommerceEmptyCartProductData.setCategory(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
            enhancedECommerceEmptyCartProductData.setPosition(String.valueOf(i + 1));
            enhancedECommerceEmptyCartProductData.setPrice(String.valueOf(recentViewViewModels.get(i).getRecentView().getProductPrice()));
            enhancedECommerceEmptyCartProductData.setProductID(recentViewViewModels.get(i).getRecentView().getProductId());
            enhancedECommerceEmptyCartProductData.setProductName(recentViewViewModels.get(i).getRecentView().getProductName());
            enhancedECommerceEmptyCartProductData.setVariant(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
            enhancedECommerceEmptyCartProductData.setList(EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_RECENT_VIEW);
            productsData.add(enhancedECommerceEmptyCartProductData.getProduct());
        }

        EnhancedECommerceEmptyCartData enhancedECommerceEmptyCart = new EnhancedECommerceEmptyCartData();
        enhancedECommerceEmptyCart.setImpressionData(productsData);

        return enhancedECommerceEmptyCart.getData();
    }

    @Override
    public Map<String, Object> generateEmptyCartAnalyticViewProductRecommendationDataLayer() {
        List<Map<String, Object>> productsData = new ArrayList<>();
        for (int i = 0; i < recommendationViewModels.size(); i++) {
            EnhancedECommerceEmptyCartProductData enhancedECommerceEmptyCartProductData =
                    new EnhancedECommerceEmptyCartProductData();
            enhancedECommerceEmptyCartProductData.setBrand(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
            enhancedECommerceEmptyCartProductData.setCategory(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
            enhancedECommerceEmptyCartProductData.setPosition(String.valueOf(i + 1));
            enhancedECommerceEmptyCartProductData.setPrice(String.valueOf(recommendationViewModels.get(i).getPriceFormat()
                    .replace("Rp", "")
                    .replace(".", "")
                    .replace(" ", "")));
            enhancedECommerceEmptyCartProductData.setProductID(recommendationViewModels.get(i).getId());
            enhancedECommerceEmptyCartProductData.setProductName(recommendationViewModels.get(i).getName());
            enhancedECommerceEmptyCartProductData.setVariant(EnhancedECommerceEmptyCartProductData.DEFAULT_VALUE_NONE_OTHER);
            enhancedECommerceEmptyCartProductData.setList(EnhancedECommerceEmptyCartActionFieldData.VALUE_SECTION_NAME_RECOMMENDATION);
            productsData.add(enhancedECommerceEmptyCartProductData.getProduct());
        }

        EnhancedECommerceEmptyCartData enhancedECommerceEmptyCart = new EnhancedECommerceEmptyCartData();
        enhancedECommerceEmptyCart.setImpressionData(productsData);

        return enhancedECommerceEmptyCart.getData();
    }

    @Override
    public boolean hasLoadAllApi() {
        int loadedApi = 0;
        for (Map.Entry<Integer, Boolean> entry : loadApiStatusMap.entrySet()) {
            if (entry.getValue()) {
                loadedApi++;
            }
        }

        return loadedApi == EMPTY_CART_API_COUNT;
    }
}
