package com.tokopedia.payment.fingerprint.domain;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 4/9/18.
 */

public class GetPostDataOtpUseCase extends UseCase<HashMap<String, String>> {

    public static final String TRANSACTION_ID = "transaction_id";
    private FingerprintRepository fingerprintRepository;

    @Inject
    public GetPostDataOtpUseCase(FingerprintRepository fingerprintRepository) {
        this.fingerprintRepository = fingerprintRepository;
    }

    @Override
    public Observable<HashMap<String, String>> createObservable(final RequestParams requestParams) {
        return fingerprintRepository.getPostDataOtp(requestParams.getString(TRANSACTION_ID, ""));
    }

    public RequestParams createRequestParams(String transactionId, String urlOtp){
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(TRANSACTION_ID, transactionId);
        return requestParams;
    }
}
