package com.tokopedia.withdraw.auto_withdrawal.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.header.HeaderUnify
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.auto_withdrawal.di.component.AutoWithdrawalComponent
import com.tokopedia.withdraw.auto_withdrawal.di.component.DaggerAutoWithdrawalComponent
import com.tokopedia.withdraw.auto_withdrawal.domain.model.Schedule
import com.tokopedia.withdraw.auto_withdrawal.presentation.adapter.ScheduleChangeListener
import com.tokopedia.withdraw.auto_withdrawal.presentation.fragment.AutoWithdrawalSettingsFragment
import kotlinx.android.synthetic.main.swd_activity_auto_withdrawal.*

class AutoWithdrawalActivity : BaseSimpleActivity(), HasComponent<AutoWithdrawalComponent>,
        ScheduleChangeListener {

    private lateinit var autoWithdrawalComponent: AutoWithdrawalComponent

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        auto_wd_header.isShowBackButton = true
        return AutoWithdrawalSettingsFragment.getInstance(bundle)
    }

    override fun getLayoutRes() = R.layout.swd_activity_auto_withdrawal

    override fun getToolbarResourceID() = R.id.auto_wd_header

    override fun getParentViewResourceID(): Int = R.id.auto_wd_view

    override fun getComponent(): AutoWithdrawalComponent {
        if (!::autoWithdrawalComponent.isInitialized)
            autoWithdrawalComponent = DaggerAutoWithdrawalComponent.builder()
                    .baseAppComponent((applicationContext as BaseMainApplication)
                            .baseAppComponent).build()
        return autoWithdrawalComponent
    }

    fun getHeader(): HeaderUnify {
        return auto_wd_header
    }

    override fun onScheduleSelected(schedule: Schedule) {
        supportFragmentManager.findFragmentByTag(TAG_AUTO_WITHDRAWAL_FRAGMENT)?.let { fragment ->
            if (fragment is ScheduleChangeListener)
                fragment.onScheduleSelected(schedule)
        }
    }

    override fun getTagFragment(): String {
        return TAG_AUTO_WITHDRAWAL_FRAGMENT
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.findFragmentByTag(TAG_AUTO_WITHDRAWAL_FRAGMENT)?.let { fragment ->
            if (fragment is AutoWithdrawalSettingsFragment) {
                if (resultCode == Activity.RESULT_OK) {
                    when (requestCode) {
                        AutoWithdrawalSettingsFragment.BANK_SETTING_REQUEST_CODE -> {
                            data?.let {
                                fragment.refreshBankAccountList()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getScreenName(): String = SCREEN_NAME

    companion object {
        const val SCREEN_NAME = "Auto Withdrawal Settings"
        const val TAG_AUTO_WITHDRAWAL_FRAGMENT = "tag_auto_withdrawal_fragment"
    }
}