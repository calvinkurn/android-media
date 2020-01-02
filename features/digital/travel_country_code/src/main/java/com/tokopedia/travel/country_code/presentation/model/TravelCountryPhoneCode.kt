package com.tokopedia.travel.country_code.presentation.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.travel.country_code.presentation.adapter.PhoneCodePickerAdapterTypeFactory
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 23/12/2019
 */
@Parcelize
data class TravelCountryPhoneCode(
        var countryId: String = "",
        var countryName: String = "",
        var countryPhoneCode: Int = 0
) : Parcelable, Visitable<PhoneCodePickerAdapterTypeFactory> {

    override fun type(typeFactory: PhoneCodePickerAdapterTypeFactory): Int =
            typeFactory.type(this)

}