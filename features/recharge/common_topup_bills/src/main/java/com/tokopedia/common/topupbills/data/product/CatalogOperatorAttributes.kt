package com.tokopedia.common.topupbills.data.product

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common.topupbills.data.prefix_select.RechargePrefix
import kotlinx.android.parcel.Parcelize

/**
 * Created by resakemal on 28/11/19.
 */
@Parcelize
class CatalogOperatorAttributes(
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("image")
        @Expose
        val image: String = "",
        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("help_cta")
        @Expose
        val helpCta: String = "",
        @SerializedName("help_text")
        @Expose
        val helpText: String = "",
        @SerializedName("help_image")
        @Expose
        val helpImage: String = "",
        @SerializedName("operator_labels")
        @Expose
        val operatorLabel: List<String> = listOf(),
        @SerializedName("prefix")
        @Expose
        val prefix: List<RechargePrefix> = listOf(),
        @SerializedName("product_descriptions")
        @Expose
        val productDescriptions: List<String> = listOf(),
        @SerializedName("operator_descriptions")
        @Expose
        val operatorDescriptions: List<String> = listOf()

) : Parcelable