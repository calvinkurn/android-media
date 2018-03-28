package com.tokopedia.payment.fingerprint.domain;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/27/18.
 */

public class SavePublicKeyUseCase extends UseCase<Boolean> {

    public static final String USER_ID = "user_id";
    public static final String PUBLIC_KEY = "public_key";
    private FingerprintRepository fingerprintRepository;

    public SavePublicKeyUseCase(FingerprintRepository fingerprintRepository) {
        this.fingerprintRepository = fingerprintRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return fingerprintRepository.savePublicKey(requestParams.getParamsAllValueInString());
    }

    public RequestParams createRequestParams(String userId, String publicKey) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(USER_ID, userId);
        requestParams.putString(PUBLIC_KEY, publicKey);
        return requestParams;
    }
}
