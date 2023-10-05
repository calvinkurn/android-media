package com.tokopedia.withdraw.saldowithdrawal.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.withdraw.R
import com.tokopedia.unifycomponents.R as RUnifyComp
import com.tokopedia.withdraw.saldowithdrawal.analytics.WithdrawAnalytics
import com.tokopedia.withdraw.saldowithdrawal.di.component.DaggerWithdrawComponent
import com.tokopedia.withdraw.saldowithdrawal.di.component.WithdrawComponent
import com.tokopedia.withdraw.saldowithdrawal.domain.model.SubmitWithdrawalResponse
import com.tokopedia.withdraw.saldowithdrawal.domain.model.WithdrawalRequest
import com.tokopedia.withdraw.saldowithdrawal.presentation.fragment.SaldoWithdrawalFragment
import com.tokopedia.withdraw.saldowithdrawal.presentation.fragment.ThankYouFragmentWithdrawal
import com.tokopedia.withdraw.saldowithdrawal.presentation.listener.WithdrawalFragmentCallback
import com.tokopedia.withdraw.saldowithdrawal.presentation.listener.WithdrawalJoinRPCallback
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.SubmitWithdrawalViewModel
import kotlinx.android.synthetic.main.swd_activity_saldo_withdraw.*
import javax.inject.Inject

/**
 * For navigating to this class
 * @see com.tokopedia.applink.internal.ApplinkConstInternalGlobal
 */
class WithdrawActivity : BaseSimpleActivity(), WithdrawalFragmentCallback,
        HasComponent<WithdrawComponent?>, WithdrawalJoinRPCallback {

    @Inject
    lateinit var analytics: dagger.Lazy<WithdrawAnalytics>

    override fun getLayoutRes() = R.layout.swd_activity_saldo_withdraw

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
        val isSuccess = setResultIfSuccessFragment()
        if (!isSuccess) {
            analytics.get().onBackPressFromWithdrawalPage()
        }
        super.onBackPressed()
    }

    private fun setResultIfSuccessFragment(): Boolean {
        if (supportFragmentManager.findFragmentByTag(TAG_THANK_YOU_FRAGMENT) != null) {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            val fragment = supportFragmentManager.findFragmentByTag(TAG_THANK_YOU_FRAGMENT)
            if (fragment is ThankYouFragmentWithdrawal) {
                fragment.onCloseButtonClick()
            }
            return true
        }
        return false
    }

    override fun openThankYouFragment(
        withdrawalRequest: WithdrawalRequest,
        submitWithdrawalResponse: SubmitWithdrawalResponse
    ) {
        swd_header.setNavigationIcon(RUnifyComp.drawable.unify_bottomsheet_close)
        updateHeaderTitle(submitWithdrawalResponse.header ?: "")
        val thankYouFragment = ThankYouFragmentWithdrawal.getInstance(withdrawalRequest, submitWithdrawalResponse)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.parent_view, thankYouFragment, TAG_THANK_YOU_FRAGMENT)
        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun getTagFragment(): String {
        return TAG_SALDO_FRAGMENT
    }

    override fun getComponent(): WithdrawComponent {
        return DaggerWithdrawComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    companion object {
        const val TAG_SALDO_FRAGMENT = "TAG_SALDO_FRAGMENT"
        const val TAG_THANK_YOU_FRAGMENT = "TAG_THANK_YOU_FRAGMENT"
    }

    override fun onWithdrawalAndJoinRekening(isJoinRP: Boolean) {
        val fragment = supportFragmentManager.findFragmentByTag(TAG_SALDO_FRAGMENT)
        fragment?.let {
            if (fragment is WithdrawalJoinRPCallback) {
                fragment.onWithdrawalAndJoinRekening(isJoinRP)
            }
        }
    }
}
