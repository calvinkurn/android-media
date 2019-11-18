package com.tokopedia.purchase_platform.features.checkout.domain.mapper;

import com.google.gson.Gson;
import com.tokopedia.purchase_platform.common.domain.model.CheckoutData;
import com.tokopedia.purchase_platform.common.domain.model.ErrorReporter;
import com.tokopedia.purchase_platform.common.domain.model.ErrorReporterText;
import com.tokopedia.purchase_platform.common.domain.model.MessageData;
import com.tokopedia.purchase_platform.common.domain.model.PriceValidationData;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout.CheckoutDataResponse;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout.CheckoutResponse;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout.ErrorReporterResponse;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout.Message;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public class CheckoutMapper implements ICheckoutMapper {

    @Inject
    public CheckoutMapper() {}

    @Override
    public CheckoutData convertCheckoutData(CheckoutResponse checkoutResponse) {
        CheckoutDataResponse checkoutDataResponse = checkoutResponse.getData();
        CheckoutData checkoutData = new CheckoutData();
        checkoutData.setJsonResponse(new Gson().toJson(checkoutResponse));
        checkoutData.setError(checkoutDataResponse.getSuccess() != 1);
        checkoutData.setErrorMessage(checkoutDataResponse.getError());

        if (checkoutDataResponse.getData().getPriceValidation() != null && checkoutDataResponse.getData().getPriceValidation().isUpdated() &&
                checkoutDataResponse.getData().getPriceValidation().getMessage() != null) {
            Message message = checkoutDataResponse.getData().getPriceValidation().getMessage();
            MessageData messageData = new MessageData();
            messageData.setTitle(message.getTitle());
            messageData.setDesc(message.getDesc());
            messageData.setAction(message.getAction());

            PriceValidationData priceValidationData = new PriceValidationData();
            priceValidationData.setUpdated(true);
            priceValidationData.setMessage(messageData);

            checkoutData.setPriceValidationData(priceValidationData);
        }

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
                && checkoutDataResponse.getData() != null
                && checkoutDataResponse.getData().getParameter() != null) {
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
