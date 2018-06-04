package com.tokopedia.checkout.domain.mapper;

import com.tokopedia.transactiondata.entity.response.shippingaddressform.ShipmentAddressFormDataResponse;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public interface IShipmentMapper {

    CartShipmentAddressFormData convertToShipmentAddressFormData(
            ShipmentAddressFormDataResponse shipmentAddressFormDataResponse
    );
}
