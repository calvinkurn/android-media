package com.tokopedia.flight.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.flight.R
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel
import com.tokopedia.unifycomponents.BaseCustomView
import java.util.*

/**
 * @author by furqan on 09/03/2020
 */
class FlightSearchFormView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var isOneWay: Boolean = true
    private var departureDate: Date? = null
    private var departureDateString: String = ""
    private var returnDate: Date? = null
    private var returnDateString: String = ""
    private var passengerString: String = ""
    private var classString: String = ""

    init {
        View.inflate(context, R.layout.layout_flight_search_view, this)

        setOneWay()
        setPassengerView()
    }

    fun setOneWay() {
        isOneWay = true
    }

    fun setRoundTrip() {
        isOneWay = false
    }

    fun isOneWay(): Boolean = isOneWay

    fun setDepartureDate(departureDate: Date) {

    }

    fun setPassengerView(passengerModel: FlightPassengerViewModel) {
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
    }

    fun setPassengerView(adult: Int = 1, children: Int = 0, infant: Int = 0) {
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
    }

    fun setClassView(classString: String) {
        this.classString = classString
    }

}