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
import com.tokopedia.settingbank.domain.model.Bank
import com.tokopedia.settingbank.view.fragment.OnBankSelectedListener
import com.tokopedia.settingbank.view.fragment.SelectBankFragment

class ChooseBankActivity : BaseSimpleActivity(), HasComponent<SettingBankComponent>,
        OnBankSelectedListener{


    private lateinit var settingBankComponent: SettingBankComponent

    override fun getNewFragment(): Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideTitleAndHomeButton()
        openChooseBankBottomSheet()
    }

    private fun hideTitleAndHomeButton() {
        supportActionBar?.setDisplayShowCustomEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = ""
    }

    private fun openChooseBankBottomSheet() {
        SelectBankFragment.showChooseBankBottomSheet(this, supportFragmentManager).setOnDismissListener {
            finish()
        }
    }

    override fun getComponent(): SettingBankComponent {
        if (!::settingBankComponent.isInitialized) {
            settingBankComponent = DaggerSettingBankComponent.builder()
                    .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                    .settingBankModule(SettingBankModule(this))
                    .build()
        }
        return settingBankComponent
    }

    override fun onBankSelected(bank: Bank) {
        startActivity(AddBankActivity.createIntent(this, bank))
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, ChooseBankActivity::class.java)
        }
    }
}