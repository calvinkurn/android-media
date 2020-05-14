package com.tokopedia.travel.country_code.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.travel.country_code.presentation.adapter.viewholder.PhoneCodePickerViewHolder
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode

/**
 * @author by furqan on 23/12/2019
 */
class PhoneCodePickerAdapterTypeFactory : BaseAdapterTypeFactory() {

    fun type(viewModel: TravelCountryPhoneCode): Int = PhoneCodePickerViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            if (type == PhoneCodePickerViewHolder.LAYOUT) {
                PhoneCodePickerViewHolder(parent)
            } else {
                super.createViewHolder(parent, type)
            }

}