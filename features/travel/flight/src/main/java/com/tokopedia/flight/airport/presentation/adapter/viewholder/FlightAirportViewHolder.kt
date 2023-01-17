package com.tokopedia.flight.airport.presentation.adapter.viewholder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.presentation.model.FlightAirportModel
import com.tokopedia.flight.databinding.ItemFlightAirportBinding
import com.tokopedia.unifyprinciples.Typography.Companion.REGULAR

/**
 * Created by zulfikarrahman on 10/24/17.
 */
class FlightAirportViewHolder(val binding: ItemFlightAirportBinding,
                              private val filterTextListener: FlightAirportClickListener)
    : AbstractViewHolder<FlightAirportModel>(binding.root) {

    private val boldColor: ForegroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_96))

    override fun bind(airport: FlightAirportModel) {
        val context = itemView.context
        with(binding){
            val filterText = filterTextListener.getFilterText()
            if (filterText.isNotEmpty()) {
                city.setWeight(REGULAR)
                this.airport.setWeight(REGULAR)
            }
            val cityStr = context.getString(
                R.string.flight_label_city,
                airport.cityName, airport.countryName
            )
            city.text = getSpandableBoldText(cityStr, filterText)
            if (airport.cityCode.isEmpty()) {
                val airportString = context.getString(
                    R.string.flight_label_airport,
                    airport.airportCode, airport.airportName
                )
                this.airport.text = getSpandableBoldText(airportString, filterText)
            } else {
                val airportString = context.getString(R.string.flight_labe_all_airport, airport.cityName)
                this.airport.text = airportString
            }
            root.setOnClickListener { filterTextListener.airportClicked(airport) }
        }
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
