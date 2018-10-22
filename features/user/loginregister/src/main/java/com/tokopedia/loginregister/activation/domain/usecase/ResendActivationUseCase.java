package com.tokopedia.loginregister.activation.domain.usecase;


import com.tokopedia.loginregister.activation.domain.mapper.ActionMapper;
import com.tokopedia.loginregister.activation.domain.pojo.ActionPojo;
import com.tokopedia.loginregister.common.data.LoginRegisterApi;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nisie on 4/17/17.
 */

public class ResendActivationUseCase extends UseCase<ActionPojo> {

    public static final String PARAM_EMAIL = "email";

    private final LoginRegisterApi loginRegisterApi;
    private final ActionMapper mapper;

    @Inject
    public ResendActivationUseCase(LoginRegisterApi loginRegisterApi,
                                   ActionMapper mapper) {
        this.loginRegisterApi = loginRegisterApi;
        this.mapper = mapper;
    }

    @Override
    public Observable<ActionPojo> createObservable(RequestParams requestParams) {
        return loginRegisterApi.resendActivation(requestParams.getParameters()).
                map(mapper);
    }

    public static RequestParams getParam(String email) {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_EMAIL, email);
        return param;
    }
}