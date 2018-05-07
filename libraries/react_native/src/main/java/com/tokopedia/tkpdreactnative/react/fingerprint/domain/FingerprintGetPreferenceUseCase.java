package com.tokopedia.tkpdreactnative.react.fingerprint.domain;

import com.tokopedia.tkpdreactnative.react.common.data.PreferenceRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/24/18.
 */

public class FingerprintGetPreferenceUseCase extends UseCase<Boolean> {

    private PreferenceRepository preferenceRepository;

    @Inject
    public FingerprintGetPreferenceUseCase(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return preferenceRepository.getPreferenceBoolean(FingerprintSavePreferenceUseCase.IS_SHOW_FINGERPRINT);
    }
}
