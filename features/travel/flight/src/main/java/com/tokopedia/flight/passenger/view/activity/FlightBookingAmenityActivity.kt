package com.tokopedia.flight.passenger.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.flight.R
import com.tokopedia.flight.passenger.view.adapter.viewholder.FlightBookingAmenityViewHolder
import com.tokopedia.flight.passenger.view.fragment.FlightBookingAmenityFragment
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityMetaModel
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityModel
import java.util.*

/**
 * Created by furqan on 06/10/21.
 */
class FlightBookingAmenityActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        val metaViewModel: FlightBookingAmenityMetaModel = intent.getParcelableExtra(FlightBookingAmenityFragment.EXTRA_SELECTED_AMENITIES)
        return FlightBookingAmenityFragment.createInstance(
                intent.getParcelableArrayListExtra(FlightBookingAmenityFragment.EXTRA_LIST_AMENITIES),
                metaViewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(intent.getStringExtra(EXTRA_TITLE))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_flight_amenity_info_reset, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_reset) {
            onResetClicked()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun onResetClicked() {
        val f = currentFragment
        if (f != null && f is FlightBookingAmenityViewHolder.ListenerCheckedLuggage) {
            (f as FlightBookingAmenityViewHolder.ListenerCheckedLuggage).resetItemCheck()
        }
    }

    private val currentFragment: Fragment?
        get() {
            val fragmentList = supportFragmentManager.fragments
            var i = 0
            val sizei = fragmentList.size
            while (i < sizei) {
                val fragment = fragmentList[i]
                if (fragment.isAdded && fragment.isVisible) {
                    return fragment
                }
                i++
            }
            return null
        }

    companion object {

        private const val EXTRA_TITLE = "EXTRA_TITLE"

        fun createIntent(context: Context?, title: String?, flightBookingAmenityViewModels: List<FlightBookingAmenityModel?>?,
                         selectedLuggage: FlightBookingAmenityMetaModel?): Intent {
            val intent = Intent(context, FlightBookingAmenityActivity::class.java)
            intent.putParcelableArrayListExtra(FlightBookingAmenityFragment.EXTRA_LIST_AMENITIES, flightBookingAmenityViewModels as ArrayList<out Parcelable?>?)
            intent.putExtra(FlightBookingAmenityFragment.EXTRA_SELECTED_AMENITIES, selectedLuggage)
            intent.putExtra(EXTRA_TITLE, title)
            return intent
        }

    }
}