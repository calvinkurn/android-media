package com.tokopedia.digital.product.data.mapper;

import com.tokopedia.digital.product.data.entity.response.InquiryBalanceResponse;
import com.tokopedia.digital.product.data.entity.response.Response;
import com.tokopedia.digital.product.view.model.CardInfo;
import com.tokopedia.digital.product.view.model.InquiryBalanceModel;

/**
 * Created by Rizky on 18/05/18.
 */
public class InquiryBalanceMapper {

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

}
