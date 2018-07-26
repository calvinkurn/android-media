package com.tokopedia.changepassword.domain

import com.tokopedia.changepassword.data.ChangePasswordApi
import com.tokopedia.changepassword.domain.mapper.ChangePasswordMapper
import com.tokopedia.changepassword.domain.model.ChangePasswordDomain
import com.tokopedia.core.app.MainApplication
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.core.network.retrofit.utils.AuthUtil
import com.tokopedia.core.util.SessionHandler
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 7/25/18.
 */
class ChangePasswordUseCase(val api: ChangePasswordApi,
                                                val mapper: ChangePasswordMapper)
    : UseCase<ChangePasswordDomain>() {


    override fun createObservable(requestParams: RequestParams): Observable<ChangePasswordDomain> {

        return api.editPassword(requestParams.parameters)
                .map(mapper)
    }

    companion object {

        private val PARAM_OLD_PASSWORD: String = "password"
        private val PARAM_NEW_PASSWORD: String = "new_password"
        private val PARAM_CONFIRM_PASSWORD: String = "confirm_password"
        private val PARAM_USER_ID: String = "user_id"
        private val PARAM_DEVICE_ID: String = "device_id"
        private val PARAM_HASH: String = "hash"
        private val PARAM_OS_TYPE: String = "os_type"
        private val PARAM_DEVICE_TIME: String = "device_time"

        private val OS_TYPE_ANDROID: String = "1"

        fun getParam(
                oldPassword: String,
                newPassword: String,
                confirmPassword: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            val userId = SessionHandler.getLoginID(MainApplication
                    .getAppContext().applicationContext)
            val deviceId = GCMHandler.getRegistrationId(MainApplication
                    .getAppContext().applicationContext)
            val hash = AuthUtil.md5("$userId~$deviceId")

            requestParams.putString(PARAM_OLD_PASSWORD, oldPassword)
            requestParams.putString(PARAM_NEW_PASSWORD, newPassword)
            requestParams.putString(PARAM_CONFIRM_PASSWORD, confirmPassword)

            requestParams.putString(PARAM_USER_ID, userId)
            requestParams.putString(PARAM_DEVICE_ID, deviceId)
            requestParams.putString(PARAM_HASH, hash)
            requestParams.putString(PARAM_OS_TYPE, OS_TYPE_ANDROID)
            requestParams.putString(PARAM_DEVICE_TIME, (Date().time / 1000).toString())

            return requestParams
        }
    }
}