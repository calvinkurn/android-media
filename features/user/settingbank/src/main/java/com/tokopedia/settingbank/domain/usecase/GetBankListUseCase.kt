package com.tokopedia.settingbank.domain.usecase

import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.settingbank.data.SettingBankApi
import com.tokopedia.settingbank.domain.mapper.GetBankListMapper
import com.tokopedia.settingbank.view.viewmodel.BankAccountListViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*

/**
 * @author by nisie on 6/8/18.
 */
class GetBankListUseCase(val api: SettingBankApi,
                         val mapper: GetBankListMapper) : UseCase<BankAccountListViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<BankAccountListViewModel> {

        return api.getBankAccountList(requestParams.parameters).map(mapper)
    }

    companion object {

        private val PARAM_PROFILE_USER_ID: String = "profile_user_id"
        private val PARAM_PAGE: String = "page"

        private val PARAM_USER_ID: String = "user_id"
        private val PARAM_DEVICE_ID: String = "device_id"
        private val PARAM_HASH: String = "hash"
        private val PARAM_OS_TYPE: String = "os_type"
        private val PARAM_DEVICE_TIME: String = "device_time"

        private val OS_TYPE_ANDROID: String = "1"

        fun getParam(
                userId: String,
                page: String,
                deviceId: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            val hash = AuthUtil.md5("$userId~$deviceId")

            requestParams.putString(PARAM_PROFILE_USER_ID, userId)
            requestParams.putString(PARAM_PAGE, page)

            requestParams.putString(PARAM_USER_ID, userId)
            requestParams.putString(PARAM_DEVICE_ID, deviceId)
            requestParams.putString(PARAM_HASH, hash)
            requestParams.putString(PARAM_OS_TYPE, OS_TYPE_ANDROID)
            requestParams.putString(PARAM_DEVICE_TIME, (Date().time / 1000).toString())

            return requestParams
        }
    }

}