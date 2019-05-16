package com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 2019-05-14.
 */
data class AutoCompleteGeocodeResponseUiModel (
        var status: String = "",
        var data: AutoCompleteGeocodeDataUiModel = AutoCompleteGeocodeDataUiModel()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readParcelable(AutoCompleteGeocodeDataUiModel::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(status)
        parcel.writeParcelable(data, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AutoCompleteGeocodeResponseUiModel> {
        override fun createFromParcel(parcel: Parcel): AutoCompleteGeocodeResponseUiModel {
            return AutoCompleteGeocodeResponseUiModel(parcel)
        }

        override fun newArray(size: Int): Array<AutoCompleteGeocodeResponseUiModel?> {
            return arrayOfNulls(size)
        }
    }
}