package com.tokopedia.hotel.evoucher.presentation.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.evoucher.presentation.fragment.HotelEVoucherFragment
import com.tokopedia.hotel.evoucher.presentation.widget.HotelMenuShareSheets


/**
 * @author by furqan on 14/05/19
 */
class HotelEVoucherActivity : HotelBaseActivity(), HotelMenuShareSheets.HotelShareListener {

    lateinit var fragment: HotelEVoucherFragment

    override fun shouldShowOptionMenu(): Boolean = true

    override fun getNewFragment(): Fragment {
        fragment = HotelEVoucherFragment.getInstance()
        return fragment
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        if (shouldShowOptionMenu()) {
            menuInflater.inflate(R.menu.hotel_share_menu, menu)
            updateOptionMenuColorWhite(menu)
        }
        return true
    }

    override fun onMenuOpened(featureId: Int, menu: Menu?): Boolean {
        showShareMenus()
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (shouldShowOptionMenu()) {
            if (item?.itemId ?: "" == R.id.action_share) {
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
        if (::fragment.isInitialized) fragment.takeScreenshot()
    }

    override fun onShareAsPdfClicked() {
        if (::fragment.isInitialized) fragment.shareAsPdf()
    }

    companion object {

        const val TAG_HOTEL_SHARE_MENU = "HOTEL_SHARE_MENU"

        fun getCallingIntent(context: Context): Intent =
                Intent(context, HotelEVoucherActivity::class.java)
    }
}