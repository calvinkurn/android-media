package com.tokopedia.flight.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.flight.R
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.dashboard.view.fragment.cache.FlightDashboardCache
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.layout_flight_search_view.view.*
import java.util.*

/**
 * @author by furqan on 09/03/2020
 */
class FlightSearchFormView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    var listener: FlightSearchFormListener? = null

    private var flightDashboardCache: FlightDashboardCache = FlightDashboardCache(context)

    private var isOneWay: Boolean = true
    private var departureDate: Date? = null
    private var departureDateString: String = ""
    private var returnDate: Date? = null
    private var returnDateString: String = ""
    private var passengerModel: FlightPassengerViewModel? = null
    private var passengerString: String = ""
    private var classModel: FlightClassViewModel? = null

    init {
        View.inflate(context, R.layout.layout_flight_search_view, this)

        renderFromCache()
        setViewClickListener()
    }

    private fun renderFromCache() {
        setTrip(!flightDashboardCache.isRoundTrip)

        if (flightDashboardCache.departureDate.isNotEmpty() &&
                FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_FORMAT, flightDashboardCache.departureDate)
                        .after(FlightDateUtil.getCurrentDate())) {
            setDepartureDate(FlightDateUtil.stringToDate(
                    FlightDateUtil.DEFAULT_FORMAT, flightDashboardCache.departureDate))
        } else {
            setDepartureDate(generateDefaultDepartureDate())
        }

        if (flightDashboardCache.returnDate.isNotEmpty() &&
                FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_FORMAT, flightDashboardCache.returnDate)
                        .after(FlightDateUtil.addTimeToCurrentDate(Calendar.DATE, 1))) {
            setReturnDate(FlightDateUtil.stringToDate(
                    FlightDateUtil.DEFAULT_FORMAT, flightDashboardCache.returnDate))
        } else {
            setReturnDate(generateDefaultReturnDate())
        }

        setPassengerView(flightDashboardCache.passengerAdult, flightDashboardCache.passengerChild, flightDashboardCache.passengerInfant)
        setClassView(getClassById(flightDashboardCache.classCache))

        renderTripView()
    }

    fun setTrip(isOneWay: Boolean) {
        this.isOneWay = isOneWay
    }

    fun isOneWay(): Boolean = isOneWay

    fun setDepartureDate(departureDate: Date) {
        this.departureDate = departureDate
        this.departureDateString = FlightDateUtil.dateToString(departureDate, FlightDateUtil.DEFAULT_VIEW_FORMAT)
        tvFlightDepartureDate.text = departureDateString
    }

    fun setReturnDate(returnDate: Date) {
        this.returnDate = returnDate
        this.returnDateString = FlightDateUtil.dateToString(returnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT)
        tvFlightReturnDate.text = returnDateString
    }

    fun setPassengerView(passengerModel: FlightPassengerViewModel) {
        this.passengerModel = passengerModel
        var passengerFmt = ""
        if (passengerModel.adult > 0) {
            passengerFmt = passengerModel.adult.toString() + " " + context.getString(R.string.flight_dashboard_adult_passenger)
            if (passengerModel.children > 0) {
                passengerFmt += ", " + passengerModel.children + " " + context.getString(R.string.flight_dashboard_adult_children)
            }
            if (passengerModel.infant > 0) {
                passengerFmt += ", " + passengerModel.infant + " " + context.getString(R.string.flight_dashboard_adult_infant)
            }
        }
        passengerString = passengerFmt
        tvFlightPassenger.text = passengerString
    }

    fun setClassView(classModel: FlightClassViewModel) {
        this.classModel = classModel
        tvFlightClass.text = classModel.title
    }

    fun removeFocus() {
        tvFlightOriginLabel.clearFocus()
        tvFlightOriginAirport.clearFocus()
        tvFlightDestinationLabel.clearFocus()
        tvFlightDestinationAirport.clearFocus()
        tvFlightDepartureDateLabel.clearFocus()
        tvFlightDepartureDate.clearFocus()
        tvFlightReturnDateLabel.clearFocus()
        tvFlightReturnDate.clearFocus()
        tvFlightPassengerLabel.clearFocus()
        tvFlightPassenger.clearFocus()
        tvFlightClassLabel.clearFocus()
        tvFlightClass.clearFocus()
    }

    private fun setViewClickListener() {
        switchFlightRoundTrip.setOnClickListener {
            toggleOneWay()
        }
        tvFlightOriginLabel.setOnClickListener { listener?.onDepartureAirportClicked() }
        tvFlightOriginAirport.setOnClickListener { listener?.onDepartureAirportClicked() }
        tvFlightDestinationLabel.setOnClickListener { listener?.onDestinationAirportClicked() }
        tvFlightDestinationAirport.setOnClickListener { listener?.onDestinationAirportClicked() }
        tvFlightDepartureDateLabel.setOnClickListener { listener?.onDepartureDateClicked(returnDate) }
        tvFlightDepartureDate.setOnClickListener { listener?.onDepartureDateClicked(returnDate) }
        tvFlightReturnDateLabel.setOnClickListener { listener?.onReturnDateClicked(returnDate) }
        tvFlightReturnDate.setOnClickListener { listener?.onReturnDateClicked(returnDate) }
        tvFlightPassengerLabel.setOnClickListener { listener?.onPassengerClicked(passengerModel) }
        tvFlightPassenger.setOnClickListener { listener?.onPassengerClicked(passengerModel) }
        tvFlightClassLabel.setOnClickListener { listener?.onClassClicked(classModel) }
        tvFlightClass.setOnClickListener { listener?.onClassClicked(classModel) }
    }

    private fun setPassengerView(adult: Int = 1, children: Int = 0, infant: Int = 0) {
        this.passengerModel = FlightPassengerViewModel(adult, children, infant)

        var passengerFmt = ""
        if (adult > 0) {
            passengerFmt = adult.toString() + " " + context.getString(R.string.flight_dashboard_adult_passenger)
            if (children > 0) {
                passengerFmt += ", " + children + " " + context.getString(R.string.flight_dashboard_adult_children)
            }
            if (infant > 0) {
                passengerFmt += ", " + infant + " " + context.getString(R.string.flight_dashboard_adult_infant)
            }
        }

        passengerString = passengerFmt
        tvFlightPassenger.text = passengerString
    }

    private fun getClassById(classId: Int): FlightClassViewModel {
        return when (classId) {
            1 -> FlightClassViewModel().apply {
                id = 1
                title = "Ekonomi"
            }
            2 -> FlightClassViewModel().apply {
                id = 2
                title = "Bisnis"
            }
            3 -> FlightClassViewModel().apply {
                id = 3
                title = "Utama"
            }
            else -> FlightClassViewModel()
        }
    }

    private fun toggleOneWay() {
        isOneWay = !isOneWay
        renderTripView()
    }

    private fun renderTripView() {
        if (isOneWay) {
            switchFlightRoundTrip.isSelected = false
            hideReturnDateView()
        } else {
            switchFlightRoundTrip.isSelected = true
            showReturnDateView()
        }
    }

    private fun showReturnDateView() {
        icFlightReturnDate.show()
        tvFlightReturnDateLabel.show()
        tvFlightReturnDate.show()
        separatorReturnDate.show()
    }

    private fun hideReturnDateView() {
        icFlightReturnDate.hide()
        tvFlightReturnDateLabel.hide()
        tvFlightReturnDate.hide()
        separatorReturnDate.hide()
    }

    private fun generateDefaultDepartureDate(): Date =
            FlightDateUtil.addTimeToCurrentDate(Calendar.DATE, 1)

    private fun generateDefaultReturnDate(): Date =
            FlightDateUtil.addTimeToCurrentDate(Calendar.DATE, 2)

    interface FlightSearchFormListener {
        fun onDepartureAirportClicked()
        fun onDestinationAirportClicked()
        fun onDepartureDateClicked(departureDate: Date?)
        fun onReturnDateClicked(returnDate: Date?)
        fun onPassengerClicked(passengerModel: FlightPassengerViewModel?)
        fun onClassClicked(classModel: FlightClassViewModel?)
    }

}