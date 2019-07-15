package com.tokopedia.checkout.view.feature.cartlist;

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.ShopGroupData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartRecentViewItemHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartRecommendationItemHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartShopHolderData;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartWishlistItemHolderData;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel;
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

    void processInitialGetCartData(String cartId, boolean initialLoad, boolean forceInitialLoad);

    void processDeleteCartItem(List<CartItemData> allCartItemData, List<CartItemData> removedCartItems, ArrayList<String> appliedPromocodeList, boolean addWishList);

    void processToUpdateCartData(List<CartItemData> cartItemDataList, List<CartShopHolderData> cartShopHolderDataList);

    void processUpdateCartDataPromoMerchant(List<CartItemData> cartItemDataList, ShopGroupData shopGroupData);

    void processUpdateCartDataPromoStacking(List<CartItemData> cartItemDataList, PromoStackingData promoStackingData, int goToDetail);

    void processToUpdateAndReloadCartData();

    void reCalculateSubTotal(List<CartShopHolderData> dataList);

    void processCheckPromoCodeFromSuggestedPromo(String promoCode, boolean isAutoApply);

    void processResetAndRefreshCartData();

    void processCancelAutoApplyPromoStack(int shopIndex, ArrayList<String> promoCodeList, boolean ignoreAPIResponse);

    void processCancelAutoApplyPromoStackAfterClash(ArrayList<String> oldPromoList, ArrayList<ClashingVoucherOrderUiModel> newPromoList, String type);

    void processApplyPromoStackAfterClash(ArrayList<ClashingVoucherOrderUiModel> newPromoList, String type);

    Map<String, Object> generateCartDataAnalytics(List<CartItemData> cartItemDataList, String enhancedECommerceAction);

    CartListData getCartListData();

    void setCartListData(CartListData cartListData);

    void processAddToWishlist(String productId, String userId, WishListActionListener wishListActionListener);

    void processRemoveFromWishlist(String productId, String userId, WishListActionListener wishListActionListener);

    void setHasPerformChecklistChange();

    boolean dataHasChanged();

    void processGetRecentViewData();

    void processGetWishlistData();

    void processGetRecommendationData(int page);

    void processAddToCart(Object productModel);

    Map<String, Object> generateAddToCartEnhanceEcommerceDataLayer(CartWishlistItemHolderData cartWishlistItemHolderData, AddToCartDataModel addToCartDataResponseModel);

    Map<String, Object> generateAddToCartEnhanceEcommerceDataLayer(CartRecentViewItemHolderData cartRecentViewItemHolderData, AddToCartDataModel addToCartDataResponseModel);

    Map<String, Object> generateAddToCartEnhanceEcommerceDataLayer(CartRecommendationItemHolderData cartRecommendationItemHolderData, AddToCartDataModel addToCartDataResponseModel);

}
