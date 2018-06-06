package com.tokopedia.tkpdreactnative.react.fingerprint.domain;

import com.tokopedia.tkpdreactnative.react.common.data.PreferenceRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/24/18.
 */

public class FingerprintSavePreferenceUseCase extends UseCase<Boolean> {

    public static final String IS_SHOW_FINGERPRINT = "is_show_fingerprint";
    private PreferenceRepository preferenceRepository;

    @Inject
    public FingerprintSavePreferenceUseCase(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return preferenceRepository.savePreference(requestParams.getParameters());
    }

    public RequestParams createRequestParams(boolean isShow){
        RequestParams requestParams = RequestParams.create();
        requestParams.putBoolean(IS_SHOW_FINGERPRINT, isShow);
        return requestParams;
    }
}
