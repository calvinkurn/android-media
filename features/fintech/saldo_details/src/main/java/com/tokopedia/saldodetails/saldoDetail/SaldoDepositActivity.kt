package com.tokopedia.saldodetails.saldoDetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.header.HeaderUnify
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.saldodetails.commom.di.component.SaldoDetailsComponent
import com.tokopedia.saldodetails.commom.di.component.SaldoDetailsComponentInstance
import com.tokopedia.saldodetails.saldoDetail.SaldoDepositFragment.Companion.REQUEST_WITHDRAW_CODE
import com.tokopedia.saldodetails.saldoDetail.coachmark.SaldoCoachMarkListener
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

/**
 * For navigating to this class
 * [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SALDO_DEPOSIT]
 */


class SaldoDepositActivity : BaseSimpleActivity(), HasComponent<SaldoDetailsComponent>, SaldoCoachMarkListener {

    private lateinit var saldoToolbar: HeaderUnify

    @Inject
    lateinit var userSession: UserSession
    private var isSeller: Boolean = false
    private val saldoComponent by lazy(LazyThreadSafetyMode.NONE) { SaldoDetailsComponentInstance.getComponent(this) }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val unMaskedRequestCode = requestCode and 0x0000ffff
        if ((requestCode == REQUEST_WITHDRAW_CODE || unMaskedRequestCode == REQUEST_WITHDRAW_CODE)
                && resultCode == Activity.RESULT_OK) {
            if (supportFragmentManager.findFragmentByTag(TAG) == null) {
                finish()
            } else {
                (supportFragmentManager.findFragmentByTag(TAG) as SaldoDepositFragment).resetPageAfterWithdrawal()
            }
        }
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                inflateFragment()
            } else {
                finish()
            }
        }
    }

    private fun initInjector() {
        saldoComponent.inject(this)
    }

    override fun getComponent(): SaldoDetailsComponent? {
        return saldoComponent
    }

    override fun getLayoutRes(): Int {
        return com.tokopedia.saldodetails.R.layout.activity_saldo_deposit
    }

    override fun getNewFragment(): Fragment? {
        return if (userSession.isLoggedIn) {
            SaldoDepositFragment.createInstance(isSeller)
        } else {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
            null
        }
    }

    override fun getToolbarResourceID() = com.tokopedia.saldodetails.R.id.saldo_deposit_toolbar

    override fun getParentViewResourceID() = com.tokopedia.saldodetails.R.id.saldo_deposit_parent_view

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        initInjector()
        setUpToolbar()
        initializeView()
    }

    private fun setUpToolbar() {
        saldoToolbar = findViewById(com.tokopedia.saldodetails.R.id.saldo_deposit_toolbar)
        saldoToolbar.apply {
            rightContentView.removeAllViews()
            addRightIcon(com.tokopedia.saldodetails.R.drawable.saldo_ic_info)
            rightIcons?.let {
                it.getOrNull(0)?.setOnClickListener { RouteManager.route(context, ApplinkConstInternalGlobal.SALDO_INTRO) }
            }
        }
    }

    private fun initializeView() {
        isSeller = userSession.hasShop() || userSession.isAffiliate
        addAutoWithdrawalSettingIcon(isSeller)
    }

    private fun addAutoWithdrawalSettingIcon(isSeller : Boolean){
        if (isSeller) {
            val isAutoWithdrawalPageEnable = FirebaseRemoteConfigImpl(this)
                    .getBoolean(FLAG_APP_SALDO_AUTO_WITHDRAWAL, false)
            if (isAutoWithdrawalPageEnable ) {
                saldoToolbar.apply {
                    addRightIcon(com.tokopedia.saldodetails.R.drawable.saldo_ic_setting)
                    rightIcons?.let {
                        it.getOrNull(1)?.setOnClickListener { RouteManager.route(context, ApplinkConstInternalGlobal.AUTO_WITHDRAW_SETTING) }
                    }
                }
            }
        }
    }

    override fun getTagFragment() = TAG
    override fun getScreenName() = null

    companion object {
        private const val FLAG_APP_SALDO_AUTO_WITHDRAWAL = "app_flag_saldo_auto_withdrawal_v2"
        private val REQUEST_CODE_LOGIN = 1001
        private val TAG = "DEPOSIT_FRAGMENT"

    }

    override fun startCoachMarkFlow(anchorView: View?) {
        if (supportFragmentManager.findFragmentByTag(TAG) == null) { } else {
            (supportFragmentManager.findFragmentByTag(TAG) as SaldoDepositFragment).startSaldoCoachMarkFlow(anchorView)
        }
    }
}
