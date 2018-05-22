package com.tokopedia.digital.product.additionalfeature.etoll.domain.interactor;

import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.requestbody.smartcardinquiry.Attributes;
import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.requestbody.smartcardinquiry.CardRequest;
import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.requestbody.smartcardinquiry.RequestBodySmartcardInquiry;
import com.tokopedia.digital.product.additionalfeature.etoll.data.repository.ETollRepository;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by Rizky on 18/05/18.
 */
public class SmartcardInquiryUseCase extends UseCase<InquiryBalanceModel> {

    private final String PARAM_ISSUER_ID = "PARAM_ISSUER_ID";
    private final String PARAM_CARD_ATTRIBUTE = "PARAM_CARD_ATTRIBUTE";
    private final String PARAM_CARD_INFO = "PARAM_CARD_INFO";
    private final String PARAM_CARD_UID = "PARAM_CARD_UID";
    private final String PARAM_CARD_LASTBALANCE = "PARAM_CARD_LASTBALANCE";

    private ETollRepository eTollRepository;

    public SmartcardInquiryUseCase(ETollRepository eTollRepository) {
        this.eTollRepository = eTollRepository;
    }

    @Override
    public Observable<InquiryBalanceModel> createObservable(RequestParams requestParams) {
        int issuerId = requestParams.getInt(PARAM_ISSUER_ID, 0);
        String cardAttribute = requestParams.getString(PARAM_CARD_ATTRIBUTE, "");
        String cardInfo = requestParams.getString(PARAM_CARD_INFO, "");
        String cardUID = requestParams.getString(PARAM_CARD_UID, "");
        String cardLastBalance = requestParams.getString(PARAM_CARD_LASTBALANCE, "");
        RequestBodySmartcardInquiry requestBodySmartcardInquiry = createRequestBodyInquiryBalance(
                issuerId, cardAttribute, cardInfo, cardUID, cardLastBalance
        );
        return eTollRepository.inquiryBalance(requestBodySmartcardInquiry);
    }

    public RequestParams createRequestParam(int issuerId, String cardAttribute, String cardInfo,
                                            String cardUID, String cardLastBalance) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(PARAM_ISSUER_ID, issuerId);
        requestParams.putString(PARAM_CARD_ATTRIBUTE, cardAttribute);
        requestParams.putString(PARAM_CARD_INFO, cardInfo);
        requestParams.putString(PARAM_CARD_UID, cardUID);
        requestParams.putString(PARAM_CARD_LASTBALANCE, cardLastBalance);
        return requestParams;
    }

    private RequestBodySmartcardInquiry createRequestBodyInquiryBalance(int issuerId,
                                                                        String cardAttribute,
                                                                        String cardInfo,
                                                                        String cardUID,
                                                                        String cardLastBalance) {
        CardRequest cardRequest = new CardRequest(issuerId, cardAttribute, cardInfo, cardUID, cardLastBalance);
        Attributes attributes = new Attributes(cardRequest);
        return new RequestBodySmartcardInquiry("smartcard_inquiry", attributes);
    }

}
