package com.tokopedia.purchase_platform.checkout.domain.mapper;

import com.tokopedia.purchase_platform.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transactiondata.entity.response.shippingaddressform.ShipmentAddressFormDataResponse;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public interface IShipmentMapper {

    CartShipmentAddressFormData convertToShipmentAddressFormData(
            ShipmentAddressFormDataResponse shipmentAddressFormDataResponse
    );
}
