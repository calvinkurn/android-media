package com.tokopedia.saldodetails.saldoDetail

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.saldodetails.commom.di.component.SaldoDetailsComponent
import com.tokopedia.saldodetails.commom.di.component.SaldoDetailsComponentInstance
import com.tokopedia.saldodetails.saldoDetail.SaldoDepositFragment.Companion.REQUEST_WITHDRAW_CODE
import com.tokopedia.saldodetails.saldoDetail.coachmark.SaldoCoachMarkListener
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSession
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import javax.inject.Inject
import com.tokopedia.saldodetails.R as saldodetailsR

/**
 * For navigating to this class
 * [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.SALDO_DEPOSIT]
 */


class SaldoDepositActivity : BaseSimpleActivity(), HasComponent<SaldoDetailsComponent>, SaldoCoachMarkListener {

    var saldoToolbar: HeaderUnify? = null

    @Inject
    lateinit var userSession: UserSession
    private var isSeller: Boolean = false
    private val saldoComponent by lazy(LazyThreadSafetyMode.NONE) { SaldoDetailsComponentInstance.getComponent(this) }
    private var defaultStatusBarColor: Int = 0

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val unMaskedRequestCode = requestCode and 0x0000ffff
        if ((requestCode == REQUEST_WITHDRAW_CODE || unMaskedRequestCode == REQUEST_WITHDRAW_CODE)
                && resultCode == Activity.RESULT_OK) {
            if (supportFragmentManager.findFragmentByTag(TAG) == null) {
                finish()
            } else {
                (supportFragmentManager.findFragmentByTag(TAG) as SaldoDepositFragment).resetPage()
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.let {
            executeParamAction(it)
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

    override fun getParentViewResourceID() = com.tokopedia.saldodetails.R.id.saldo_deposit_parent_view

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        initInjector()
        setUpToolbar()
        initializeView()
        defaultStatusBarColor = window.statusBarColor
        hideStatusBar()
    }

    private fun executeParamAction(intent: Intent) {
        val queryParam = UriUtil.uriQueryParamsToMap(intent.data.toString())
        val toasterText = queryParam[TOASTER]
        val shouldReload = queryParam.containsKey(RELOAD)
        if (toasterText?.isNotEmpty() == true) {
            val rootView = findViewById<RelativeLayout>(saldodetailsR.id.il_main_content)
            val type = if (queryParam[TOASTER_ERROR] == "true") Toaster.TYPE_ERROR else Toaster.TYPE_NORMAL
            Toaster.build(rootView, toasterText, Snackbar.LENGTH_SHORT, type).show()
        }

        if (shouldReload) {
            (supportFragmentManager.findFragmentByTag(TAG) as SaldoDepositFragment).resetPage()
        }
    }

    private fun setUpToolbar() {
        saldoToolbar = findViewById(com.tokopedia.saldodetails.R.id.saldo_deposit_toolbar)
        saldoToolbar?.apply {
            headerView?.setTextColor(ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN900))
            navigationIcon = getIconUnifyDrawable(context, IconUnify.ARROW_BACK, ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN900))
            setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun hideStatusBar() {
        this.window?.let { window ->
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    fun showStatusBar() {
        this.window?.let { window ->
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = defaultStatusBarColor
        }
    }

    private fun initializeView() {
        isSeller = userSession.hasShop() || userSession.isAffiliate
    }

    override fun getTagFragment() = TAG
    override fun getScreenName() = null

    companion object {
        private const val FLAG_APP_SALDO_AUTO_WITHDRAWAL = "app_flag_saldo_auto_withdrawal_v2"
        private val REQUEST_CODE_LOGIN = 1001
        private val TAG = "DEPOSIT_FRAGMENT"
        private const val TOASTER = "toaster"
        private const val TOASTER_ERROR = "toaster_error"
        private const val RELOAD = "reload"
    }

    override fun startCoachMarkFlow(anchorView: View?) {
        if (supportFragmentManager.findFragmentByTag(TAG) == null) { } else {
            (supportFragmentManager.findFragmentByTag(TAG) as SaldoDepositFragment).startSaldoCoachMarkFlow(anchorView)
        }
    }
}
