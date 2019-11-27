package com.tokopedia.purchase_platform.features.cart.view;

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecentViewItemHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecommendationItemHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartShopHolderData;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartWishlistItemHolderData;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.request.UpdateInsuranceProductApplicationDetails;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartDigitalProduct;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartShops;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public interface ICartListPresenter {

    void attachView(ICartListView view);

    void detachView();

    void processInitialGetCartData(String cartId, boolean initialLoad, boolean isLoadingTypeRefresh);

    void processDeleteCartItem(List<CartItemData> allCartItemData, List<CartItemData> removedCartItems, ArrayList<String> appliedPromocodeList, boolean addWishList, boolean removeInsurance);

    void processToUpdateCartData(List<CartItemData> cartItemDataList);

    void processUpdateCartDataPromoMerchant(List<CartItemData> cartItemDataList, ShopGroupData shopGroupData);

    void processUpdateCartDataPromoStacking(List<CartItemData> cartItemDataList, PromoStackingData promoStackingData, int goToDetail);

    void processToUpdateAndReloadCartData();

    void reCalculateSubTotal(List<CartShopHolderData> dataList, ArrayList<InsuranceCartShops> insuranceCartShops);

    void processCheckPromoCodeFromSuggestedPromo(String promoCode, boolean isAutoApply);

    void processResetAndRefreshCartData();

    void processCancelAutoApplyPromoStack(int shopIndex, ArrayList<String> promoCodeList, boolean ignoreAPIResponse);

    void processCancelAutoApplyPromoStackAfterClash(ArrayList<String> oldPromoList, ArrayList<ClashingVoucherOrderUiModel> newPromoList, String type);

    void processApplyPromoStackAfterClash(ArrayList<ClashingVoucherOrderUiModel> newPromoList, String type);

    Map<String, Object> generateCartDataAnalytics(List<CartItemData> cartItemDataList, String enhancedECommerceAction);

    Map<String, Object> generateRecommendationDataAnalytics(List<CartRecommendationItemHolderData> cartRecommendationItemHolderDataList, boolean isEmptyCart);

    Map<String, Object> generateRecommendationDataOnClickAnalytics(RecommendationItem recommendationItem, boolean isEmptyCart, int position);

    Map<String, Object> generateRecentViewProductClickDataLayer(CartRecentViewItemHolderData cartRecentViewItemHolderData, int position);

    Map<String, Object> generateRecentViewProductClickEmptyCartDataLayer(CartRecentViewItemHolderData cartRecentViewItemHolderData, int position);

    Map<String, Object> generateWishlistProductClickDataLayer(CartWishlistItemHolderData cartWishlistItemHolderData, int position);

    Map<String, Object> generateWishlistProductClickEmptyCartDataLayer(CartWishlistItemHolderData cartWishlistItemHolderData, int position);

    Map<String, Object> generateWishlistDataImpressionAnalytics(List<CartWishlistItemHolderData> cartWishlistItemHolderDataList, boolean isEmptyCart);

    Map<String, Object> generateRecentViewDataImpressionAnalytics(List<CartRecentViewItemHolderData> cartRecentViewItemHolderDataList, boolean isEmptyCart);

    CartListData getCartListData();

    void setCartListData(CartListData cartListData);

    void processAddToWishlist(String productId, String userId, WishListActionListener wishListActionListener);

    void processRemoveFromWishlist(String productId, String userId, WishListActionListener wishListActionListener);

    void setHasPerformChecklistChange();

    boolean dataHasChanged();

    void processGetRecentViewData();

    void processGetWishlistData();

    void processGetRecommendationData(int page, List<String> allProductIds);

    void processAddToCart(Object productModel);

    Map<String, Object> generateAddToCartEnhanceEcommerceDataLayer(CartWishlistItemHolderData cartWishlistItemHolderData, AddToCartDataModel addToCartDataResponseModel, boolean isCartEmpty);

    Map<String, Object> generateAddToCartEnhanceEcommerceDataLayer(CartRecentViewItemHolderData cartRecentViewItemHolderData, AddToCartDataModel addToCartDataResponseModel, boolean isCartEmpty);

    Map<String, Object> generateAddToCartEnhanceEcommerceDataLayer(CartRecommendationItemHolderData cartRecommendationItemHolderData, AddToCartDataModel addToCartDataResponseModel, boolean isCartEmpty);

    void getInsuranceTechCart();

    void processDeleteCartInsurance(ArrayList<InsuranceCartDigitalProduct> insuranceCartShops, boolean showToaster);

    void updateInsuranceProductData(InsuranceCartShops insuranceCartShops, ArrayList<UpdateInsuranceProductApplicationDetails> list);

    void setAllInsuranceProductsChecked(ArrayList<InsuranceCartShops> insuranceCartShops, boolean isChecked);

    Map<String, Object> generateCheckoutDataAnalytics(List<CartItemData> cartItemDataList, String step);
}
