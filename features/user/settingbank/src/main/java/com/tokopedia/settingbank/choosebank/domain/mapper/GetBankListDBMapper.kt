package com.tokopedia.settingbank.choosebank.domain.mapper

import com.tokopedia.core.database.model.Bank
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankListViewModel
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel
import rx.Observable

/**
 * @author by nisie on 7/2/18.
 */

class GetBankListDBMapper {
    fun map(data: List<Bank>): Observable<BankListViewModel>? {

        val bankList: ArrayList<BankViewModel> = ArrayList()

        for (bankData in data) {
            bankList.add(mapToBank(bankData))
        }
        return Observable.just(BankListViewModel(bankList, false))
    }

    private fun mapToBank(bankData: Bank): BankViewModel {
        return BankViewModel(bankData.bankId,
                bankData.bankName,
                false)
    }
}

