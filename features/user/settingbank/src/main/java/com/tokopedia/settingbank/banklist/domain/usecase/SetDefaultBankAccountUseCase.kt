package com.tokopedia.settingbank.banklist.domain.usecase

import com.tokopedia.settingbank.banklist.data.SettingBankApi
import com.tokopedia.settingbank.banklist.domain.mapper.SetDefaultBankAccountMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 6/20/18.
 */
class SetDefaultBankAccountUseCase @Inject constructor(val api: SettingBankApi,
                                   val mapper: SetDefaultBankAccountMapper) : UseCase<Boolean>() {

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {

        return api.setDefaultBank(requestParams.parameters).map(mapper)
    }

    companion object {

        private const val PARAM_ACCOUNT_ID: String = "acc_id"

        fun getParam(
                accountId: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            requestParams.putString(PARAM_ACCOUNT_ID, accountId)

            return requestParams
        }
    }

}