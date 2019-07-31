package com.tokopedia.settingbank.addeditaccount.domain.usecase

import com.tokopedia.settingbank.addeditaccount.data.AddEditAccountApi
import com.tokopedia.settingbank.addeditaccount.domain.mapper.ValidateBankMapper
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.ValidateBankViewModel
import com.tokopedia.settingbank.banklist.data.SettingBankApi
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 7/13/18.
 */

class ValidateBankUseCase @Inject constructor(val api: AddEditAccountApi,
                          val mapper: ValidateBankMapper) : UseCase<ValidateBankViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<ValidateBankViewModel> {

        return api.validateBankAccount(requestParams.parameters).map(mapper)
    }

    companion object {

        private val PARAM_ACCOUNT_ID = "acc_id"
        private val PARAM_ACCOUNT_NAME = "acc_name"
        private val PARAM_ACCOUNT_NUMBER = "acc_no"
        private val PARAM_BANK_ID = "bank_id"
        private val PARAM_BANK_NAME = "bank_name"


        fun getParam(
                accountId: String,
                accountName: String,
                accountNumber: String,
                bankId: String,
                bankName : String): RequestParams {

            val requestParams: RequestParams = RequestParams.create()

            if (!accountId.isEmpty()) requestParams.putString(PARAM_ACCOUNT_ID, accountId)
            requestParams.putString(PARAM_ACCOUNT_NAME, accountName)
            requestParams.putString(PARAM_ACCOUNT_NUMBER, accountNumber)
            requestParams.putString(PARAM_BANK_ID, bankId)
            requestParams.putString(PARAM_BANK_NAME, bankName)


            return requestParams
        }
    }

}