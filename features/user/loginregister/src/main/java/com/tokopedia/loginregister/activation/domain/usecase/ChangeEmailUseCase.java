package com.tokopedia.loginregister.activation.domain.usecase;

import com.tokopedia.loginregister.activation.domain.mapper.ActionMapper;
import com.tokopedia.loginregister.activation.domain.pojo.ActionPojo;
import com.tokopedia.loginregister.common.data.LoginRegisterApi;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 10/19/18.
 */
public class ChangeEmailUseCase extends UseCase<ActionPojo> {

    public static final String PARAM_OLD_EMAIL = "old_email";
    public static final String PARAM_NEW_EMAIL = "new_email";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_SEND_EMAIL = "send_email";
    public static final String DEFAULT_SEND_EMAIL = "1";

    private final LoginRegisterApi loginRegisterApi;
    private final ActionMapper mapper;

    @Inject
    public ChangeEmailUseCase(LoginRegisterApi loginRegisterApi,
                                   ActionMapper mapper) {
        this.loginRegisterApi = loginRegisterApi;
        this.mapper = mapper;
    }

    @Override
    public Observable<ActionPojo> createObservable(RequestParams requestParams) {
        return loginRegisterApi.changeEmail(requestParams.getParameters()).
                map(mapper);
    }

    public static RequestParams getParam(String oldEmail, String newEmail, String password) {
        RequestParams params = RequestParams.create();
        params.putString(ChangeEmailUseCase.PARAM_NEW_EMAIL, newEmail);
        params.putString(ChangeEmailUseCase.PARAM_OLD_EMAIL, oldEmail);
        params.putString(ChangeEmailUseCase.PARAM_PASSWORD, password);
        params.putString(ChangeEmailUseCase.PARAM_SEND_EMAIL, ChangeEmailUseCase.DEFAULT_SEND_EMAIL);
        return params;
    }
}
