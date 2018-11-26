package com.tokopedia.loginregister.registerinitial.domain.usecase;

import com.tokopedia.loginregister.common.data.LoginRegisterApi;
import com.tokopedia.loginregister.registerinitial.domain.mapper.RegisterValidationMapper;
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterValidationPojo;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by alvinatin on 12/06/18.
 */

public class RegisterValidationUseCase extends UseCase<RegisterValidationPojo> {

    private static final String PARAM_ID = "id";

    private final LoginRegisterApi loginRegisterApi;
    private final RegisterValidationMapper mapper;

    @Inject
    public RegisterValidationUseCase(LoginRegisterApi loginRegisterApi,
                                     RegisterValidationMapper mapper) {
        this.loginRegisterApi = loginRegisterApi;
        this.mapper = mapper;
    }

    @Override
    public Observable<RegisterValidationPojo> createObservable(RequestParams requestParams) {
        return loginRegisterApi.validateRegister(requestParams.getParameters())
                .map(mapper);
    }

    /**
     * @param id either email or phone number
     * @return params
     */
    public static RequestParams createValidateRegisterParam(String id) {
        RequestParams param = RequestParams.create();
        param.putString(RegisterValidationUseCase.PARAM_ID, id);
        return param;
    }
}
