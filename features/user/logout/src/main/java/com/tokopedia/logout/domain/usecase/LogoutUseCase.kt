package com.tokopedia.logout.domain.usecase

import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.logout.data.LogoutApi
import com.tokopedia.logout.domain.mapper.LogoutMapper
import com.tokopedia.logout.domain.model.LogoutDomain
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSession
import rx.Observable
import java.util.*

/**
 * @author by nisie on 5/30/18.
 */
open class LogoutUseCase(private val api: LogoutApi,
                    private val mapper: LogoutMapper,
                    private val userSession: UserSession) : UseCase<LogoutDomain>() {

    override fun createObservable(requestParams: RequestParams): Observable<LogoutDomain> {

        return api.logout(requestParams.parameters)
                .map(mapper)
                .doOnNext { removeSession() }
    }

    private fun removeSession() {
        userSession.logoutSession()
    }

    companion object {

        private val PARAM_USER_ID: String = "user_id"
        private val PARAM_DEVICE_ID: String = "device_id"
        private val PARAM_HASH: String = "hash"
        private val PARAM_OS_TYPE: String = "os_type"
        private val PARAM_DEVICE_TIME: String = "device_time"

        private val OS_TYPE_ANDROID: String = "1"

        fun getParam(userSession: UserSession): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            val userId = userSession.userId
            val deviceId = userSession.deviceId
            val hash = AuthUtil.md5("$userId~$deviceId")

            requestParams.putString(PARAM_USER_ID, userId)
            requestParams.putString(PARAM_DEVICE_ID, deviceId)
            requestParams.putString(PARAM_HASH, hash)
            requestParams.putString(PARAM_OS_TYPE, OS_TYPE_ANDROID)
            requestParams.putString(PARAM_DEVICE_TIME, (Date().time / 1000).toString())

            return requestParams
        }
    }
}