package com.tokopedia.flight.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.flight.R
import com.tokopedia.flight.dashboard.view.fragment.cache.FlightDashboardCache
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel
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
        setPassengerView(flightDashboardCache.passengerAdult, flightDashboardCache.passengerChild, flightDashboardCache.passengerInfant)
        tvFlightPassenger.text = passengerString
        tvFlightClass.text = getClassTitleById(flightDashboardCache.classCache)
    }

    fun isOneWay(): Boolean = isOneWay

    fun setDepartureDate(departureDate: Date) {

    }

    fun setReturnDate(returnDate: Date) {

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

        // save passenger to cache
        flightDashboardCache.putPassengerCount(passengerModel.adult, passengerModel.children, passengerModel.infant)
    }

    fun setClassView(classModel: FlightClassViewModel) {
        this.classModel = classModel
        flightDashboardCache.putClassCache(classModel.id)
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

        // save passenger to cache
        flightDashboardCache.putPassengerCount(adult, children, infant)
    }

    private fun getClassTitleById(classId: Int): String {
        return when (classId) {
            1 -> "Ekonomi"
            2 -> "Bisnis"
            3 -> "Utama"
            else -> ""
        }
    }

    interface FlightSearchFormListener {
        fun onDepartureAirportClicked()
        fun onDestinationAirportClicked()
        fun onDepartureDateClicked(departureDate: Date?)
        fun onReturnDateClicked(returnDate: Date?)
        fun onPassengerClicked(passengerModel: FlightPassengerViewModel?)
        fun onClassClicked(classModel: FlightClassViewModel?)
    }

}