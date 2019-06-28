package com.tokopedia.checkout.view.feature.cartlist;

import android.app.Activity;

import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartTickerErrorData;
import com.tokopedia.checkout.domain.datamodel.cartlist.ShopGroupData;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.checkout.domain.datamodel.voucher.promostacking.ResponseFirstStep;
import com.tokopedia.checkout.view.common.base.IBaseView;
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartShopHolderData;
import com.tokopedia.promocheckout.common.data.entity.request.Promo;
import com.tokopedia.promocheckout.common.view.model.PromoData;
import com.tokopedia.promocheckout.common.view.model.PromoStackingData;
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel;

import java.util.List;
import java.util.Map;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public interface ICartListView extends IBaseView {

    void renderInitialGetCartListDataSuccess(CartListData cartListData);

    void renderErrorInitialGetCartListData(String message);

    void renderActionDeleteCartDataSuccess(CartItemData cartItemData, String message, boolean addWishList);

    void renderNoRecipientAddressShipmentForm(CartShipmentAddressFormData shipmentAddressFormData);

    void renderToShipmentFormSuccess(Map<String, Object> stringObjectMap,
                                     boolean checkoutProductEligibleForCashOnDelivery, int condition);

    void renderToAddressChoice();

    void renderErrorToShipmentForm(String message);

    void renderCheckPromoCodeFromSuggestedPromoSuccess(PromoCodeCartListData promoCodeCartListData);

    void renderCheckPromoStackingCodeFromSuggestedPromoSuccess(ResponseFirstStep responseFirstStep);

    void renderEmptyCartData(CartListData cartListData);

    void disableSwipeRefresh();

    void enableSwipeRefresh();

    List<CartShopHolderData> getAllShopDataList();

    List<CartItemData> getSelectedCartDataList();

    List<CartItemData> getAllCartDataList();

    void renderDetailInfoSubTotal(String qty, String subtotalPrice, boolean selectAllItem);

    void updateCashback(double cashback);

    void renderPromoVoucher();

    void renderPromoGlobalVoucher();

    void showToastMessageRed(String message);

    void showToastMessageGreen(String message);

    void renderLoadGetCartData();

    void renderLoadGetCartDataFinish();

    void renderCartTickerError(CartTickerErrorData cartTickerErrorData);

    void renderCancelAutoApplyCouponSuccess();

    void renderCancelAutoApplyCouponStackSuccess(int position);

    void renderCancelAutoApplyCouponError();

    void onDeleteCartDataSuccess();

    Activity getActivity();

    void goToCouponList();

    void goToDetail(PromoData promoData);

    void goToDetailPromoStacking(PromoStackingData promoStackingData);

    void stopTrace();

    void renderAppliedPromoStacking();

    void onSuccessClearPromoStack(int shopIndex);

    void onSuccessCheckPromoFirstStep(ResponseGetPromoStackUiModel responseGetPromoStackUiModel);

    void onFailedClearPromoStack(boolean ignoreAPIResponse);

    Promo generateCheckPromoFirstStepParam();

    void showMerchantVoucherListBottomsheet(ShopGroupData shopGroupData);

    void onClashCheckPromo(ClashingInfoDetailUiModel clashingInfoDetailUiModel, String type);

    void onSuccessClearPromoStackAfterClash();

    String getCartId();

    PromoStackingData getPromoStackingGlobalData();
}
