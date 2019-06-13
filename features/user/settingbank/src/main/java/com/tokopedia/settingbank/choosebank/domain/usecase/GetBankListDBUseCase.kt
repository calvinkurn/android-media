package com.tokopedia.settingbank.choosebank.domain.usecase

import com.tokopedia.settingbank.choosebank.data.database.BankDao
import com.tokopedia.settingbank.choosebank.data.database.BankTable
import com.tokopedia.settingbank.choosebank.domain.mapper.GetBankListDBMapper
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankListViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 7/2/18.
 */
class GetBankListDBUseCase @Inject constructor(private val getBankListMapper: GetBankListDBMapper,
                           val bankDao: BankDao) : UseCase<BankListViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<BankListViewModel> {
        return Observable.just(requestParams.parameters[PARAM_KEYWORD])
                .flatMap { t -> getDataFromDB(t as String) }
    }

    private fun getDataFromDB(query: String): Observable<out BankListViewModel>? {
        val bankList: List<BankTable>

        if (query == "")
            bankList = bankDao.findAllBank()
        else {
            bankList = bankDao.findBankByName("%$query%")
        }
        return getBankListMapper.map(bankList, query)

    }


    companion object {

        val PARAM_KEYWORD: String = "keyword"

        fun getParam(keyword: String): RequestParams {
            val requestParams: RequestParams = RequestParams.create()
            requestParams.putString(PARAM_KEYWORD, keyword)
            return requestParams
        }
    }
}