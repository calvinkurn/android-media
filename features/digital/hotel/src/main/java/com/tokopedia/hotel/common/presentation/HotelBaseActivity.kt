package com.tokopedia.hotel.common.presentation

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.applink.ApplinkConstant
import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.common.presentation.widget.HotelMenuBottomSheets
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by furqan on 25/03/19
 */
abstract class HotelBaseActivity: BaseSimpleActivity(), HotelMenuBottomSheets.HotelMenuListener {

    private lateinit var hotelComponent: HotelComponent

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

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

    override fun onMenuOpened(featureId: Int, menu: Menu?): Boolean {
        showBottomMenus()
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        if (shouldShowOptionMenu()) {
            if (shouldShowMenuWhite()) menuInflater.inflate(R.menu.hotel_base_menu_white, menu)
            else menuInflater.inflate(R.menu.hotel_base_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (shouldShowOptionMenu()) {
            if (item?.itemId ?: "" == R.id.action_overflow_menu) {
                showBottomMenus()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onOrderListClicked() {
        if (userSessionInterface.isLoggedIn) {
            RouteManager.route(this, ApplinkConstant.HOTEL_ORDER_LIST)
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
        val hotelMenuBottomSheets = HotelMenuBottomSheets()
        hotelMenuBottomSheets.listener = this
        hotelMenuBottomSheets.show(supportFragmentManager, TAG_HOTEL_MENU)
    }

    protected fun getHotelComponent(): HotelComponent {
        if (!::hotelComponent.isInitialized) {
            hotelComponent = HotelComponentInstance.getHotelComponent(application)
        }
        return hotelComponent as HotelComponent
    }

    open fun shouldShowMenuWhite(): Boolean = false

    abstract fun shouldShowOptionMenu(): Boolean

    companion object {
        val TAG_HOTEL_MENU = "hotelMenu"
    }
}