package com.tokopedia.checkout.domain.mapper;

import com.tokopedia.transactiondata.entity.response.checkout.CheckoutResponse;
import com.tokopedia.transactiondata.entity.shared.checkout.CheckoutData;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public interface ICheckoutMapper {

    CheckoutData convertCheckoutData(CheckoutResponse checkoutResponse);
}
