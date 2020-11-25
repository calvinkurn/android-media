package com.tokopedia.checkout.domain.mapper;

import com.tokopedia.checkout.domain.model.checkout.CheckoutData;
import com.tokopedia.checkout.data.model.response.checkout.CheckoutResponse;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public interface ICheckoutMapper {

    CheckoutData convertCheckoutData(CheckoutResponse checkoutDataResponse);
}
