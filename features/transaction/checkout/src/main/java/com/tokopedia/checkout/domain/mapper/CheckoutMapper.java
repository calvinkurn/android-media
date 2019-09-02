package com.tokopedia.checkout.domain.mapper;

import com.google.gson.Gson;
import com.tokopedia.transactiondata.entity.response.checkout.CheckoutDataResponse;
import com.tokopedia.transactiondata.entity.response.checkout.ErrorReporterResponse;
import com.tokopedia.transactiondata.entity.shared.checkout.CheckoutData;
import com.tokopedia.transactiondata.entity.shared.checkout.ErrorReporter;

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
        checkoutData.setJsonResponse(new Gson().toJson(checkoutDataResponse));
        checkoutData.setError(true);
        checkoutData.setErrorMessage(checkoutDataResponse.getError());
//        if (checkoutDataResponse.getErrorReporterResponse() != null) {
            ErrorReporterResponse errorReporterResponse = checkoutDataResponse.getErrorReporterResponse();
            ErrorReporter errorReporter = new ErrorReporter(true, "blablabla");
            checkoutData.setErrorReporter(errorReporter);
//        }
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
