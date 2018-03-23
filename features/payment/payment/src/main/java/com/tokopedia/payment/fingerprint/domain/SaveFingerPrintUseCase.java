package com.tokopedia.payment.fingerprint.domain;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/22/18.
 */

public class SaveFingerPrintUseCase extends UseCase<Boolean> {

    private FingerprintRepository fingerprintRepository;

    public SaveFingerPrintUseCase(FingerprintRepository fingerprintRepository) {
        this.fingerprintRepository = fingerprintRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return fingerprintRepository.saveFingerprint(requestParams.getParamsAllValueInString());
    }
}
