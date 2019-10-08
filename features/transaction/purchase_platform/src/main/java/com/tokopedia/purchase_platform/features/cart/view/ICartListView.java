package com.tokopedia.purchase_platform.features.cart.view;

import android.app.Activity;

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData;
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupData;
import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.RecentView;
import com.tokopedia.purchase_platform.features.cart.domain.model.voucher.PromoCodeCartListData;
import com.tokopedia.purchase_platform.common.base.IBaseView;
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartShopHolderData;
import com.tokopedia.promocheckout.common.data.entity.request.Promo;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartDigitalProduct;
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.InsuranceCartResponse;
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public interface ICartListView extends IBaseView {

    void renderInitialGetCartListDataSuccess(CartListData cartListData);

    void renderErrorInitialGetCartListData(String message);

    void renderToShipmentFormSuccess(Map<String, Object> eeCheckoutData,
                                     List<CartItemData> cartItemDataList,
                                     boolean checkoutProductEligibleForCashOnDelivery,
                                     int condition);

    void renderToAddressChoice();

    void renderErrorToShipmentForm(String message);

    void renderCheckPromoCodeFromSuggestedPromoSuccess(PromoCodeCartListData promoCodeCartListData);

    void disableSwipeRefresh();

    void enableSwipeRefresh();

    List<CartShopHolderData> getAllShopDataList();

    List<CartItemData> getSelectedCartDataList();

    List<CartItemData> getAllCartDataList();

    void renderDetailInfoSubTotal(String qty, String subtotalPrice, boolean selectAllItem);

    void updateCashback(double cashback);

    void showToastMessageRed(String message);

    void showToastMessageGreen(String message);

    void renderLoadGetCartData();

    void renderLoadGetCartDataFinish();

    void onDeleteCartDataSuccess(List<Integer> deletedCartIds);

    Activity getActivity();

    void goToCouponList();

    void goToDetailPromoStacking(PromoStackingData promoStackingData);

    void stopCartPerformanceTrace();

    void stopAllCartPerformanceTrace();

    void onSuccessClearPromoStack(int shopIndex);

    void onSuccessCheckPromoFirstStep(ResponseGetPromoStackUiModel responseGetPromoStackUiModel);

    void onFailedClearPromoStack(boolean ignoreAPIResponse);

    Promo generateCheckPromoFirstStepParam();

    void showMerchantVoucherListBottomsheet(ShopGroupData shopGroupData);

    void onClashCheckPromo(ClashingInfoDetailUiModel clashingInfoDetailUiModel, String type);

    void onSuccessClearPromoStackAfterClash();

    String getCartId();

    void renderRecentView(List<RecentView> recentViewList);

    void renderWishlist(List<Wishlist> wishlist);

    void renderRecommendation(RecommendationWidget recommendationWidget);

    void showItemLoading();

    void hideItemLoading();

    void notifyBottomCartParent();

    void setHasTriedToLoadWishList();

    void setHasTriedToLoadRecentView();

    void setHasTriedToLoadRecommendation();

    void triggerSendEnhancedEcommerceAddToCartSuccess(AddToCartDataModel addToCartDataResponseModel, Object productModel);

    void renderInsuranceCartData(InsuranceCartResponse insuranceCartResponse, boolean isRecommendation);

    ArrayList<InsuranceCartDigitalProduct> getInsuranceCartShopData();

    void removeInsuranceProductItem(List<Long> productId);
}
