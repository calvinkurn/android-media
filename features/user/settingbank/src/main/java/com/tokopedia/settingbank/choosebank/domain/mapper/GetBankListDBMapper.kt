package com.tokopedia.settingbank.choosebank.domain.mapper

import com.tokopedia.settingbank.choosebank.data.database.BankTable
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankListViewModel
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 7/2/18.
 */

class GetBankListDBMapper @Inject constructor() {
    fun map(data: List<BankTable>, query: String): Observable<BankListViewModel>? {

        val bankList: ArrayList<BankViewModel> = ArrayList()

        for (bankData in data) {
            bankList.add(mapToBank(bankData, query))
        }
        return Observable.just(BankListViewModel(bankList, false))
    }

    private fun mapToBank(bankData: BankTable, query: String): BankViewModel {
        return BankViewModel(bankData.bankId,
                bankData.bankName,
                false,
                query)
    }
}

