package com.tokopedia.settingbank.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.view.listener.SettingBankContract
import com.tokopedia.settingbank.view.presenter.SettingBankPresenter
import com.tokopedia.settingbank.common.analytics.SettingBankAnalytics
import com.tokopedia.settingbank.common.di.SettingBankDependencyInjector
import com.tokopedia.settingbank.view.viewmodel.BankAccountListViewModel

/**
 * @author by nisie on 6/7/18.
 */

class SettingBankFragment : SettingBankContract.View, BaseDaggerFragment() {

    lateinit var presenter: SettingBankPresenter

    companion object {

        fun newInstance(): SettingBankFragment {
            return SettingBankFragment()
        }
    }


    override fun getScreenName(): String {
        return SettingBankAnalytics.SCREEN_NAME
    }

    override fun initInjector() {
        presenter = SettingBankDependencyInjector.Companion.inject(activity.applicationContext)
        presenter.attachView(this)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_setting_bank, container, false)
        return view
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBankList()
    }

    private fun getBankList() {
        presenter.getBankList()
    }

    override fun onErrorGetListBank(errorMessage: String) {
        NetworkErrorHelper.showEmptyState(context, view) {
            getBankList()
        }
    }

    override fun onSuccessGetListBank(bankAccountList: BankAccountListViewModel) {

    }

}