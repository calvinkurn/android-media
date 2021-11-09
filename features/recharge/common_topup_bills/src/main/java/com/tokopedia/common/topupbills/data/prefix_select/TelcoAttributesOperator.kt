package com.tokopedia.common.topupbills.data.prefix_select

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by nabillasabbaha on 10/05/19.
 */
@Parcelize
data class TelcoAttributesOperator(
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",
        @SerializedName("default_product_id")
        @Expose
        val defaultProductId: String = "0"
): Parcelable