package com.tokopedia.settingbank.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.settingbank.di.DaggerSettingBankComponent
import com.tokopedia.settingbank.di.SettingBankComponent
import com.tokopedia.settingbank.di.SettingBankModule
import com.tokopedia.settingbank.domain.model.BankAccount
import com.tokopedia.settingbank.view.fragment.ARG_ACCOUNT_TYPE
import com.tokopedia.settingbank.view.fragment.ARG_BANK_ACCOUNT_DATA
import com.tokopedia.settingbank.view.fragment.ARG_KYC_NAME
import com.tokopedia.settingbank.view.fragment.AccountDocumentFragment

class AccountDocumentActivity : BaseSimpleActivity(), HasComponent<SettingBankComponent> {

    override fun getComponent(): SettingBankComponent = DaggerSettingBankComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .settingBankModule(SettingBankModule(this))
            .build()

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return AccountDocumentFragment().apply {
            arguments = bundle
        }
    }

    companion object {
        fun createIntent(context: Context, bankAccount: BankAccount, accountType: Int, kycName: String?): Intent {
            return Intent(context, AccountDocumentActivity::class.java).apply {
                putExtra(ARG_BANK_ACCOUNT_DATA, bankAccount)
                putExtra(ARG_ACCOUNT_TYPE, accountType)
                kycName?.let {
                    putExtra(ARG_KYC_NAME, kycName)
                }
            }
        }
    }

    override fun getTagFragment(): String {
        return AccountDocumentFragment::class.java.name
    }

}