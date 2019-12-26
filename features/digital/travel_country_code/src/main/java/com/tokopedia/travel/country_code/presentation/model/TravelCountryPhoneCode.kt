package com.tokopedia.travel.country_code.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.travel.country_code.presentation.adapter.PhoneCodePickerAdapterTypeFactory

/**
 * @author by furqan on 23/12/2019
 */
data class TravelCountryPhoneCode(
        val countryId: String = "",
        val countryName: String = "",
        val countryPhoneCode: Int = 0
) : Parcelable, Visitable<PhoneCodePickerAdapterTypeFactory> {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun type(typeFactory: PhoneCodePickerAdapterTypeFactory): Int =
            typeFactory.type(this)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(countryId)
        parcel.writeString(countryName)
        parcel.writeInt(countryPhoneCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TravelCountryPhoneCode> {
        override fun createFromParcel(parcel: Parcel): TravelCountryPhoneCode {
            return TravelCountryPhoneCode(parcel)
        }

        override fun newArray(size: Int): Array<TravelCountryPhoneCode?> {
            return arrayOfNulls(size)
        }
    }

}