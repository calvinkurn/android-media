package com.tokopedia.settingbank.choosebank.domain.usecase

import com.raizlabs.android.dbflow.sql.language.Select
import com.tokopedia.core.database.model.Bank
import com.tokopedia.core.database.model.Bank_Table
import com.tokopedia.core.manage.people.address.datamanager.NetworkParam.PARAM_QUERY
import com.tokopedia.settingbank.choosebank.domain.mapper.GetBankListDBMapper
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankListViewModel
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.Subscriber

/**
 * @author by nisie on 7/2/18.
 */
class GetBankListDBUseCase(private val getBankListMapper: GetBankListDBMapper)  : UseCase<BankListViewModel>(){

    override fun createObservable(requestParams: RequestParams): Observable<BankListViewModel> {
        return Observable.just(requestParams.parameters[GetBankListUseCase.Companion.PARAM_KEYWORD])
                .flatMap { t -> getDataFromDB(t as String) }
    }

    private fun getDataFromDB(query: String): Observable<out BankListViewModel>? {
                    val bankList: List<Bank>

        if (query == "")
                bankList = Select().from(Bank::class.java).queryList()
            else {
                bankList = Select().from(Bank::class.java).where(Bank_Table.bank_name.like("%$query%")).queryList()
            }
        return getBankListMapper.map(bankList)

    }

}