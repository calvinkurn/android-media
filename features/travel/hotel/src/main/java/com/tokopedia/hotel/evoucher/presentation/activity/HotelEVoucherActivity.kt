package com.tokopedia.hotel.evoucher.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.evoucher.di.DaggerHotelEVoucherComponent
import com.tokopedia.hotel.evoucher.di.HotelEVoucherComponent
import com.tokopedia.hotel.evoucher.presentation.fragment.HotelEVoucherFragment
import com.tokopedia.hotel.evoucher.presentation.fragment.HotelEVoucherFragment.Companion.EXTRA_ORDER_ID
import com.tokopedia.hotel.evoucher.presentation.widget.HotelMenuShareSheets


/**
 * @author by furqan on 14/05/19
 */
class HotelEVoucherActivity : HotelBaseActivity(), HotelMenuShareSheets.HotelShareListener,
        HasComponent<HotelEVoucherComponent> {

    lateinit var fragment: HotelEVoucherFragment

    override fun shouldShowOptionMenu(): Boolean = true

    override fun getNewFragment(): Fragment {
        fragment = HotelEVoucherFragment.getInstance(intent.getStringExtra(EXTRA_ORDER_ID))
        return fragment
    }

    override fun getParentViewResourceID() = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        if (shouldShowOptionMenu()) {
            menuInflater.inflate(R.menu.hotel_share_menu, menu)
        }
        return true
    }

    override fun onMenuOpened(featureId: Int, menu: Menu?): Boolean {
        showShareMenus()
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (shouldShowOptionMenu()) {
            if (item?.itemId == R.id.action_share) {
                showShareMenus()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showShareMenus() {
        val hotelMenuBottomSheets = HotelMenuShareSheets()
        hotelMenuBottomSheets.listener = this
        hotelMenuBottomSheets.show(supportFragmentManager, TAG_HOTEL_SHARE_MENU)
    }

    override fun onShareAsImageClicked() {
        if (::fragment.isInitialized) fragment.takeScreenshot(isShare = true)
    }

    override fun onShareAsPdfClicked() {
        if (::fragment.isInitialized) fragment.shareAsPdf()
    }

    override fun onSaveImageClicked() {
        if (::fragment.isInitialized) fragment.takeScreenshot(isShare = false)
    }

    override fun getComponent(): HotelEVoucherComponent =
            DaggerHotelEVoucherComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()

    companion object {

        const val TAG_HOTEL_SHARE_MENU = "HOTEL_SHARE_MENU"

        fun getCallingIntent(context: Context, orderId: String): Intent =
                Intent(context, HotelEVoucherActivity::class.java)
                        .putExtra(EXTRA_ORDER_ID, orderId)
    }
}