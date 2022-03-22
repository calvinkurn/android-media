package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Channel(
        @Expose @SerializedName("header") val header: Header? = null,
        @Expose @SerializedName("hero") val hero: Hero? = null,
        @Expose @SerializedName("banner") val banner: Banner? = null,
        @Expose @SerializedName("id") val id: String = "",
        @Expose @SerializedName("name") val name: String = "",
        @Expose @SerializedName("layout") val layout: String = "",
        @Expose @SerializedName("widgetParam") val widgetParam: String = "",
        @Expose @SerializedName("contextualInfo") val contextualInfo: Int = 0,
        @Expose @SerializedName("campaignID") val campaignID: Int = -1,
        @Expose @SerializedName("campaignCode") val campaignCode: String = "",
        @Expose @SerializedName("grids") val grids: List<Grid> = listOf(),
        @Expose @SerializedName("galaxy_attribution") val galaxyAttribution: String = "",
        @Expose @SerializedName("persona") val persona: String = "",
        @Expose @SerializedName("category_persona") val categoryPersona: String = "",
        @Expose @SerializedName("brand_id") val brandId: String = "",
        @Expose @SerializedName("pageName") val pageName: String = "",
        @Expose @SerializedName("viewAllCard") val viewAllCard: ViewAllCard = ViewAllCard(),
) : Parcelable
