package com.tokopedia.filter.common.data

import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.helper.toMapParam
import kotlinx.android.parcel.Parcelize

@Parcelize
class Sort(@SerializedName("name")
           @Expose
           override var name: String = "",

           @SerializedName("key")
           @Expose
           override var key: String = "",

           @SerializedName("value")
           @Expose
           override var value: String = "",

           @SerializedName(value = "input_type", alternate = ["inputType"])
           @Expose
           override var inputType: String = "",

           @SerializedName("applyFilter")
           @Expose
           var applyFilter: String = "",

           override var inputState: String = "",

) : Parcelable, IOption {

    override val isNew: Boolean
        get() = false

    override val iconUrl: String
        get() = ""

    override val hexColor: String
        get() = ""

    override val description: String
        get() = ""

    override fun toString(): String {
        return name
    }

    fun toMapParam(): Map<String, String> =
        mapOf(key to value) + applyFilter.toMapParam()
}
