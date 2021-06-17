package com.tokopedia.flight.airport.presentation.adapter.viewholder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.presentation.model.FlightAirportModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.Typography.Companion.REGULAR

/**
 * Created by zulfikarrahman on 10/24/17.
 */
class FlightAirportViewHolder(itemView: View,
                              private val filterTextListener: FlightAirportClickListener)
    : AbstractViewHolder<FlightAirportModel>(itemView) {

    private val cityTextView: Typography = itemView.findViewById(R.id.city)
    private val airportTextView: Typography = itemView.findViewById(R.id.airport)
    private val boldColor: ForegroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_96))

    override fun bind(airport: FlightAirportModel) {
        val context = itemView.context
        val filterText = filterTextListener.getFilterText()
        if (filterText.isNotEmpty()) {
            cityTextView.setWeight(REGULAR)
            airportTextView.setWeight(REGULAR)
        }
        val cityStr = context.getString(R.string.flight_label_city,
                airport.cityName, airport.countryName)
        cityTextView.text = getSpandableBoldText(cityStr, filterText)
        if (!TextUtils.isEmpty(airport.airportCode)) {
            val airportString = context.getString(R.string.flight_label_airport,
                    airport.airportCode, airport.airportName)
            airportTextView.text = getSpandableBoldText(airportString, filterText)
        } else {
            val airportString = context.getString(R.string.flight_labe_all_airport)
            airportTextView.text = airportString
        }
        itemView.setOnClickListener { filterTextListener.airportClicked(airport) }
    }

    private fun getSpandableBoldText(strToPut: String, stringToBold: String): CharSequence {
        var indexStartBold = 0
        val indexEndBold = strToPut.length
        if (TextUtils.isEmpty(stringToBold)) {
            return strToPut
        }
        val strToPutLowerCase = strToPut.toLowerCase()
        val strToBoldLowerCase = stringToBold.toLowerCase()
        val spannableStringBuilder = SpannableStringBuilder(strToPut)
        return try {
            if (strToPutLowerCase.contains(strToBoldLowerCase)) {
                indexStartBold = strToPutLowerCase.indexOf(strToBoldLowerCase) + stringToBold.length
            }
            if (indexStartBold < strToPut.length) {
                spannableStringBuilder.setSpan(StyleSpan(Typeface.BOLD),
                        indexStartBold, indexEndBold, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableStringBuilder.setSpan(boldColor, indexStartBold, indexEndBold,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableStringBuilder
            } else {
                spannableStringBuilder
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            spannableStringBuilder
        }
    }

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_flight_airport
    }
}