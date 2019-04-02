package com.tokopedia.tkpdreactnative.react.singleauthpayment.domain;

import com.tokopedia.tkpdreactnative.react.common.data.PreferenceRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/24/18.
 */

public class SinglePaymentSavePreferenceUseCase extends UseCase<Boolean> {

    public static final String IS_SHOW_SINGLE_PAYMENT = "is_show_single_payment";
    private PreferenceRepository preferenceRepository;

    @Inject
    public SinglePaymentSavePreferenceUseCase(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return preferenceRepository.savePreference(requestParams.getParameters());
    }

    public RequestParams createRequestParams(boolean isShow){
        RequestParams requestParams = RequestParams.create();
        requestParams.putBoolean(IS_SHOW_SINGLE_PAYMENT, isShow);
        return requestParams;
    }
}
