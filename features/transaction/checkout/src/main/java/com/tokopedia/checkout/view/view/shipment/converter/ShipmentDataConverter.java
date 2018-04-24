package com.tokopedia.checkout.view.view.shipment.converter;

import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentMultipleAddressItem;
import com.tokopedia.checkout.view.view.shipment.viewmodel.ShipmentSingleAddressItem;

public class ShipmentDataConverter {

    public ShipmentItem convert(CartShipmentAddressFormData cartShipmentAddressFormData) {
        ShipmentItem shipmentItem;
        if (cartShipmentAddressFormData.isMultiple()) {
            shipmentItem = new ShipmentMultipleAddressItem();
        } else {
            shipmentItem = new ShipmentSingleAddressItem();
        }

        return shipmentItem;
    }

}
