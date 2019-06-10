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
import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.common.presentation.widget.HotelMenuBottomSheets
import com.tokopedia.hotel.orderdetail.data.model.HotelTransportDetail
import com.tokopedia.hotel.orderdetail.presentation.adapter.ContactAdapter
import com.tokopedia.hotel.orderdetail.presentation.fragment.HotelOrderDetailFragment
import com.tokopedia.hotel.orderdetail.presentation.widget.HotelContactPhoneBottomSheet
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by furqan on 25/03/19
 */
abstract class HotelBaseActivity: BaseSimpleActivity(), HotelMenuBottomSheets.HotelMenuListener,
        ContactAdapter.OnClickCallListener {

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
            menuInflater.inflate(R.menu.hotel_base_menu, menu)
            updateOptionMenuColorWhite(menu)
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
            // Will be decided later
            val bottomSheet = HotelContactPhoneBottomSheet()
            bottomSheet.contactList = listOf(
                    HotelTransportDetail.ContactInfo("100"),
                    HotelTransportDetail.ContactInfo("101"),
                    HotelTransportDetail.ContactInfo("102"),
                    HotelTransportDetail.ContactInfo("103"),
                    HotelTransportDetail.ContactInfo("104"),
                    HotelTransportDetail.ContactInfo("105"),
                    HotelTransportDetail.ContactInfo("106"),
                    HotelTransportDetail.ContactInfo("107"))
            bottomSheet.listener = this
            bottomSheet.show(supportFragmentManager, HotelOrderDetailFragment.TAG_CONTACT_INFO)
        } else {
            RouteManager.route(this, ApplinkConst.LOGIN)
        }
    }

    override fun onPromoClicked() {
        RouteManager.route(this, ApplinkConst.PROMO_LIST)
    }

    override fun onHelpClicked() {
        //        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun onClickCall(contactNumber: String) {
        RouteManager.route(this, "tokopedia://hotel/order/${contactNumber}")
    }

    abstract fun shouldShowOptionMenu(): Boolean

    companion object {
        val TAG_HOTEL_MENU = "hotelMenu"
    }
}