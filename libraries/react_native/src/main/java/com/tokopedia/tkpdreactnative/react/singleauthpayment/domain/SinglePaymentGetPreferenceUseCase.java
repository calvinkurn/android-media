package com.tokopedia.tkpdreactnative.react.singleauthpayment.domain;

import com.tokopedia.tkpdreactnative.react.common.data.PreferenceRepository;
import com.tokopedia.tkpdreactnative.react.fingerprint.domain.FingerprintSavePreferenceUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/24/18.
 */

public class SinglePaymentGetPreferenceUseCase extends UseCase<Boolean> {

    private PreferenceRepository preferenceRepository;

    @Inject
    public SinglePaymentGetPreferenceUseCase(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return preferenceRepository.getPreferenceBoolean(SinglePaymentSavePreferenceUseCase.IS_SHOW_SINGLE_PAYMENT);
    }
}
