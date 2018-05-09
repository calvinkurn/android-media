package com.tokopedia.checkout.domain.mapper;

import com.tokopedia.transactiondata.entity.response.checkout.CheckoutDataResponse;
import com.tokopedia.checkout.domain.datamodel.cartcheckout.CheckoutData;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public interface ICheckoutMapper {

    CheckoutData convertCheckoutData(CheckoutDataResponse checkoutDataResponse);
}
