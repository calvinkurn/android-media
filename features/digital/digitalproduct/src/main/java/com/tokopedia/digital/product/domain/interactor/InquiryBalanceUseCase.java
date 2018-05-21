package com.tokopedia.digital.product.domain.interactor;

import com.tokopedia.digital.product.data.repository.EMoneyRepository;
import com.tokopedia.digital.product.view.model.InquiryBalanceModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by Rizky on 18/05/18.
 */
public class InquiryBalanceUseCase extends UseCase<InquiryBalanceModel> {

    private final String PARAM_ISSUER_ID = "PARAM_ISSUER_ID";
    private final String PARAM_CARD_ATTRIBUTE = "PARAM_CARD_ATTRIBUTE";
    private final String PARAM_CARD_INFO = "PARAM_CARD_INFO";
    private final String PARAM_CARD_UID = "PARAM_CARD_UID";
    private final String PARAM_CARD_LASTBALANCE = "PARAM_CARD_LASTBALANCE";

    private EMoneyRepository eMoneyRepository;

    public InquiryBalanceUseCase(EMoneyRepository eMoneyRepository) {
        this.eMoneyRepository = eMoneyRepository;
    }

    @Override
    public Observable<InquiryBalanceModel> createObservable(RequestParams requestParams) {
        return eMoneyRepository.inquiryBalance();
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

}
