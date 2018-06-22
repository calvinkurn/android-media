package com.tokopedia.settingbank.addeditaccount.domain.usecase

import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.settingbank.addeditaccount.domain.mapper.EditBankMapper
import com.tokopedia.settingbank.banklist.data.SettingBankApi
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*

/**
 * @author by nisie on 6/22/18.
 */

class EditBankUseCase(val api: SettingBankApi,
                      val mapper: EditBankMapper) : UseCase<String>() {

    override fun createObservable(requestParams: RequestParams): Observable<String> {

        return api.editBankAccount(requestParams.parameters).map(mapper)
    }

    companion object {

        private val PARAM_ACCOUNT_ID = "account_id"
        private val PARAM_ACCOUNT_NAME = "account_name"
        private val PARAM_ACCOUNT_NUMBER = "account_no"
        private val PARAM_BANK_NAME = "bank_name"
        private val PARAM_OTP_CODE = "otp_code"

        private val PARAM_USER_ID: String = "user_id"
        private val PARAM_DEVICE_ID: String = "device_id"
        private val PARAM_HASH: String = "hash"
        private val PARAM_OS_TYPE: String = "os_type"
        private val PARAM_DEVICE_TIME: String = "device_time"

        private val OS_TYPE_ANDROID: String = "1"

        fun getParam(
                userId: String,
                deviceId: String,
                accountId: String,
                accountName: String,
                accountNumber: String,
                bankName: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            val hash = AuthUtil.md5("$userId~$deviceId")

            requestParams.putString(PARAM_ACCOUNT_ID, accountId)
            requestParams.putString(PARAM_ACCOUNT_NAME, accountName)
            requestParams.putString(PARAM_ACCOUNT_NUMBER, accountNumber)
            requestParams.putString(PARAM_BANK_NAME, bankName)
            requestParams.putString(PARAM_OTP_CODE, "124553")

            requestParams.putString(PARAM_USER_ID, userId)
            requestParams.putString(PARAM_DEVICE_ID, deviceId)
            requestParams.putString(PARAM_HASH, hash)
            requestParams.putString(PARAM_OS_TYPE, OS_TYPE_ANDROID)
            requestParams.putString(PARAM_DEVICE_TIME, (Date().time / 1000).toString())

            return requestParams
        }
    }

}