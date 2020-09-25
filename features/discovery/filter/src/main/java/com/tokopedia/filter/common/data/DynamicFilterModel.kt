package com.tokopedia.filter.common.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.common.constants.SearchApiConst
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
                         var isOfficialSelectedFlag: Boolean = false,
                         var defaultSortValue: String = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
) : Parcelable {

    fun getIsOfficialSelectedFlag(): Boolean {
        return isOfficialSelectedFlag
    }

    fun isEmpty(): Boolean {
        return data.filter.map { it.options }.flatten().isEmpty()
    }

    fun hasSort() = data.sort.isNotEmpty()

    fun getSortKey() = if (hasSort()) data.sort[0].key else ""
}
