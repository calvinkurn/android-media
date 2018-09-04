package com.tokopedia.settingbank.choosebank.domain.usecase

import com.raizlabs.android.dbflow.config.FlowManager
import com.tokopedia.bankdb.Bank
import com.tokopedia.bankdb.BankFlowDatabase
import com.tokopedia.settingbank.choosebank.data.BankListApi
import com.tokopedia.settingbank.choosebank.domain.mapper.GetBankListWSMapper
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankListViewModel
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * @author by nisie on 7/2/18.
 */
class GetBankListWSUseCase(val api: BankListApi,
                           val mapper: GetBankListWSMapper) : UseCase<BankListViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<BankListViewModel> {
        return api.getBankList(requestParams.parameters)
                .map(mapper)
                .map { t -> saveToDB(t, requestParams) }

    }

    private fun saveToDB(bankListViewModel: BankListViewModel, requestParams: RequestParams): BankListViewModel {

        val database = FlowManager.getDatabase(BankFlowDatabase.NAME).writableDatabase
        database.beginTransaction()
        try {
            val bankList = mapToDBModel(bankListViewModel.list!!)
            for (bank in bankList) {
                bank.setBankPage(requestParams.parameters[PARAM_PAGE] as Int)
                bank.save()
            }
            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
        }

        return bankListViewModel
    }

    private fun mapToDBModel(list: ArrayList<BankViewModel>): List<Bank> {
        val listBank = ArrayList<Bank>()

        for (bankModel: BankViewModel in list) {
            val bank = Bank()
            bank.setBankId(bankModel.bankId)
            bank.setBankName(bankModel.bankName)
            listBank.add(bank)
        }
        return listBank
    }


    companion object {

        private val PARAM_PROFILE_USER_ID: String = "profile_user_id"
        val PARAM_PAGE: String = "page"
        val PARAM_KEYWORD: String = "keyword"

        fun getParam(
                keyword: String,
                page: Int,
                userId: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()

            requestParams.putString(PARAM_PROFILE_USER_ID, userId)
            requestParams.putInt(PARAM_PAGE, page)
            requestParams.putString(PARAM_KEYWORD, keyword)

            return requestParams
        }
    }
}