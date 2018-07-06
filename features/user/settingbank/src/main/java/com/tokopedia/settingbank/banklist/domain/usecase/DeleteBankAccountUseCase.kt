package com.tokopedia.settingbank.banklist.domain.usecase

import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.settingbank.banklist.data.SettingBankApi
import com.tokopedia.settingbank.banklist.domain.mapper.DeleteBankAccountMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*

/**
 * @author by nisie on 6/20/18.
 */

class DeleteBankAccountUseCase(val api: SettingBankApi,
                               val mapper: DeleteBankAccountMapper) : UseCase<String>() {

    override fun createObservable(requestParams: RequestParams): Observable<String> {

        return api.deleteBank(requestParams.parameters).map(mapper)
    }

    companion object {

        private val PARAM_ACCOUNT_ID: String = "acc_id"

        private val PARAM_USER_ID: String = "user_id"
        private val PARAM_DEVICE_ID: String = "device_id"
        private val PARAM_HASH: String = "hash"
        private val PARAM_OS_TYPE: String = "os_type"
        private val PARAM_DEVICE_TIME: String = "device_time"

        private val OS_TYPE_ANDROID: String = "1"

        fun getParam(
                userId: String,
                accountId: String,
                deviceId: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            val hash = AuthUtil.md5("$userId~$deviceId")

            requestParams.putString(PARAM_ACCOUNT_ID, accountId)

            requestParams.putString(PARAM_USER_ID, userId)
            requestParams.putString(PARAM_DEVICE_ID, deviceId)
            requestParams.putString(PARAM_HASH, hash)
            requestParams.putString(PARAM_OS_TYPE, OS_TYPE_ANDROID)
            requestParams.putString(PARAM_DEVICE_TIME, (Date().time / 1000).toString())

            return requestParams
        }
    }

}