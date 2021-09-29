package com.tokopedia.common.topupbills.data.prefix_select

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by nabillasabbaha on 10/05/19.
 */
@Parcelize
data class RechargeCatalogPrefixSelect(
        @SerializedName("componentID")
        @Expose
        val componentId: Int = 0,
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("paramName")
        @Expose
        val paramName: String = "",
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("help")
        @Expose
        val help: String = "",
        @SerializedName("placeholder")
        @Expose
        val placeholder: String = "",
        @SerializedName("validations")
        @Expose
        val validations: List<RechargeValidation> = listOf(),
        @SerializedName("prefixes")
        @Expose
        val prefixes: List<RechargePrefix> = listOf()
): Parcelable

@Parcelize
data class RechargePrefix(
        @SerializedName("key")
        @Expose
        val key: String = "",
        @SerializedName("value")
        @Expose
        val value: String = "",
        @SerializedName("operator")
        @Expose
        val operator: TelcoOperator = TelcoOperator()
): Parcelable

@Parcelize
data class RechargeValidation(
        @SerializedName("id")
        @Expose
        val id: String = "0",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("rule")
        @Expose
        val rule: String = ""
): Parcelable