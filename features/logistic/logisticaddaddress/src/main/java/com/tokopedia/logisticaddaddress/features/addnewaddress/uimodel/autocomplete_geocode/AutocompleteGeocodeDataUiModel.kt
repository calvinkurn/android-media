package com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 2019-05-14.
 */
data class AutocompleteGeocodeDataUiModel (
        var results: List<AutocompleteGeocodeResultUiModel> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(AutocompleteGeocodeResultUiModel)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(results)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AutocompleteGeocodeDataUiModel> {
        override fun createFromParcel(parcel: Parcel): AutocompleteGeocodeDataUiModel {
            return AutocompleteGeocodeDataUiModel(parcel)
        }

        override fun newArray(size: Int): Array<AutocompleteGeocodeDataUiModel?> {
            return arrayOfNulls(size)
        }
    }
}