package com.tokopedia.settingbank.banklist.domain.usecase

import com.tokopedia.settingbank.banklist.data.SettingBankApi
import com.tokopedia.settingbank.banklist.domain.mapper.GetBankAccountListMapper
import com.tokopedia.settingbank.banklist.view.viewmodel.BankAccountListViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 6/8/18.
 */
class GetBankAccountListUseCase @Inject constructor(val api: SettingBankApi,
                                val mapper: GetBankAccountListMapper) : UseCase<BankAccountListViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<BankAccountListViewModel> {

        return api.getBankAccountList(requestParams.parameters).map(mapper)
    }

    companion object {

        private val PARAM_PAGE: String = "page"
        private val PARAM_USER_ID: String = "user_id"

        fun getParam(
                userId: String,
                page: Int): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            requestParams.putInt(PARAM_PAGE, page)
            requestParams.putString(PARAM_USER_ID, userId)

            return requestParams
        }
    }

}