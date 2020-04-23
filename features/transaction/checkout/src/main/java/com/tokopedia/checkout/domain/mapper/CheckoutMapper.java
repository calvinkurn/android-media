package com.tokopedia.checkout.domain.mapper;

import com.google.gson.Gson;
import com.tokopedia.checkout.domain.model.checkout.CheckoutData;
import com.tokopedia.checkout.domain.model.checkout.ErrorReporter;
import com.tokopedia.checkout.domain.model.checkout.ErrorReporterText;
import com.tokopedia.checkout.domain.model.checkout.MessageData;
import com.tokopedia.checkout.domain.model.checkout.PriceValidationData;
import com.tokopedia.checkout.domain.model.checkout.TrackerData;
import com.tokopedia.checkout.data.model.response.checkout.CheckoutDataResponse;
import com.tokopedia.checkout.data.model.response.checkout.CheckoutResponse;
import com.tokopedia.checkout.data.model.response.checkout.ErrorReporterResponse;
import com.tokopedia.checkout.data.model.response.checkout.Message;
import com.tokopedia.checkout.data.model.response.checkout.Tracker;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public class CheckoutMapper implements ICheckoutMapper {

    @Inject
    public CheckoutMapper() {
    }

    @Override
    public CheckoutData convertCheckoutData(CheckoutResponse checkoutResponse) {
        CheckoutDataResponse checkoutDataResponse = checkoutResponse.getData();
        CheckoutData checkoutData = new CheckoutData();
        checkoutData.setJsonResponse(new Gson().toJson(checkoutResponse));
        checkoutData.setError(checkoutDataResponse.getSuccess() != 1);
        checkoutData.setErrorMessage(checkoutDataResponse.getError());

        if (checkoutDataResponse.getData() != null &&
                checkoutDataResponse.getData().getPriceValidation() != null &&
                checkoutDataResponse.getData().getPriceValidation().isUpdated() &&
                checkoutDataResponse.getData().getPriceValidation().getMessage() != null) {
            Message message = checkoutDataResponse.getData().getPriceValidation().getMessage();
            MessageData messageData = new MessageData();
            messageData.setTitle(message.getTitle());
            messageData.setDesc(message.getDesc());
            messageData.setAction(message.getAction());

            PriceValidationData priceValidationData = new PriceValidationData();
            priceValidationData.setUpdated(true);
            priceValidationData.setMessage(messageData);

            Tracker tracker = checkoutDataResponse.getData().getPriceValidation().getTrackerData();
            if (tracker != null) {
                TrackerData trackerData = new TrackerData();
                trackerData.setCampaignType(tracker.getCampaignType());
                trackerData.setProductChangesType(tracker.getProductChangesType());
                List<String> productIds = new ArrayList<>();
                for (Long aLong : tracker.getProductIds()) {
                    productIds.add(String.valueOf(aLong));
                }
                trackerData.setProductIds(productIds);
                priceValidationData.setTrackerData(trackerData);
            }

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
