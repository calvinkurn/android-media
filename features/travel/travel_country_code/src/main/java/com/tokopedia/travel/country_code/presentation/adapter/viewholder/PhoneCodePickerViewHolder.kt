package com.tokopedia.travel.country_code.presentation.adapter.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travel.country_code.R
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode

/**
 * @author by furqan on 23/12/2019
 */
class PhoneCodePickerViewHolder(itemView: View) : AbstractViewHolder<TravelCountryPhoneCode>(itemView) {

    private val tvCountryPhoneCode: TextView = itemView.findViewById(R.id.country_phone_code)
    private val tvCountryName: TextView = itemView.findViewById(R.id.country_name)

    override fun bind(element: TravelCountryPhoneCode) {
        tvCountryName.text = element.countryName
        tvCountryPhoneCode.text = getString(R.string.travel_phone_code_picker_label, element.countryPhoneCode.toString())
    }

    companion object {
        val LAYOUT = R.layout.item_phone_code_picker
    }
}