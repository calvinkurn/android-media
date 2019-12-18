package com.tokopedia.settingbank.banklist.v2.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.settingbank.banklist.v2.di.DaggerSettingBankComponent
import com.tokopedia.settingbank.banklist.v2.di.SettingBankComponent
import com.tokopedia.settingbank.banklist.v2.domain.BankAccount
import com.tokopedia.settingbank.banklist.v2.view.fragment.ARG_ACCOUNT_TYPE
import com.tokopedia.settingbank.banklist.v2.view.fragment.ARG_BANK_ACCOUNT_DATA
import com.tokopedia.settingbank.banklist.v2.view.fragment.ARG_KYC_NAME
import com.tokopedia.settingbank.banklist.v2.view.fragment.AccountConfirmFragment

class AccountDocumentActivity : BaseSimpleActivity(), HasComponent<SettingBankComponent> {

    override fun getComponent(): SettingBankComponent = DaggerSettingBankComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication)
                    .baseAppComponent).build()

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return AccountConfirmFragment().apply {
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
        return AccountConfirmFragment::class.java.name
    }

}