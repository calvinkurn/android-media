package com.tokopedia.settingbank.banklist.v2.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.settingbank.addeditaccount.view.activity.AddEditBankActivity
import com.tokopedia.settingbank.banklist.v2.di.DaggerSettingBankComponent
import com.tokopedia.settingbank.banklist.v2.di.SettingBankComponent
import com.tokopedia.settingbank.banklist.v2.domain.Bank
import com.tokopedia.settingbank.banklist.v2.util.SettingBankRemoteConfig
import com.tokopedia.settingbank.banklist.v2.view.fragment.AddBankFragment
import com.tokopedia.settingbank.banklist.v2.view.fragment.OnBankSelectedListener

class AddBankActivity : BaseSimpleActivity(), HasComponent<SettingBankComponent>, OnBankSelectedListener {

    override fun getComponent(): SettingBankComponent = DaggerSettingBankComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication)
                    .baseAppComponent).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SettingBankRemoteConfig.instance(this).isOldFlowEnabled()) {
            val newIntent = Intent(this, AddEditBankActivity::class.java)
            intent.extras?.let {
                newIntent.putExtras(it)
            }
            startActivity(newIntent)
            finish()
            return
        }
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return AddBankFragment().apply {
            arguments = bundle
        }
    }

    companion object {
        const val ARG_BANK_DATA = "arg_bank_data"
        fun createIntent(context: Context, bank: Bank): Intent {
            return Intent(context, AddBankActivity::class.java).apply {
                putExtra(ARG_BANK_DATA, bank)
            }
        }
    }

    override fun getTagFragment(): String {
        return AddBankFragment::class.java.name
    }

    override fun onBankSelected(bank: Bank) {
        val fragment = supportFragmentManager.findFragmentByTag(tagFragment)
        fragment?.let {
            if (fragment is AddBankFragment) {
                fragment.closeBottomSheet()
                fragment.onBankSelected(bank)
            }
        }
    }

}