package com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 2019-05-14.
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
data class AutocompleteGeocodeResultUiModel (
        var name: String = "",
        var placeId: String = "",
        var vicinity: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(placeId)
        parcel.writeString(vicinity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AutocompleteGeocodeResultUiModel> {
        override fun createFromParcel(parcel: Parcel): AutocompleteGeocodeResultUiModel {
            return AutocompleteGeocodeResultUiModel(parcel)
        }

        override fun newArray(size: Int): Array<AutocompleteGeocodeResultUiModel?> {
            return arrayOfNulls(size)
        }
    }

}