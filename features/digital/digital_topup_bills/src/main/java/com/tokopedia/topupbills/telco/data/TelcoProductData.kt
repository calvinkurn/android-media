package com.tokopedia.topupbills.telco.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 10/05/19.
 */
class TelcoProductData(
        @SerializedName("data_collections")
        @Expose
        val productDataCollections: List<TelcoProductDataCollection>)
        : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.createTypedArrayList(TelcoProductDataCollection.CREATOR))

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(this.productDataCollections)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TelcoProductData> {
        override fun createFromParcel(parcel: Parcel): TelcoProductData {
            return TelcoProductData(parcel)
        }

        override fun newArray(size: Int): Array<TelcoProductData?> {
            return arrayOfNulls(size)
        }
    }
}