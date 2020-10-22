package com.tokopedia.filter.common.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Sort(@SerializedName("name")
           @Expose
           var name: String = "",

           @SerializedName("key")
           @Expose
           var key: String = "",

           @SerializedName("value")
           @Expose
           var value: String = "",

           @SerializedName(value = "input_type", alternate = ["inputType"])
           @Expose
           private var inputType: String = "",

           @SerializedName("applyFilter")
           @Expose
           var applyFilter: String = "") : Parcelable {

    override fun toString(): String {
        return name
    }
}
