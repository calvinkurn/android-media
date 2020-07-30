package com.tokopedia.digital.product.additionalfeature.etoll.data.mapper;

import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.response.ResponseSmartcard;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;
import com.tokopedia.digital.product.view.model.CardInfo;
import com.tokopedia.digital.utils.NFCUtils;

import javax.inject.Inject;

/**
 * Created by Rizky on 18/05/18.
 */
public class SmartcardMapper {

    @Inject
    public SmartcardMapper() {
    }

    public InquiryBalanceModel map(ResponseSmartcard responseSmartcard) {
        String errorMessage = null;
        if (responseSmartcard.getAttributes().getError() != null) {
            errorMessage = responseSmartcard.getAttributes().getError().getTitle();
        }

        return new InquiryBalanceModel(
                responseSmartcard.getId(),
                responseSmartcard.getAttributes().getStatus(),
                new CardInfo(responseSmartcard.getAttributes().getCardResponse().getCardNumber(),
                        NFCUtils.formatCardUID(responseSmartcard.getAttributes().getCardResponse().getCardNumber()),
                        responseSmartcard.getAttributes().getCardResponse().getLastBalance(),
                        responseSmartcard.getAttributes().getCardResponse().getIssuerImage()),
                responseSmartcard.getAttributes().getCardResponse().getButtonText(),
                responseSmartcard.getAttributes().getCardResponse().getPayload(),
                errorMessage
        );
    }

}
