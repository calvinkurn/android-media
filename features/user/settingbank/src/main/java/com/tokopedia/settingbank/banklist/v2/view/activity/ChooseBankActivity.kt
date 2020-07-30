package com.tokopedia.settingbank.banklist.v2.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.addeditaccount.view.activity.AddEditBankActivity
import com.tokopedia.settingbank.banklist.v2.di.DaggerSettingBankComponent
import com.tokopedia.settingbank.banklist.v2.di.SettingBankComponent
import com.tokopedia.settingbank.banklist.v2.di.SettingBankModule
import com.tokopedia.settingbank.banklist.v2.domain.Bank
import com.tokopedia.settingbank.banklist.v2.util.SettingBankRemoteConfig
import com.tokopedia.settingbank.banklist.v2.view.fragment.OnBankSelectedListener
import com.tokopedia.settingbank.banklist.v2.view.fragment.SelectBankFragment
import com.tokopedia.settingbank.banklist.v2.view.widgets.CloseableBottomSheetFragment

class ChooseBankActivity : BaseSimpleActivity(), HasComponent<SettingBankComponent>,
        OnBankSelectedListener, CloseableBottomSheetFragment.ClosableCallback {

    private lateinit var bankListBottomSheet: CloseableBottomSheetFragment

    override fun getNewFragment(): Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideTitleAndHomeButton()
        if (SettingBankRemoteConfig.instance(this).isOldFlowEnabled()) {
            startOldFlowAddBank()
            return
        }
        openChooseBankBottomSheet()
    }

    private fun startOldFlowAddBank(){
        val newIntent = Intent(this, AddEditBankActivity::class.java)
        intent.extras?.let {
            newIntent.putExtras(it)
        }
        startActivity(newIntent)
        finish()
    }

    private fun hideTitleAndHomeButton() {
        supportActionBar?.setDisplayShowCustomEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = ""
    }

    private fun openChooseBankBottomSheet() {
        bankListBottomSheet = CloseableBottomSheetFragment.newInstance(SelectBankFragment(),
                true,
                getString(R.string.sbank_choose_a_bank),
                this,
                CloseableBottomSheetFragment.STATE_FULL)
        bankListBottomSheet.show(supportFragmentManager, "")
    }

    override fun getComponent(): SettingBankComponent {
        return DaggerSettingBankComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .settingBankModule(SettingBankModule(this))
                .build()
    }

    override fun onBankSelected(bank: Bank) {
        startActivity(AddBankActivity.createIntent(this, bank))
        finish()
    }

    override fun onCloseClick(bottomSheetFragment: BottomSheetDialogFragment) {
        finish()
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, ChooseBankActivity::class.java)
        }
    }
}