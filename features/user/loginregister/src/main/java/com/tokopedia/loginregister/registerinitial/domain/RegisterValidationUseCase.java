package com.tokopedia.loginregister.registerinitial.domain;

import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterValidationPojo;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author by alvinatin on 12/06/18.
 */

public class RegisterValidationUseCase extends UseCase<RegisterValidationPojo> {

    public static final String PARAM_ID = "id";

//    private final RegisterValidationSource registerValidationSource;

//    @Inject
//    public RegisterValidationUseCase(RegisterValidationSource registerValidationSource) {
//        this.registerValidationSource = registerValidationSource;
//    }

    @Override
    public Observable<RegisterValidationPojo> createObservable(RequestParams requestParams) {
//        return registerValidationSource.validateRegister(requestParams.getParameters());
        return null;
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
