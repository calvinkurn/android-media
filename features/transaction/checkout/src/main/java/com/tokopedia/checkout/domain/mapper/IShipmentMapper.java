package com.tokopedia.checkout.domain.mapper;

import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.checkout.data.model.response.shipment_address_form.ShipmentAddressFormDataResponse;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public interface IShipmentMapper {

    CartShipmentAddressFormData convertToShipmentAddressFormData(
            ShipmentAddressFormDataResponse shipmentAddressFormDataResponse
    );
}
