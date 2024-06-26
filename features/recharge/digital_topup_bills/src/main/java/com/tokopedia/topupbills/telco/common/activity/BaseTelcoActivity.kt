package com.tokopedia.topupbills.telco.common.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
import javax.inject.Inject

open abstract class BaseTelcoActivity :
    BaseSimpleActivity(),
    HasComponent<DigitalTelcoComponent>,
    TopupBillsMenuBottomSheets.MenuListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var topupAnalytics: DigitalTopupAnalytics

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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuTelco = menu
            menuInflater.inflate(R.menu.menu_telco, menu)
            return true
        }
        return false
    }

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        showBottomMenus()
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId ?: "" == R.id.action_overflow_menu) {
            showBottomMenus()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onOrderListClicked() {
        RouteManager.route(this, ApplinkConst.DIGITAL_ORDER)
    }

    override fun onPromoClicked() {
        RouteManager.route(this, APPLINK_CONST_DEALS)
    }

    override fun onHelpClicked() {
        RouteManager.route(this, ApplinkConst.CONTACT_US_NATIVE)
    }

    private fun showBottomMenus() {
        sendTrackingDotsMenuTelco(userSession.userId)
        val menuBottomSheet = TopupBillsMenuBottomSheets.newInstance()
        menuBottomSheet.listener = this
        menuBottomSheet.setShowListener {
            menuBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }
        menuBottomSheet.show(supportFragmentManager, TAG_TELCO_MENU)
    }

    fun setupAppBar() {
        (toolbar as HeaderUnify).transparentMode = true
        if (::menuTelco.isInitialized) {
            menuTelco.getItem(0).icon = ContextCompat.getDrawable(
                this@BaseTelcoActivity,
                com.tokopedia.abstraction.R.drawable.ic_toolbar_overflow_level_two_white
            )
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
        const val PARAM_MENU_ID = "menu_id"
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_CLIENT_NUMBER = "client_number"
        const val PARAM_CATEGORY_ID = "category_id"
        const val RECHARGE_PRODUCT_EXTRA = "RECHARGE_PRODUCT_EXTRA"
        const val TAG_TELCO_MENU = "menu_telco"

        const val APPLINK_CONST_DEALS = "tokopedia://discovery/deals?activeTab=7&componentID=588"
    }
}
