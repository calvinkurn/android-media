package com.tokopedia.purchase_platform.features.checkout.domain.mapper;

import com.google.gson.Gson;
import com.tokopedia.purchase_platform.common.base.IMapperUtil;
import com.tokopedia.purchase_platform.common.domain.model.CheckoutData;
import com.tokopedia.purchase_platform.common.domain.model.ErrorReporter;
import com.tokopedia.purchase_platform.common.domain.model.ErrorReporterText;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout.CheckoutDataResponse;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout.CheckoutResponse;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout.ErrorReporterResponse;

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
    public CheckoutData convertCheckoutData(CheckoutResponse checkoutResponse) {
        CheckoutDataResponse checkoutDataResponse = checkoutResponse.getData();
        CheckoutData checkoutData = new CheckoutData();
        checkoutData.setJsonResponse(new Gson().toJson(checkoutResponse));
        checkoutData.setError(checkoutDataResponse.getSuccess() != 1);
        checkoutData.setErrorMessage(checkoutDataResponse.getError());
        ErrorReporterResponse errorReporterResponse = checkoutResponse.getErrorReporter();
        ErrorReporter errorReporter = new ErrorReporter();
        errorReporter.setEligible(errorReporterResponse.getEligible());
        ErrorReporterText errorReporterText = new ErrorReporterText();
        errorReporterText.setSubmitTitle(errorReporterResponse.getTexts().getSubmitTitle());
        errorReporterText.setSubmitDescription(errorReporterResponse.getTexts().getSubmitDescription());
        errorReporterText.setSubmitButton(errorReporterResponse.getTexts().getSubmitButton());
        errorReporterText.setCancelButton(errorReporterResponse.getTexts().getCancelButton());
        errorReporter.setTexts(errorReporterText);
        checkoutData.setErrorReporter(errorReporter);
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
