package com.tokopedia.changepassword.domain

import com.tokopedia.changepassword.data.ChangePasswordApi
import com.tokopedia.changepassword.domain.mapper.ChangePasswordMapper
import com.tokopedia.changepassword.domain.model.ChangePasswordDomain
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * @author by nisie on 7/25/18.
 */
class ChangePasswordUseCase(val api: ChangePasswordApi,
                                                val mapper: ChangePasswordMapper)
    : UseCase<ChangePasswordDomain>() {


    override fun createObservable(requestParams: RequestParams): Observable<ChangePasswordDomain> {

        return api.changePassword(requestParams.parameters)
                .map(mapper)
    }

    companion object {

        private val PARAM_OLD_PASSWORD: String = "password"
        private val PARAM_NEW_PASSWORD: String = "new_password"
        private val PARAM_CONFIRM_PASSWORD: String = "confirm_password"

        private val PARAM_OS_TYPE: String = "os_type"
        private val OS_TYPE_ANDROID: String = "1"

        fun getParam(
                oldPassword: String,
                newPassword: String,
                confirmPassword: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            requestParams.putString(PARAM_OLD_PASSWORD, oldPassword)
            requestParams.putString(PARAM_NEW_PASSWORD, newPassword)
            requestParams.putString(PARAM_CONFIRM_PASSWORD, confirmPassword)

            requestParams.putString(PARAM_OS_TYPE, OS_TYPE_ANDROID)

            return requestParams
        }
    }
}