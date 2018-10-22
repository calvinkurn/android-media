package com.tokopedia.loginregister.activation.domain.usecase;

import com.tokopedia.loginregister.activation.domain.mapper.ActivationMapper;
import com.tokopedia.loginregister.activation.domain.pojo.ActivateUnicodePojo;
import com.tokopedia.loginregister.common.data.LoginRegisterApi;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nisie on 4/17/17.
 */

public class ActivateUnicodeUseCase extends UseCase<ActivateUnicodePojo> {

    public static final String PARAM_UNICODE = "password";
    public static final String PARAM_EMAIL = "username";
    public static final String PARAM_GRANT_TYPE = "grant_type";
    public static final String PARAM_PASSWORD_TYPE = "password_type";
    public static final String DEFAULT_GRANT_TYPE = "password";
    public static final String DEFAULT_PASSWORD_TYPE = "activation_unique_code";

    private final LoginRegisterApi loginRegisterApi;
    private final ActivationMapper mapper;

    @Inject
    public ActivateUnicodeUseCase(LoginRegisterApi loginRegisterApi,
                                  ActivationMapper mapper) {
        this.loginRegisterApi = loginRegisterApi;
        this.mapper = mapper;
    }

    @Override
    public Observable<ActivateUnicodePojo> createObservable(RequestParams requestParams) {
        return loginRegisterApi.activateWithUnicode(requestParams.getParameters())
                .map(mapper);
    }

    public static RequestParams getParam(String email, String unicode) {
        RequestParams param = RequestParams.create();
        param.putString(ActivateUnicodeUseCase.PARAM_EMAIL, email);
        param.putString(ActivateUnicodeUseCase.PARAM_UNICODE, unicode);
        param.putString(ActivateUnicodeUseCase.PARAM_GRANT_TYPE,
                ActivateUnicodeUseCase.DEFAULT_GRANT_TYPE);
        param.putString(ActivateUnicodeUseCase.PARAM_PASSWORD_TYPE,
                ActivateUnicodeUseCase.DEFAULT_PASSWORD_TYPE);
        return param;
    }
}