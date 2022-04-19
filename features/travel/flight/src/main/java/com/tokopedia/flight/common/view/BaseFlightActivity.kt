package com.tokopedia.flight.common.view

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.widget.TravelMenuBottomSheet
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.common.constant.FlightUrl
import com.tokopedia.flight.common.di.component.FlightComponent
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by furqan on 10/06/2021
 */
abstract class BaseFlightActivity : BaseSimpleActivity(),
        TravelMenuBottomSheet.TravelMenuListener {

    @Inject
    lateinit var flightAnalytics: FlightAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    private var component: FlightComponent? = null

    override fun getScreenName(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        initInjector()
    }

    open override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        menuInflater.inflate(R.menu.menu_flight_dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_overflow_menu) {
            showBottomMenu()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        showBottomMenu()
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_LOGIN_FLIGHT) {
            RouteManager.route(this, ApplinkConst.FLIGHT_ORDER)
        }
    }

    override fun onOrderListClicked() {
        flightAnalytics.eventClickTransactions(screenName)
        RouteManager.route(this, ApplinkConst.FLIGHT_ORDER)
    }

    override fun onPromoClicked() {
        navigateToAllPromoPage()
    }

    open override fun onHelpClicked() {
        navigateToHelpPage()
    }

    protected fun getFlightComponent(): FlightComponent? {
        if (component == null) {
            component = FlightComponentInstance.getFlightComponent(application)
        }
        return component
    }

    open fun navigateToHelpPage() {
        RouteManager.route(this, FlightUrl.CONTACT_US_FLIGHT)
    }

    private fun navigateToAllPromoPage() {
        RouteManager.route(this, FlightUrl.FLIGHT_PROMO_APPLINK)
    }

    private fun initInjector() {
        getFlightComponent()?.inject(this)
    }

    private fun showBottomMenu() {
        val flightMenuBottomSheet = TravelMenuBottomSheet()
        flightMenuBottomSheet.listener = this
        flightMenuBottomSheet.show(supportFragmentManager, TAG_FLIGHT_MENU)
    }

    companion object {
        const val TAG_FLIGHT_MENU = "flightMenu"
        const val REQUEST_CODE_LOGIN_FLIGHT = 101
    }
}