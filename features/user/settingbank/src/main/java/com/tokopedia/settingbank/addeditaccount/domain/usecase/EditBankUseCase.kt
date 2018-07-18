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
                      val mapper: EditBankMapper) : UseCase<Boolean>() {

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {

        return api.editBankAccount(requestParams.parameters).map(mapper)
    }

    companion object {

        private val PARAM_ACCOUNT_ID = "acc_id"
        private val PARAM_ACCOUNT_NAME = "acc_name"
        private val PARAM_ACCOUNT_NUMBER = "acc_no"
        private val PARAM_BANK_NAME = "bank_name"
        private val PARAM_BANK_ID = "bank_id"

        fun getParam(
                accountId: String,
                accountName: String,
                accountNumber: String,
                bankName: String,
                bankId : String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            requestParams.putString(PARAM_ACCOUNT_ID, accountId)
            requestParams.putString(PARAM_ACCOUNT_NAME, accountName)
            requestParams.putString(PARAM_ACCOUNT_NUMBER, accountNumber)
            requestParams.putString(PARAM_BANK_NAME, bankName)
            requestParams.putString(PARAM_BANK_ID, bankId)
            return requestParams
        }
    }

}