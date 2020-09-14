package com.tokopedia.checkout.view;

import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData;
import com.tokopedia.logisticcart.shipping.model.ShopShipment;
import com.tokopedia.purchase_platform.common.feature.checkout.request.DataCheckoutRequest;
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel;

import java.util.List;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public interface ShipmentAdapterActionListener {

    void onCancelVoucherLogisticClicked(String pslCode, int position);

    void onDataEnableToCheckout();

    void onNeedToSaveState(ShipmentCartItemModel shipmentCartItemModel);

    void onDataDisableToCheckout(String message);

    void onCheckoutValidationResult(boolean result, Object shipmentData, int position, int requestCode);

    void onChangeAddress();

    void onChooseShipmentDuration(ShipmentCartItemModel shipmentCartItemModel,
                                  RecipientAddressModel recipientAddressModel,
                                  int cartPosition);

    void onTotalPaymentChange(String totalPayment);

    void onFinishChoosingShipment();

    void updateCheckoutRequest(List<DataCheckoutRequest> checkoutRequestData);

    void resetTotalPrice();

    void onInsuranceChecked(int position);

    void onPriorityChecked(int position);

    void onNeedUpdateViewItem(int position);

    void onSubTotalItemClicked(int position);

    void onInsuranceTncClicked();

    void onPriorityTncClicker();

    void onOntimeDeliveryClicked(String url);

    void onImpressionOntimeDelivery(String message);

    void onNeedUpdateRequestData();

    void onDropshipCheckedForTrackingAnalytics();

    void onInsuranceCheckedForTrackingAnalytics();

    void onChoosePaymentMethodButtonClicked();

    void onDonationChecked(boolean checked);

    void onEgoldChecked(boolean checked);

    void onChangeShippingDuration(ShipmentCartItemModel shipmentCartItemModel,
                                  RecipientAddressModel recipientAddressModel,
                                  int position);

    void onChangeShippingCourier(RecipientAddressModel recipientAddressModel,
                                 ShipmentCartItemModel shipmentCartItemModel,
                                 int position);

    void hideSoftKeyboard();

    void onLoadShippingState(int shipperId, int spId, int itemPosition,
                             ShipmentDetailData shipmentDetailData,
                             ShipmentCartItemModel shipmentCartItemModel,
                             List<ShopShipment> shopShipmentList,
                             boolean isTradeInDropOff);

    void onCourierPromoCanceled(String shipperName, String promoCode);

    void onPurchaseProtectionLogicError();

    void onPurchaseProtectionChangeListener(int position);

    void navigateToProtectionMore(String url);

    void onNotifierClicked(String url);

    void onClickChangePhoneNumber(RecipientAddressModel recipientAddressModel);

    void onProcessToPayment();

    void onProcessToPaymentCod();

    void onChangeTradeInDropOffClicked();

    boolean isTradeInByDropOff();

    boolean hasSelectTradeInLocation();

    void onTradeInAddressTabChanged(int position);

    void onClickPromoCheckout(LastApplyUiModel lastApplyUiModel);

    void onSendAnalyticsClickPromoCheckout(Boolean isApplied, List<String> listAllPromoCodes);

    void onSendAnalyticsViewPromoCheckoutApplied();

    void onCheckShippingCompletionClicked();

    void onShowTickerShippingCompletion();
}