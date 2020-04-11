package com.tokopedia.settingbank.banklist.view.fragment

import android.os.Bundle
import androidx.annotation.RestrictTo
import com.tokopedia.settingbank.banklist.di.SettingBankComponent
import com.tokopedia.settingbank.banklist.view.viewmodel.BankAccountListViewModel
import com.tokopedia.settingbank.util.FetchingIdlingResource

/**
 * @author by nisie on 6/7/18.
 */

class DebugSettingBankFragment : SettingBankFragment() {
    override fun getBankList() {
        FetchingIdlingResource.begin()
        super.getBankList()
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun reInitInjector(component: SettingBankComponent){
        component.inject(this)
        presenter.attachView(this)
    }

    override fun onErrorGetListBank(errorMessage: String) {
        FetchingIdlingResource.complete()
        super.onErrorGetListBank(errorMessage)
    }
    override fun onErrorGetListBankFirstTime(errorMessage: String) {
        FetchingIdlingResource.complete()
        super.onErrorGetListBankFirstTime(errorMessage)
    }

    override fun onSuccessGetListBank(bankAccountList: BankAccountListViewModel) {
        FetchingIdlingResource.complete()
        super.onSuccessGetListBank(bankAccountList)
    }
}