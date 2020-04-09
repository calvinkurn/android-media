package com.tokopedia.flight.searchV4.presentation.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.common.view.BaseFlightActivity
import com.tokopedia.flight.dashboard.view.fragment.model.FlightPassengerModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.searchV4.presentation.fragment.FlightSearchFragment
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.activity_flight_search.*

/**
 * @author by furqan on 06/04/2020
 */
open class FlightSearchActivity : BaseFlightActivity() {

    protected lateinit var flightSearchPassDataModel: FlightSearchPassDataModel
    protected var dateString = ""
    protected var passengerString = ""
    protected var classString = ""

    private lateinit var wrapper: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeDataFromExtras()
        super.onCreate(savedInstanceState)

        setupSearchToolbarAction()
        setupSearchToolbarText()
    }

    override fun getScreenName(): String = FlightAnalytics.Screen.SEARCH

    override fun getLayoutRes(): Int = R.layout.activity_flight_search

    override fun getToolbarResourceID(): Int = R.id.flight_search_header

    override fun getParentViewResourceID(): Int = R.id.flight_search_parent_view

    override fun getNewFragment(): Fragment? =
            FlightSearchFragment.newInstance(flightSearchPassDataModel)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = false

    open fun initializeToolbarData() {
        dateString = FlightDateUtil.formatDate(
                FlightDateUtil.DEFAULT_FORMAT,
                FlightDateUtil.DEFAULT_VIEW_FORMAT,
                flightSearchPassDataModel.departureDate
        )
        passengerString = buildPassengerTextFormatted(flightSearchPassDataModel.flightPassengerViewModel)
        classString = flightSearchPassDataModel.flightClass.title
    }

    open fun getDepartureAirport(): FlightAirportModel = flightSearchPassDataModel.departureAirport

    open fun getArrivalAirport(): FlightAirportModel = flightSearchPassDataModel.arrivalAirport

    open fun isReturnPage(): Boolean = false

    protected fun buildPassengerTextFormatted(passData: FlightPassengerModel): String {
        var passengerFmt = ""

        if (passData.adult > 0) {
            passengerFmt += "${passData.adult}  ${getString(R.string.flight_dashboard_adult_passenger)}"
            if (passData.children > 0) {
                passengerFmt += ", ${passData.children} ${getString(R.string.flight_dashboard_adult_children)}"
            }
            if (passData.infant > 0) {
                passengerFmt += ", ${passData.infant} ${getString(R.string.flight_dashboard_adult_infant)}"
            }
        }

        return passengerFmt
    }

    private fun initializeDataFromExtras() {
        flightSearchPassDataModel = intent.getParcelableExtra(EXTRA_PASS_DATA)
        initializeToolbarData()
    }

    private fun setupSearchToolbarAction() {
        wrapper = LinearLayout(this)
        wrapper.apply {
            val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            setBackgroundColor(Color.WHITE)
            layoutParams = param
        }

        val imageView = ImageView(this)
        val param = LinearLayout.LayoutParams(DIMEN_24_IN_PX, DIMEN_24_IN_PX)
        imageView.layoutParams = param
        imageView.setImage(R.drawable.ic_flight_edit, CORNER_RADIUS)

        wrapper.addView(imageView)
        wrapper.setOnClickListener {
            //            showChangeSearchBottomSheet()
        }

        flight_search_header.addCustomRightContent(wrapper)
        flight_search_header.isShowBackButton = true
        flight_search_header.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupSearchToolbarText() {
        val title = "${getDepartureAirport().cityName} ➝ ${getArrivalAirport().cityName}"
        val subtitle = "$dateString | $passengerString | $classString"

        flight_search_header.title = title
        flight_search_header.subtitle = subtitle
    }

    companion object {
        const val EXTRA_PASS_DATA = "EXTRA_PASS_DATA"
        private val DIMEN_24_IN_PX = 24.toPx()
        private const val CORNER_RADIUS = 8f

        fun getCallingIntent(context: Context, passDataModel: FlightSearchPassDataModel): Intent =
                Intent(context, FlightSearchActivity::class.java)
                        .putExtra(EXTRA_PASS_DATA, passDataModel)
    }
}