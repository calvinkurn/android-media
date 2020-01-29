package com.tokopedia.settingbank.banklist.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RestrictTo
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.addeditaccount.view.activity.AddEditBankActivity
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.BankFormModel
import com.tokopedia.settingbank.banklist.analytics.SettingBankAnalytics
import com.tokopedia.settingbank.banklist.view.adapter.BankAccountAdapter
import com.tokopedia.settingbank.banklist.view.adapter.BankAccountTypeFactoryImpl
import com.tokopedia.settingbank.banklist.view.listener.BankAccountPopupListener
import com.tokopedia.settingbank.banklist.view.listener.EmptyBankAccountListener
import com.tokopedia.settingbank.banklist.view.listener.SettingBankContract
import com.tokopedia.settingbank.banklist.view.presenter.SettingBankPresenter
import com.tokopedia.settingbank.banklist.view.viewmodel.BankAccountListViewModel
import com.tokopedia.settingbank.banklist.view.viewmodel.BankAccountViewModel
import com.tokopedia.settingbank.banklist.di.DaggerSettingBankComponent
import com.tokopedia.settingbank.banklist.di.SettingBankComponent
import com.tokopedia.util.FetchingIdlingResource
import kotlinx.android.synthetic.main.fragment_setting_bank.*
import javax.inject.Inject

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

    override fun onSuccessGetListBank(bankAccountList: BankAccountListViewModel) {
        FetchingIdlingResource.complete()
        super.onSuccessGetListBank(bankAccountList)
    }
}