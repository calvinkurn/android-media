package com.tokopedia.hotel.search_map.data.model.params

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by jessica on 12/08/20
 */

@Parcelize
data class ParamFilterV2(
        @SerializedName("name")
        @Expose
        var name: String = "",

        @SerializedName("values")
        @Expose
        var values: MutableList<String> = mutableListOf()
): Parcelable