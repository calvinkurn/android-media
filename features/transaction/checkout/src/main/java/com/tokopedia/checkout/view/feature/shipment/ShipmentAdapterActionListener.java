package com.tokopedia.checkout.view.feature.shipment;

import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShopShipment;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.view.common.adapter.CartAdapterActionListener;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentCartItemModel;
import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view.ShippingCourierViewModel;
import com.tokopedia.transactiondata.entity.request.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.transactiondata.entity.request.DataCheckoutRequest;

import java.util.List;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public interface ShipmentAdapterActionListener extends CartAdapterActionListener {

    void onChangeAddress();

    void onSendToMultipleAddress(RecipientAddressModel recipientAddressModel, String cartIds);

    void onChooseShipment(int position, ShipmentCartItemModel shipmentCartItemModel,
                          RecipientAddressModel recipientAddressModel);

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

    void onRemovePromoCode();

    void resetTotalPrice();

    void onInsuranceChecked(int position);

    void onNeedUpdateViewItem(int position);

    void onSubTotalCartItemClicked(int position);

    void onInsuranceTncClicked();

    void onNeedUpdateRequestData();

    void onDropshipCheckedForTrackingAnalytics();

    void onInsuranceCheckedForTrackingAnalytics();

    void onChoosePaymentMethodButtonClicked();

    void onDonationChecked(boolean checked);

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

    void onCourierPromoCanceled(String shipperName);

    boolean isToogleYearEndPromoOn();

    void onPurchaseProtectionLogicError();

    void onPurchaseProtectionChangeListener(int position);

    void navigateToProtectionMore(String url);
}
