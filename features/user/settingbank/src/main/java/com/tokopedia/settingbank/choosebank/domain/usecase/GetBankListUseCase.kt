package com.tokopedia.settingbank.choosebank.domain.usecase

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankListViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*

/**
 * @author by nisie on 7/3/18.
 */
class GetBankListUseCase(private val getBankListDBUseCase: GetBankListDBUseCase,
                         private val getBankListWSUseCase: GetBankListWSUseCase,
                         private val bankCache: LocalCacheHandler) :
        UseCase<BankListViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<BankListViewModel> {


        return getBankListDBUseCase.createObservable(requestParams)
                .flatMap { t -> checkShouldRefreshData(t, requestParams) }

    }

    private fun checkShouldRefreshData(data: BankListViewModel, requestParams: RequestParams): Observable<BankListViewModel> {
        return if (bankCache.isExpired || data.list!!.isEmpty()) {
            getBankListWSUseCase.createObservable(requestParams)
        } else {
            Observable.just(data)
        }
    }


    companion object {

        private val PARAM_PROFILE_USER_ID: String = "profile_user_id"
        val PARAM_PAGE: String = "page"
        val PARAM_KEYWORD: String = "keyword"


        private val PARAM_USER_ID: String = "user_id"
        private val PARAM_DEVICE_ID: String = "device_id"
        private val PARAM_HASH: String = "hash"
        private val PARAM_OS_TYPE: String = "os_type"
        private val PARAM_DEVICE_TIME: String = "device_time"

        private val OS_TYPE_ANDROID: String = "1"

        fun getParam(
                keyword : String,
                page: Int,
                userId: String,
                deviceId: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            val hash = AuthUtil.md5("$userId~$deviceId")

            requestParams.putString(PARAM_PROFILE_USER_ID, userId)
            requestParams.putInt(PARAM_PAGE, page)
            requestParams.putString(PARAM_KEYWORD, keyword)

            requestParams.putString(PARAM_USER_ID, userId)
            requestParams.putString(PARAM_DEVICE_ID, deviceId)
            requestParams.putString(PARAM_HASH, hash)
            requestParams.putString(PARAM_OS_TYPE, OS_TYPE_ANDROID)
            requestParams.putString(PARAM_DEVICE_TIME, (Date().time / 1000).toString())

            return requestParams
        }
    }
}