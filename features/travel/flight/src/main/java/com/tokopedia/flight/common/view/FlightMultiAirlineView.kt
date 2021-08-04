package com.tokopedia.flight.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.flight.R
import java.util.*

/**
 * Created by Furqan on 06/10/2021.
 */
class FlightMultiAirlineView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    private var airlineLogoList: MutableList<String>? = null

    fun setAirlineLogo(airlineLogo: String) {
        airlineLogoList = ArrayList()
        airlineLogoList?.add(airlineLogo)
        updateViewByAirlineLogo()
    }

    fun setAirlineLogos(airlineLogos: MutableList<String>?) {
        airlineLogoList = airlineLogos
        updateViewByAirlineLogo()
    }

    private fun updateViewByAirlineLogo() {
        removeAllViews()
        if (airlineLogoList == null || airlineLogoList!!.size == 0) {
            airlineLogoList = ArrayList()
            airlineLogoList?.add("")
        } else if (airlineLogoList!!.size > 1) {
            val view = LayoutInflater.from(context).inflate(R.layout.view_airline_logo, this, false)
            val ivAirline = view.findViewById<ImageView>(R.id.iv_airline_logo)
            ivAirline.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.flight_ic_multi_airlines))
            addView(ivAirline)
        } else if (airlineLogoList!!.size == 1) {
            val view = LayoutInflater.from(context).inflate(R.layout.view_airline_logo, this, false)
            val ivAirline = view.findViewById<ImageView>(R.id.iv_airline_logo)
            ImageHandler.loadImageWithoutPlaceholder(ivAirline, airlineLogoList!![0],
                    ContextCompat.getDrawable(context, R.drawable.flight_ic_airline_default)
            )
            addView(view)
        }
    }
}