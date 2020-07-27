package com.tokopedia.topupbills.telco.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by nabillasabbaha on 10/05/19.
 */
@Parcelize
data class TelcoProduct(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("attributes")
        @Expose
        val attributes: TelcoAttributesProduct = TelcoAttributesProduct(),
        var isTitle: Boolean = false,
        var titleSection: String = "")
    : Parcelable