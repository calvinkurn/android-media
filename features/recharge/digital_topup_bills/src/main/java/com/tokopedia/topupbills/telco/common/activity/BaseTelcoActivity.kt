package com.tokopedia.topupbills.telco.common.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.topupbills.view.bottomsheet.TopupBillsMenuBottomSheets
import com.tokopedia.header.HeaderUnify
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.common.analytics.DigitalTopupAnalytics
import com.tokopedia.topupbills.telco.common.di.DigitalTelcoComponent
import com.tokopedia.topupbills.telco.common.fragment.DigitalBaseTelcoFragment

import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs

open abstract class BaseTelcoActivity : BaseSimpleActivity(), HasComponent<DigitalTelcoComponent>,
        TopupBillsMenuBottomSheets.MenuListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var topupAnalytics: DigitalTopupAnalytics

    lateinit var appBarLayout: AppBarLayout
    lateinit var menuTelco: Menu

    private fun initInjector() {
        component.inject(this)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_digital_telco
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getToolbarResourceID(): Int {
        return R.id.telco_toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()
        intent?.handleExtra()

        //draw background without overdraw GPU
        window.setBackgroundDrawableResource(R.color.digital_cardview_light_background)

        setAnimationAppBarLayout()
    }

    private fun setAnimationAppBarLayout() {
        appBarLayout = findViewById(R.id.telco_app_bar_layout)

        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var lastOffset = -1
            override fun onOffsetChanged(p0: AppBarLayout?, verticalOffSet: Int) {
                if (lastOffset == verticalOffSet) return

                if (::menuTelco.isInitialized) {
                    lastOffset = verticalOffSet
                    if (abs(verticalOffSet) >= appBarLayout.totalScrollRange - toolbar.height) {
                        //Collapsed
                        (toolbar as HeaderUnify).transparentMode = false
                        menuTelco.getItem(0).icon = ContextCompat.getDrawable(this@BaseTelcoActivity,
                                com.tokopedia.abstraction.R.drawable.ic_toolbar_overflow_level_two_black)
                        if (fragment != null && fragment is DigitalBaseTelcoFragment) {
                            (fragment as DigitalBaseTelcoFragment).onCollapseAppBar()
                        }
                    } else {
                        //Expanded
                        (toolbar as HeaderUnify).transparentMode = true
                        menuTelco.getItem(0).icon = ContextCompat.getDrawable(this@BaseTelcoActivity,
                                com.tokopedia.abstraction.R.drawable.ic_toolbar_overflow_level_two_white)
                        if (fragment != null && fragment is DigitalBaseTelcoFragment) {
                            (fragment as DigitalBaseTelcoFragment).onExpandAppBar()
                        }
                    }
                }
            }
        })
    }

    /* This Method is use to tracking Action click when user click TelcoProduct
   */

    private fun Intent.handleExtra() {
        if (intent.data != null) {
            val trackingClick = intent.getStringExtra(RECHARGE_PRODUCT_EXTRA)
            if (trackingClick != null) {
                Timber.w("P2#ACTION_SLICE_CLICK_RECHARGE#$trackingClick")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuTelco = menu
            menuInflater.inflate(R.menu.menu_telco, menu)
            return true
        }
        return false
    }

    override fun onMenuOpened(featureId: Int, menu: Menu?): Boolean {
        showBottomMenus()
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId ?: "" == R.id.action_overflow_menu) {
            showBottomMenus()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onOrderListClicked() {
        if (userSession.isLoggedIn) {
            navigatePageToOrder()
        } else {
            val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_LOGIN_TELCO)
        }
    }

    override fun onPromoClicked() {
        RouteManager.route(this, ApplinkConst.PROMO_LIST)
    }

    override fun onHelpClicked() {
        RouteManager.route(this, ApplinkConst.CONTACT_US_NATIVE)
    }

    private fun showBottomMenus() {
        sendTrackingDotsMenuTelco(userSession.userId)
        val menuBottomSheet = TopupBillsMenuBottomSheets()
        menuBottomSheet.listener = this
        menuBottomSheet.show(supportFragmentManager, TAG_TELCO_MENU)
    }

    private fun navigatePageToOrder() {
        RouteManager.route(this, ApplinkConst.DIGITAL_ORDER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data?.let {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CODE_LOGIN_TELCO && userSession.isLoggedIn) {
                    navigatePageToOrder()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (fragment != null && fragment is DigitalBaseTelcoFragment) {
            (fragment as DigitalBaseTelcoFragment).onBackPressed()
        }
        super.onBackPressed()
    }

    abstract fun sendTrackingDotsMenuTelco(userId: String)

    companion object {
        const val REQUEST_CODE_LOGIN_TELCO = 10000

        const val PARAM_MENU_ID = "menu_id"
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_CLIENT_NUMBER = "client_number"
        const val PARAM_CATEGORY_ID = "category_id"
        const val RECHARGE_PRODUCT_EXTRA = "RECHARGE_PRODUCT_EXTRA"
        const val TAG_TELCO_MENU = "menu_telco"
    }
}