package com.tokopedia.loginregister.registeremail.domain;

import com.tokopedia.loginregister.common.data.LoginRegisterApi;
import com.tokopedia.loginregister.registeremail.domain.mapper.RegisterEmailMapper;
import com.tokopedia.loginregister.registeremail.domain.pojo.RegisterEmailPojo;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nisie on 4/13/17.
 */

public class RegisterEmailUseCase extends UseCase<RegisterEmailPojo> {

    public static final String PARAM_CONFIRM_PASSWORD = "confirm_password";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_FULLNAME = "full_name";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_IS_AUTO_VERIFY = "is_auto_verify";

    private final LoginRegisterApi api;
    private final RegisterEmailMapper mapper;

    @Inject
    public RegisterEmailUseCase(LoginRegisterApi api, RegisterEmailMapper mapper) {
        this.api = api;
        this.mapper= mapper;
    }

    @Override
    public Observable<RegisterEmailPojo> createObservable(RequestParams requestParams) {
        return api.registerEmail(requestParams.getParameters()).
                map(mapper);
    }

    public static RequestParams getParam(String email, String name, String password, String confirmPassword, int isAutoVerify) {
        RequestParams param = RequestParams.create();
        param.putString(RegisterEmailUseCase.PARAM_EMAIL, email);
        param.putString(RegisterEmailUseCase.PARAM_FULLNAME, name);
        param.putString(RegisterEmailUseCase.PARAM_PASSWORD, password);
        param.putString(RegisterEmailUseCase.PARAM_CONFIRM_PASSWORD, confirmPassword);
        param.putInt(RegisterEmailUseCase.PARAM_IS_AUTO_VERIFY, isAutoVerify);
        return param;
    }
}