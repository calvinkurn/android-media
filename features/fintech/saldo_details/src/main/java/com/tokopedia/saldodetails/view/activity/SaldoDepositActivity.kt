package com.tokopedia.saldodetails.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.di.SaldoDetailsComponent
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance
import com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment
import com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment.Companion.REQUEST_WITHDRAW_CODE
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.activity_saldo_deposit.*
import javax.inject.Inject

/**
 * For navigating to this class
 * [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SALDO_DEPOSIT]
 */


class SaldoDepositActivity : BaseSimpleActivity(), HasComponent<SaldoDetailsComponent> {

    @Inject
    lateinit var userSession: UserSession
    private var isSeller: Boolean = false
    private val saldoComponent by lazy(LazyThreadSafetyMode.NONE) { SaldoDetailsComponentInstance.getComponent(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSecureWindowFlag()
    }

    private fun setSecureWindowFlag() {
        runOnUiThread {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val unMaskedRequestCode = requestCode and 0x0000ffff
        if ((requestCode == REQUEST_WITHDRAW_CODE || unMaskedRequestCode == REQUEST_WITHDRAW_CODE)
                && resultCode == Activity.RESULT_OK) {
            if (supportFragmentManager.findFragmentByTag(TAG) == null) {
                finish()
            } else {
                (supportFragmentManager.findFragmentByTag(TAG) as SaldoDepositFragment).refresh()
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

        if (userSession.isLoggedIn) {
            return SaldoDepositFragment.createInstance(isSeller)
        } else {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
            return null
        }

    }

    override fun getParentViewResourceID(): Int {
        return com.tokopedia.saldodetails.R.id.saldo_deposit_parent_view
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        initInjector()
        initializeView()
        setUpToolbar()
    }

    private fun setUpToolbar() {
        toolbar = findViewById(com.tokopedia.saldodetails.R.id.saldo_deposit_toolbar)
        val upArrow = ContextCompat.getDrawable(this, com.tokopedia.abstraction.R.drawable.ic_action_back)
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, com.tokopedia.design.R.color.grey_700), PorterDuff.Mode.SRC_ATOP)
            toolbar.navigationIcon = upArrow
        } else {
            toolbar.setNavigationIcon(com.tokopedia.design.R.drawable.ic_icon_back_black)
        }
        toolbar.setPadding(toolbar.paddingLeft, toolbar.paddingTop, resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_12), toolbar.paddingBottom)
        setSupportActionBar(toolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(true)
            it.setHomeButtonEnabled(true)
            it.setTitle(this.title)
            updateTitle("")
        }

    }

    private fun initializeView() {
        isSeller = userSession.hasShop() || userSession.isAffiliate
        val saldoHelp = findViewById<View>(com.tokopedia.saldodetails.R.id.toolbar_saldo_help)

        saldoHelp.show()
        saldoHelp.setOnClickListener { v -> RouteManager.route(this, ApplinkConstInternalGlobal.SALDO_INTRO) }
        addAutoWithdrawalSettingIcon(isSeller)
    }

    private fun addAutoWithdrawalSettingIcon(isSeller : Boolean){
        if(isSeller) {
            val isAutoWithdrawalPageEnable = FirebaseRemoteConfigImpl(this)
                    .getBoolean(FLAG_APP_SALDO_AUTO_WITHDRAWAL, false)
            if(isAutoWithdrawalPageEnable && isAutoWithRollenceActive()) {
                toolbarAutoWithdrawalSetting.visible()
                toolbarAutoWithdrawalSetting.setOnClickListener {
                    RouteManager.route(this, ApplinkConstInternalGlobal.AUTO_WITHDRAW_SETTING)
                }
            }else{
                toolbarAutoWithdrawalSetting.gone()
            }
        }else{
            toolbarAutoWithdrawalSetting.gone()
        }
    }

    private fun isAutoWithRollenceActive() : Boolean{
      return (KEY_ROLLENCE_AUTO_WITHDRAWAL == RemoteConfigInstance.getInstance()
              .abTestPlatform.getString(KEY_ROLLENCE_AUTO_WITHDRAWAL, ""))
    }

    override fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }


    override fun getTagFragment(): String? {
        return TAG
    }

    companion object {
        private const val KEY_ROLLENCE_AUTO_WITHDRAWAL = "Auto_Withdrawal_RP"
        private const val FLAG_APP_SALDO_AUTO_WITHDRAWAL = "app_flag_saldo_auto_withdrawal_v2"
        private val REQUEST_CODE_LOGIN = 1001
        private val TAG = "DEPOSIT_FRAGMENT"

    }
}
