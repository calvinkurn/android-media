package com.tokopedia.filter.common.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class DynamicFilterModel(@SerializedName("data")
                         @Expose
                         var data: DataValue = DataValue(),
                         @SerializedName("process_time")
                         @Expose
                         var processTime: String = "",
                         @SerializedName("status")
                         @Expose
                         var status: String = "",
                         @SerializedName("isOfficialSelectedFlag")
                         @Expose
                         var isOfficialSelectedFlag: Boolean = false) : Parcelable {

    fun getIsOfficialSelectedFlag(): Boolean {
        return isOfficialSelectedFlag
    }

    fun isEmpty(): Boolean {
        return data.filter.map { it.options }.flatten().isEmpty()
    }
}
