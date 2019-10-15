package com.tokopedia.purchase_platform.features.checkout.domain.mapper;

import com.tokopedia.purchase_platform.common.domain.model.CheckoutData;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout.CheckoutResponse;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public interface ICheckoutMapper {

    CheckoutData convertCheckoutData(CheckoutResponse checkoutDataResponse);
}
