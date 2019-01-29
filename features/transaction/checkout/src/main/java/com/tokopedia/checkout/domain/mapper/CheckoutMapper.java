package com.tokopedia.checkout.domain.mapper;

import com.tokopedia.transaction.common.data.cartcheckout.CheckoutData;
import com.tokopedia.transactiondata.entity.response.checkout.CheckoutDataResponse;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public class CheckoutMapper implements ICheckoutMapper {

    private final IMapperUtil mapperUtil;

    @Inject
    public CheckoutMapper(IMapperUtil mapperUtil) {
        this.mapperUtil = mapperUtil;
    }

    @Override
    public CheckoutData convertCheckoutData(CheckoutDataResponse checkoutDataResponse) {
        CheckoutData checkoutData = new CheckoutData();
        checkoutData.setError(checkoutDataResponse.getSuccess() != 1);
        checkoutData.setErrorMessage(checkoutDataResponse.getError());
        if (!checkoutData.isError()
                && !mapperUtil.isEmpty(checkoutDataResponse.getData())
                && !mapperUtil.isEmpty(checkoutDataResponse.getData().getParameter())) {
            checkoutData.setTransactionId(checkoutDataResponse.getData().getParameter().getTransactionId());
            checkoutData.setPaymentId(checkoutDataResponse.getData().getParameter().getTransactionId());
            checkoutData.setQueryString(checkoutDataResponse.getData().getQueryString());
            checkoutData.setRedirectUrl(checkoutDataResponse.getData().getRedirectUrl());
            checkoutData.setCallbackSuccessUrl(checkoutDataResponse.getData().getCallbackUrl());
            checkoutData.setCallbackFailedUrl(checkoutDataResponse.getData().getCallbackUrl());
        }
        return checkoutData;
    }
}
