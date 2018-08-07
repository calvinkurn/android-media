package com.tokopedia.settingbank.banklist.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.settingbank.banklist.view.viewmodel.BankAccountListViewModel
import com.tokopedia.settingbank.banklist.view.viewmodel.BankAccountViewModel

/**
 * @author by nisie on 6/7/18.
 */

interface SettingBankContract {

    interface View : CustomerView {
        fun getContext(): Context?

        fun showLoadingFull()

        fun hideLoadingFull()

        fun onErrorGetListBankFirstTime(errorMessage: String)

        fun onSuccessGetListBank(bankAccountList: BankAccountListViewModel)

        fun onSuccessSetDefault(adapterPosition: Int)

        fun onSuccessDeleteAccount(adapterPosition: Int)

        fun onErrorSetDefaultBank(errorMessage: String)

        fun onErrorDeleteAccount(errorMessage: String)

        fun onEmptyList(enableAddButton: Boolean, reason: String)

        fun showLoadingList()

        fun hideLoadingList()

        fun onErrorGetListBank(errorMessage: String)

        fun onSuccessRefresh(bankAccountList: BankAccountListViewModel, resultMessage: String)

    }

    interface Presenter : CustomerPresenter<View> {

        fun getBankListFirstTime()

        fun setMainAccount(adapterPosition: Int, element: BankAccountViewModel?)

        fun deleteAccount(adapterPosition: Int, element: BankAccountViewModel?)

        fun loadMore()

        fun isMsisdnVerified(): Boolean

        fun refreshBankList(resultMessage: String)
    }
}