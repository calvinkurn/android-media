package com.tokopedia.flight.search.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.flight.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by furqan on 16/04/2020
 */
class DepartureTripLabelView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val tvDestination: Typography
    private val tvAirline: Typography
    private val tvDate: Typography
    private val tvTime: Typography
    private val tvPrice: Typography

    init {
        val view = View.inflate(context, R.layout.widget_flight_departure_trip_label, this)
        tvDestination = view.findViewById(R.id.tvJourneyAirports)
        tvAirline = view.findViewById(R.id.tvJourneyAirline)
        tvDate = view.findViewById(R.id.tvJourneyDate)
        tvTime = view.findViewById(R.id.tvJourneyTime)
        tvPrice = view.findViewById(R.id.tvJourneyPrice)
    }

    fun setDestination(destination: CharSequence) {
        tvDestination.text = destination
    }

    fun setAirline(airline: CharSequence) {
        tvAirline.text = airline
    }

    fun setDate(date: CharSequence) {
        tvDate.text = date
    }

    fun setTime(time: CharSequence) {
        tvTime.text = time
    }

    fun setPrice(price: CharSequence) {
        tvPrice.text = price
    }
}