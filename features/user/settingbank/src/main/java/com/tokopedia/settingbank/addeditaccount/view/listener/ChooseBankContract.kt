package com.tokopedia.settingbank.addeditaccount.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankListViewModel

/**
 * @author by nisie on 6/22/18.
 */

interface ChooseBankContract {

    interface View : CustomerView {
        fun showLoading()

        fun hideLoading()

        fun getContext(): Context?

        fun onErrorGetBankList(errorMessage: String?)

        fun onSuccessGetBankList(listBank: BankListViewModel)

    }

    interface Presenter : CustomerPresenter<ChooseBankContract.View> {
        fun getBankList(query: String)

    }
}