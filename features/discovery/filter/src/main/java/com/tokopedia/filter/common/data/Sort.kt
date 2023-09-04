package com.tokopedia.filter.common.data

import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.bottomsheet.filtergeneraldetail.GeneralFilterSortOptions
import kotlinx.android.parcel.Parcelize

@Parcelize
class Sort(@SerializedName("name")
           @Expose
           override var name: String = "",

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
           var applyFilter: String = "",

           override var inputState: String = "",

) : Parcelable, GeneralFilterSortOptions {

    override val uniqueId: String
        get() = key + Option.UID_FIRST_SEPARATOR_SYMBOL + value + Option.UID_SECOND_SEPARATOR_SYMBOL + name

    override fun toString(): String {
        return name
    }
}
