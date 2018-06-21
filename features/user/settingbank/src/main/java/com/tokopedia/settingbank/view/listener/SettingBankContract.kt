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

        fun showLoadingFull()

        fun hideLoadingFull()

        fun onErrorGetListBankFirstTime(errorMessage: String)

        fun onSuccessGetListBank(bankAccountList: BankAccountListViewModel)

        fun onSuccessSetDefault(adapterPosition: Int, statusMessage: String)

        fun onSuccessDeleteAccount(adapterPosition: Int, statusMessage: String)

        fun onErrorSetDefaultBank(errorMessage: String)

        fun onErrorDeleteAccount(errorMessage: String)

        fun onEmptyList()

        fun showLoadingDialog()

        fun hideLoadingDialog()
        fun showLoadingList()
        fun hideLoadingList()

        fun onErrorGetListBank(errorMessage: String)

    }

    interface Presenter : CustomerPresenter<View> {

        fun getBankListFirstTime()

        fun setMainAccount(adapterPosition: Int, element: BankAccountViewModel?)

        fun deleteAccount(adapterPosition: Int, element: BankAccountViewModel?)

        fun loadMore()
    }
}