package com.tokopedia.topupbills.telco.data

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TelcoFavNumber(

        @SerializedName("client_number")
        @Expose
        var clientNumber: String = "",
        @SerializedName("label")
        @Expose
        var label: String= "",
        @SerializedName("product_id")
        @Expose
        var productId: String = "",
        @SerializedName("operator_id")
        @Expose
        var operatorId: String = "",
        @SerializedName("category_id")
        @Expose
        var categoryId: String = "",
        @SerializedName("favorite")
        @Expose
        var isFavorite: Boolean = true) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(clientNumber)
        parcel.writeString(label)
        parcel.writeString(productId)
        parcel.writeString(operatorId)
        parcel.writeString(categoryId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TelcoFavNumber> {
        override fun createFromParcel(parcel: Parcel): TelcoFavNumber {
            return TelcoFavNumber(parcel)
        }

        override fun newArray(size: Int): Array<TelcoFavNumber?> {
            return arrayOfNulls(size)
        }
    }

    fun isEmpty(): Boolean {
        return (TextUtils.isEmpty(clientNumber)
                || TextUtils.isEmpty(productId) || productId.equals("0", ignoreCase = true)
                || TextUtils.isEmpty(categoryId) || categoryId.equals("0", ignoreCase = true)
                || TextUtils.isEmpty(operatorId) || operatorId.equals("0", ignoreCase = true))
    }
}