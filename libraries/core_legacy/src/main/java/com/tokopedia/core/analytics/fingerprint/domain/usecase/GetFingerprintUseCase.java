package com.tokopedia.core.analytics.fingerprint.domain.usecase;

import com.tokopedia.core.analytics.fingerprint.domain.FingerprintRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by Herdi_WORK on 20.06.17.
 */

public class GetFingerprintUseCase extends UseCase<String> {

    private final FingerprintRepository fingerprintRepository;

    public GetFingerprintUseCase(FingerprintRepository fpRepository){
        fingerprintRepository = fpRepository;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return fingerprintRepository.getFingerPrint();
    }

}
