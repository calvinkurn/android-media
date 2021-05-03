package com.tokopedia.hotel.common.presentation

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTravel
import com.tokopedia.common.travel.widget.TravelMenuBottomSheet
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by furqan on 25/03/19
 */
abstract class HotelBaseActivity: BaseSimpleActivity(), TravelMenuBottomSheet.TravelMenuListener {

    private lateinit var hotelComponent: HotelComponent

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    @Inject
    lateinit var trackingHotelUtil: TrackingHotelUtil

    var optionMenu: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        initInjector()
        GraphqlClient.init(this)
    }

    private fun initInjector() {
        getHotelComponent().inject(this)
    }

    override fun sendScreenAnalytics() {
        screenName?.let {
            trackingHotelUtil.openScreen(this, it)
        }
    }

    override fun onMenuOpened(featureId: Int, menu: Menu?): Boolean {
        showBottomMenus()
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        if (shouldShowOptionMenu()) {
            if (shouldShowMenuWhite()) {
                menuInflater.inflate(R.menu.hotel_base_menu_white, menu)
                optionMenu = menu?.findItem(R.id.action_overflow_menu_white)
            }
            else {
                menuInflater.inflate(R.menu.hotel_base_menu, menu)
                optionMenu = menu?.findItem(R.id.action_overflow_menu)
            }

        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (shouldShowOptionMenu()) {
            if (item?.itemId ?: "" == R.id.action_overflow_menu ||
                    item?.itemId ?: "" == R.id.action_overflow_menu_white) {
                showBottomMenus()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onOrderListClicked() {
        if (userSessionInterface.isLoggedIn) {
            RouteManager.route(this, ApplinkConst.HOTEL_ORDER)
        } else {
            val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_LOGIN_HOTEl)
        }
    }

    override fun onPromoClicked() {
        RouteManager.route(this, ApplinkConstInternalTravel.HOTEL_PROMO_LIST)
    }

    override fun onHelpClicked() {
        RouteManager.route(this, ApplinkConst.CONTACT_US_NATIVE)
    }

    private fun showBottomMenus() {
        val hotelMenuBottomSheets = TravelMenuBottomSheet()
        hotelMenuBottomSheets.listener = this
        hotelMenuBottomSheets.show(supportFragmentManager, TAG_HOTEL_MENU)
    }

    protected fun getHotelComponent(): HotelComponent {
        if (!::hotelComponent.isInitialized) {
            hotelComponent = HotelComponentInstance.getHotelComponent(application)
        }
        return hotelComponent as HotelComponent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_LOGIN_HOTEl) {
            RouteManager.route(this, ApplinkConst.FLIGHT_ORDER)
        }
    }

    open fun shouldShowMenuWhite(): Boolean = false

    abstract fun shouldShowOptionMenu(): Boolean

    companion object {
        val TAG_HOTEL_MENU = "hotelMenu"

        const val REQUEST_CODE_LOGIN_HOTEl = 100
    }
}