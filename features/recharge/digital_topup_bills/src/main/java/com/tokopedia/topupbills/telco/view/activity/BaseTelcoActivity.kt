package com.tokopedia.topupbills.telco.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.topupbills.view.bottomsheet.TopupBillsMenuBottomSheets
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.common.DigitalTopupAnalytics
import com.tokopedia.topupbills.telco.view.di.DigitalTopupComponent
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
import javax.inject.Inject

open abstract class BaseTelcoActivity : BaseSimpleActivity(), HasComponent<DigitalTopupComponent>,
        TopupBillsMenuBottomSheets.MenuListener {

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var topupAnalytics: DigitalTopupAnalytics

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
        return R.id.toolbar_telco
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()
        intent?.handleExtra()

        //draw background without overdraw GPU
        window.setBackgroundDrawableResource(R.color.digital_cardview_light_background)
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
        menuInflater.inflate(R.menu.menu_telco, menu)
        return true
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
        RouteManager.route(this, ApplinkConst.DIGITAL_ORDER)
        } else {
            RouteManager.route(this, ApplinkConst.LOGIN)
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

    abstract fun sendTrackingDotsMenuTelco(userId: String)

    companion object {
        const val PARAM_MENU_ID = "menu_id"
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_CLIENT_NUMBER = "client_number"
        const val PARAM_CATEGORY_ID = "category_id"
        const val RECHARGE_PRODUCT_EXTRA = "RECHARGE_PRODUCT_EXTRA"
        const val TAG_TELCO_MENU = "menu_telco"
    }
}