package com.tokopedia.settingbank.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.settingbank.view.viewmodel.BankAccountListViewModel
import com.tokopedia.settingbank.view.viewmodel.BankAccountViewModel

/**
 * @author by nisie on 6/7/18.
 */

interface SettingBankContract {

    interface View : CustomerView {
        fun getContext(): Context

        fun showLoading()

        fun hideLoading()

        fun onErrorGetListBank(errorMessage: String)

        fun onSuccessGetListBank(bankAccountList: BankAccountListViewModel)

        fun onSuccessSetMain(adapterPosition: Int)

    }

    interface Presenter : CustomerPresenter<View> {

        fun getBankList()

        fun setMainAccount(adapterPosition: Int, element: BankAccountViewModel?)
    }
}