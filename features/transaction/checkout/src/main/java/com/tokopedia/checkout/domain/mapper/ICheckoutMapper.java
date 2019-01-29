package com.tokopedia.checkout.domain.mapper;

import com.tokopedia.transaction.common.data.cartcheckout.CheckoutData;
import com.tokopedia.transactiondata.entity.response.checkout.CheckoutDataResponse;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public interface ICheckoutMapper {

    CheckoutData convertCheckoutData(CheckoutDataResponse checkoutDataResponse);
}
