package com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 2019-05-14.
 */
data class AutocompleteGeocodeResponseUiModel (
        var status: String = "",
        var data: AutocompleteGeocodeDataUiModel = AutocompleteGeocodeDataUiModel()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readParcelable(AutocompleteGeocodeDataUiModel::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(status)
        parcel.writeParcelable(data, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AutocompleteGeocodeResponseUiModel> {
        override fun createFromParcel(parcel: Parcel): AutocompleteGeocodeResponseUiModel {
            return AutocompleteGeocodeResponseUiModel(parcel)
        }

        override fun newArray(size: Int): Array<AutocompleteGeocodeResponseUiModel?> {
            return arrayOfNulls(size)
        }
    }
}