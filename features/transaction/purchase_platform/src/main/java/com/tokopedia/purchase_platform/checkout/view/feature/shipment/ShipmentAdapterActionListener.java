package com.tokopedia.purchase_platform.checkout.view.feature.shipment;

import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentDetailData;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment;
import com.tokopedia.purchase_platform.checkout.data.model.request.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.purchase_platform.checkout.data.model.request.DataCheckoutRequest;

import java.util.List;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public interface ShipmentAdapterActionListener {

    void onVoucherMerchantPromoClicked(Object object);

    void onCancelVoucherMerchantClicked(String promoMerchantCode, int position, boolean ignoreAPIResponse);

    void onCartDataEnableToCheckout();

    void onNeedToSaveState(ShipmentCartItemModel shipmentCartItemModel);

    void onCartDataDisableToCheckout(String message);

    void onCheckoutValidationResult(boolean result, Object shipmentData, int position, int requestCode);

    void onChangeAddress();

    void onSendToMultipleAddress(RecipientAddressModel recipientAddressModel, String cartIds);

    void onChooseShipmentDuration(ShipmentCartItemModel shipmentCartItemModel,
                                  RecipientAddressModel recipientAddressModel,
                                  List<ShopShipment> shopShipmentList,
                                  int cartPosition);

    void onChoosePickupPoint(RecipientAddressModel addressAdapterData);

    void onClearPickupPoint(RecipientAddressModel addressAdapterData);

    void onEditPickupPoint(RecipientAddressModel addressAdapterData);

    void onTotalPaymentChange(String totalPayment);

    void onFinishChoosingShipment(List<CheckPromoCodeCartShipmentRequest.Data> data);

    void updateCheckoutRequest(List<DataCheckoutRequest> checkoutRequestData);

    void onRemovePromoCode(String promoCode);

    void resetTotalPrice();

    void showBottomSheetTotalBenefit();

    void onInsuranceChecked(int position);

    void onPriorityChecked(int position);

    void onNeedUpdateViewItem(int position);

    void onSubTotalCartItemClicked(int position);

    void onInsuranceTncClicked();

    void onPriorityTncClicker();

    void onNeedUpdateRequestData();

    void onDropshipCheckedForTrackingAnalytics();

    void onInsuranceCheckedForTrackingAnalytics();

    void onChoosePaymentMethodButtonClicked();

    void onDonationChecked(boolean checked);

    void onEgoldChecked(boolean checked);

    void onChangeShippingDuration(ShipmentCartItemModel shipmentCartItemModel,
                                  RecipientAddressModel recipientAddressModel,
                                  List<ShopShipment> shopShipmentList,
                                  int position);

    void onChangeShippingCourier(List<ShippingCourierViewModel> shippingCourierViewModels,
                                 RecipientAddressModel recipientAddressModel,
                                 ShipmentCartItemModel shipmentCartItemModel,
                                 List<ShopShipment> shopShipmentList, int position);

    void hideSoftKeyboard();

    void onLoadShippingState(int shipperId, int spId, int itemPosition,
                             ShipmentDetailData shipmentDetailData,
                             ShipmentCartItemModel shipmentCartItemModel,
                             List<ShopShipment> shopShipmentList,
                             boolean isCourierRecommendation);

    void onCourierPromoCanceled(String shipperName, String promoCode);

    boolean isToogleYearEndPromoOn();

    void onPurchaseProtectionLogicError();

    void onPurchaseProtectionChangeListener(int position);

    void navigateToProtectionMore(String url);

    void onNotifierClicked(String url);

    void onClickChangePhoneNumber(RecipientAddressModel recipientAddressModel);

    void onProcessToPayment();

    void onProcessToPaymentCod();
}
