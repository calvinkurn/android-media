package com.tokopedia.digital.product.additionalfeature.etoll.data.mapper;

import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.response.ResponseSmartcardInquiry;
import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.response.Response;
import com.tokopedia.digital.product.view.model.CardInfo;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;

/**
 * Created by Rizky on 18/05/18.
 */
public class SmartcardInquiryMapper {

    public InquiryBalanceModel map(Response inquiryBalanceResponse) {
        String errorMessage = null;
        if (inquiryBalanceResponse.getData().getAttributes().getError() != null) {
            errorMessage = inquiryBalanceResponse.getData().getAttributes().getError().getTitle();
        }

        return new InquiryBalanceModel(
                inquiryBalanceResponse.getData().getAttributes().getStatus(),
                new CardInfo(inquiryBalanceResponse.getData().getAttributes().getCardResponse().getCardNumber(),
                        inquiryBalanceResponse.getData().getAttributes().getCardResponse().getLastBalance(),
                        inquiryBalanceResponse.getData().getAttributes().getCardResponse().getIssuerImage()),
                inquiryBalanceResponse.getData().getAttributes().getCardResponse().getButtonText(),
                inquiryBalanceResponse.getData().getAttributes().getCardResponse().getPayload(),
                errorMessage
        );
    }

    public InquiryBalanceModel map(ResponseSmartcardInquiry responseSmartcardInquiry) {
        String errorMessage = null;
        if (responseSmartcardInquiry.getAttributes().getError() != null) {
            errorMessage = responseSmartcardInquiry.getAttributes().getError().getTitle();
        }

        return new InquiryBalanceModel(
                responseSmartcardInquiry.getAttributes().getStatus(),
                new CardInfo(responseSmartcardInquiry.getAttributes().getCardResponse().getCardNumber(),
                        responseSmartcardInquiry.getAttributes().getCardResponse().getLastBalance(),
                        responseSmartcardInquiry.getAttributes().getCardResponse().getIssuerImage()),
                responseSmartcardInquiry.getAttributes().getCardResponse().getButtonText(),
                responseSmartcardInquiry.getAttributes().getCardResponse().getPayload(),
                errorMessage
        );
    }

}
