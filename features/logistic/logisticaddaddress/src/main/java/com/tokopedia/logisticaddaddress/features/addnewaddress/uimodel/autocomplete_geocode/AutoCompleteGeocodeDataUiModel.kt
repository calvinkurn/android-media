package com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 2019-05-14.
 */
data class AutoCompleteGeocodeDataUiModel (
        var results: List<AutoCompleteGeocodeResultUiModel> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(AutoCompleteGeocodeResultUiModel)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(results)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AutoCompleteGeocodeDataUiModel> {
        override fun createFromParcel(parcel: Parcel): AutoCompleteGeocodeDataUiModel {
            return AutoCompleteGeocodeDataUiModel(parcel)
        }

        override fun newArray(size: Int): Array<AutoCompleteGeocodeDataUiModel?> {
            return arrayOfNulls(size)
        }
    }
}