package com.tokopedia.settingbank.choosebank.domain.usecase

import com.tokopedia.settingbank.choosebank.data.database.BankDao
import com.tokopedia.settingbank.choosebank.data.database.BankTable
import com.tokopedia.settingbank.choosebank.data.BankListApi
import com.tokopedia.settingbank.choosebank.domain.mapper.GetBankListWSMapper
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankListViewModel
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 7/2/18.
 */
class GetBankListWSUseCase @Inject constructor(val api: BankListApi,
                           val mapper: GetBankListWSMapper,
                           val bankDao: BankDao) : UseCase<BankListViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<BankListViewModel> {
        return api.getBankList(requestParams.parameters)
                .map(mapper)
                .map { t -> saveToDB(t, requestParams) }

    }

    private fun saveToDB(bankListViewModel: BankListViewModel, requestParams: RequestParams): BankListViewModel {

        val bankList = mapToDBModel(bankListViewModel.list!!)
        for (bank in bankList) {
            bank.bankPage = requestParams.parameters[PARAM_PAGE] as Int
            bankDao.insert(bank)
        }

        return bankListViewModel
    }

    private fun mapToDBModel(list: ArrayList<BankViewModel>): List<BankTable> {
        val listBank = mutableListOf<BankTable>()

        for (bankModel: BankViewModel in list) {
            val bank = BankTable()
            bank.bankId = bankModel.bankId!!
            bank.bankName = bankModel.bankName!!
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