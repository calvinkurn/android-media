package com.tokopedia.settingbank.choosebank.domain.usecase

import com.raizlabs.android.dbflow.sql.language.Select
import com.tokopedia.bankdb.Bank
import com.tokopedia.bankdb.Bank_Table

import com.tokopedia.settingbank.choosebank.domain.mapper.GetBankListDBMapper
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankListViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * @author by nisie on 7/2/18.
 */
class GetBankListDBUseCase(private val getBankListMapper: GetBankListDBMapper) : UseCase<BankListViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<BankListViewModel> {
        return Observable.just(requestParams.parameters[PARAM_KEYWORD])
                .flatMap { t -> getDataFromDB(t as String) }
    }

    private fun getDataFromDB(query: String): Observable<out BankListViewModel>? {
        val bankList: List<Bank>

        if (query == "")
            bankList = Select().from(Bank::class.java).queryList()
        else {
            bankList = Select().from(Bank::class.java).where(Bank_Table.bank_name.like("%$query%")).queryList()
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