package com.tokopedia.settingbank.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.settingbank.di.DaggerSettingBankComponent
import com.tokopedia.settingbank.di.SettingBankComponent
import com.tokopedia.settingbank.di.SettingBankModule
import com.tokopedia.settingbank.domain.model.Bank
import com.tokopedia.settingbank.view.fragment.AddBankFragment
import com.tokopedia.settingbank.view.fragment.OnBankSelectedListener

class AddBankActivity : BaseSimpleActivity(), HasComponent<SettingBankComponent>,
        OnBankSelectedListener {

    private lateinit var settingBankComponent: SettingBankComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSecureWindowFlag()
    }

    private fun setSecureWindowFlag() {
        runOnUiThread {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }


    override fun getComponent(): SettingBankComponent {
        if (!::settingBankComponent.isInitialized)
            settingBankComponent = DaggerSettingBankComponent.builder()
                    .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                    .settingBankModule(SettingBankModule(this))
                    .build()
        return settingBankComponent
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

    override fun getTagFragment(): String {
        return AddBankFragment::class.java.name
    }


    override fun onBankSelected(bank: Bank) {
        val fragment = supportFragmentManager.findFragmentByTag(tagFragment)
        fragment?.let {
            if (fragment is AddBankFragment) {
                fragment.onBankSelected(bank)
            }
        }
    }

    companion object {
        const val ARG_BANK_DATA = "arg_bank_data"
        fun createIntent(context: Context, bank: Bank): Intent {
            return Intent(context, AddBankActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
                putExtra(ARG_BANK_DATA, bank)
            }
        }
    }

}