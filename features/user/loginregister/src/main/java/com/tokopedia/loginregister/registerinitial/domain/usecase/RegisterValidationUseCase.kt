package com.tokopedia.loginregister.registerinitial.domain.usecase

import com.tokopedia.loginregister.common.data.LoginRegisterApi
import com.tokopedia.loginregister.registerinitial.domain.mapper.RegisterValidationMapper
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterValidationPojo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by alvinatin on 12/06/18.
 */
class RegisterValidationUseCase @Inject constructor(private val loginRegisterApi: LoginRegisterApi,
                                                    private val mapper: RegisterValidationMapper) : UseCase<RegisterValidationPojo?>() {
    override fun createObservable(requestParams: RequestParams): Observable<RegisterValidationPojo?>? {
        return loginRegisterApi.validateRegister(requestParams.parameters)
                .map<RegisterValidationPojo>(mapper)
    }

    companion object {
        private const val PARAM_ID = "id"

        /**
         * @param id either email or phone number
         * @return params
         */
        fun createValidateRegisterParam(id: String?): RequestParams {
            val param = RequestParams.create()
            param.putString(PARAM_ID, id)
            return param
        }
    }

}