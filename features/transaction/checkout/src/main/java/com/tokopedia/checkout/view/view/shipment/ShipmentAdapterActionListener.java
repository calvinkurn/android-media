package com.tokopedia.checkout.view.view.shipment;

import com.tokopedia.checkout.data.entity.request.DataCheckoutRequest;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.view.adapter.CartAdapterActionListener;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentCartItem;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;

import java.util.List;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public interface ShipmentAdapterActionListener extends CartAdapterActionListener {

    void onAddOrChangeAddress();

    void onChooseShipment(int position, ShipmentCartItem shipmentCartItem,
                          RecipientAddressModel recipientAddressModel);

    void onChoosePickupPoint(RecipientAddressModel addressAdapterData);

    void onClearPickupPoint(RecipientAddressModel addressAdapterData);

    void onEditPickupPoint(RecipientAddressModel addressAdapterData);

    void onTotalPaymentChange(ShipmentCostModel shipmentCostModel);

    void onFinishChoosingShipment(List<CheckPromoCodeCartShipmentRequest.Data> data, List<DataCheckoutRequest> checkoutRequest);

    void onShowPromoMessage(String promoMessage);

    void onHidePromoMessage();

    void onRemovePromoCode();

    void resetTotalPrice();

    void onInsuranceChecked(int position);

    void onViewVisibilityStateChanged(int position);

    void onInsuranceTncClicked();

}
