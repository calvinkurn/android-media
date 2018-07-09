package com.tokopedia.digital.product.additionalfeature.etoll.domain.interactor;

import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.requestbody.smartcardcommand.Attributes;
import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.requestbody.smartcardcommand.CardRequest;
import com.tokopedia.digital.product.additionalfeature.etoll.data.entity.requestbody.smartcardcommand.RequestBodySmartcardCommand;
import com.tokopedia.digital.product.additionalfeature.etoll.data.repository.ETollRepository;
import com.tokopedia.digital.product.additionalfeature.etoll.view.model.InquiryBalanceModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by Rizky on 18/05/18.
 */
public class SmartcardCommandUseCase extends UseCase<InquiryBalanceModel> {

    private static final String PARAM_PAYLOAD = "PARAM_PAYLOAD";
    private static final String PARAM_ID = "PARAM_ID";
    private static final String PARAM_ISSUER_ID = "PARAM_ISSUER_ID";

    private ETollRepository eTollRepository;

    public SmartcardCommandUseCase(ETollRepository eTollRepository) {
        this.eTollRepository = eTollRepository;
    }

    @Override
    public Observable<InquiryBalanceModel> createObservable(RequestParams requestParams) {
        String payload = requestParams.getString(PARAM_PAYLOAD, "");
        int id = requestParams.getInt(PARAM_ID, 0);
        int issuerId = requestParams.getInt(PARAM_ISSUER_ID, 0);
        RequestBodySmartcardCommand requestBodySmartcardCommand = createRequestBodySmartcardCommand(
                payload, id, issuerId);
        return eTollRepository.sendCommand(requestBodySmartcardCommand);
    }

    public RequestParams createRequestParams(String payload, int id, int issuerId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_PAYLOAD, payload);
        requestParams.putInt(PARAM_ID, id);
        requestParams.putInt(PARAM_ISSUER_ID, issuerId);
        return requestParams;
    }

    private RequestBodySmartcardCommand createRequestBodySmartcardCommand(String payload, int id, int issuerId) {
        CardRequest cardRequest = new CardRequest(payload, issuerId);
        Attributes attributes = new Attributes(cardRequest);
        return new RequestBodySmartcardCommand("smartcard_command", id, attributes);
    }

}
