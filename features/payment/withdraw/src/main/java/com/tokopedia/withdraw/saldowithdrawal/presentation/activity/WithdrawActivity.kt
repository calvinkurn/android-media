package com.tokopedia.withdraw.saldowithdrawal.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.saldowithdrawal.WithdrawAnalytics
import com.tokopedia.withdraw.saldowithdrawal.di.component.DaggerWithdrawComponent
import com.tokopedia.withdraw.saldowithdrawal.di.component.WithdrawComponent
import com.tokopedia.withdraw.saldowithdrawal.domain.model.SubmitWithdrawalResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.model.WithdrawalRequest
import com.tokopedia.withdraw.saldowithdrawal.presentation.fragment.SaldoWithdrawalFragment
import com.tokopedia.withdraw.saldowithdrawal.presentation.fragment.SuccessFragmentWithdrawal
import com.tokopedia.withdraw.saldowithdrawal.presentation.listener.WithdrawalFragmentCallback
import kotlinx.android.synthetic.main.activity_saldo_withdraw.*
import javax.inject.Inject

/**
 * For navigating to this class
 * @see com.tokopedia.applink.internal.ApplinkConstInternalGlobal
 */
class WithdrawActivity : BaseSimpleActivity(), WithdrawalFragmentCallback, HasComponent<WithdrawComponent?> {


    @Inject
    lateinit var analytics: WithdrawAnalytics

    override fun getLayoutRes() = R.layout.activity_saldo_withdraw

    override fun getToolbarResourceID() = R.id.swd_header

    override fun getParentViewResourceID(): Int = R.id.parent_view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateHeaderTitle(getString(R.string.swd_activity_withdraw))
        initInjector()
    }

    private fun initInjector() {
        component.inject(this)
    }

    override fun getScreenName(): String {
        return WithdrawAnalytics.SCREEN_WITHDRAW
    }

    private fun updateHeaderTitle(title: String) {
        swd_header.headerTitle = title
    }

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return SaldoWithdrawalFragment.getFragmentInstance(bundle)
    }

    override fun onBackPressed() {
        setResultIfSuccessFragment()
        analytics.eventClickBackArrow()
        super.onBackPressed()
    }

    private fun setResultIfSuccessFragment() {
        if (supportFragmentManager.findFragmentByTag(TAG_SUCCESS_FRAGMENT) != null) {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
        }
    }

    override fun openSuccessFragment(withdrawalRequest: WithdrawalRequest,
                                     submitWithdrawalResponse: SubmitWithdrawalResponse) {
        updateHeaderTitle(getString(R.string.swd_success_page_title))
        val successFragment = SuccessFragmentWithdrawal.getInstance(withdrawalRequest, submitWithdrawalResponse)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.parent_view, successFragment, TAG_SUCCESS_FRAGMENT)
        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun getComponent(): WithdrawComponent {
        return DaggerWithdrawComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    companion object {
        const val TAG_SUCCESS_FRAGMENT = "TAG_SUCCESS_FRAGMENT"
    }
}