package com.tokopedia.oldcatalog.model.raw

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class CatalogResponseData(
    @Expose
    @SerializedName("catalogGetDetailModular")
    val catalogGetDetailModular: CatalogGetDetailModular?
) {
    data class CatalogGetDetailModular(
        @Expose
        @SerializedName("basicInfo")
        val basicInfo: BasicInfo,
        @Expose
        @SerializedName("globalStyle")
        val globalStyle: GlobalStyle?,
        @Expose
        @SerializedName("comparisonInfo")
        val comparisonInfoComponentData: ComponentData?,
        @Expose
        @SerializedName("components")
        val components: List<BasicInfo.Component>?,
        @Expose
        @SerializedName("layout")
        val layouts: List<BasicInfo.Layout>?
    ) {
        data class BasicInfo(
            @Expose
            @SerializedName("id")
            val id: String,

            @Expose
            @SerializedName("departmentId")
            val departmentID: String?,

            @Expose
            @SerializedName("name")
            val name: String?,
            @Expose
            @SerializedName("brand")
            val brand: String?,
            @Expose
            @SerializedName("tag")
            val tag: String?,
            @Expose
            @SerializedName("description")
            val description: String?,
            @Expose
            @SerializedName("shortDescription")
            val shortDescription: String?,
            @Expose
            @SerializedName("url")
            val url: String?,

            @Expose
            @SerializedName("mobileUrl")
            val mobileURL: String?,

            @Expose
            @SerializedName("catalogImage")
            val catalogImage: ArrayList<CatalogImage>?,
            @Expose
            @SerializedName("marketPrice")
            val marketPrice: List<MarketPrice>?,
            @Expose
            @SerializedName("longDescription")
            val longDescription: List<LongDescription>?,
            @Expose
            @SerializedName("productSortingStatus")
            val productSortingStatus: Int?
        ) {

            data class LongDescription(
                @Expose
                @SerializedName("Title")
                val title: String,

                @Expose
                @SerializedName("Description")
                val description: String
            )

            data class MarketPrice(
                @Expose
                @SerializedName("min")
                val min: Long?,
                @Expose
                @SerializedName("max")
                val max: Long?,

                @Expose
                @SerializedName("minFmt")
                val minFmt: String?,

                @Expose
                @SerializedName("maxFmt")
                val maxFmt: String?,

                @Expose
                @SerializedName("date")
                val date: String?,
                @Expose
                @SerializedName("name")
                val name: String?
            )

            data class Component(
                @Expose
                @SerializedName("id")
                val id: String,
                @Expose
                @SerializedName("name")
                val name: String,
                @Expose
                @SerializedName("type")
                val type: String,
                @Expose
                @SerializedName("sticky")
                val sticky: Boolean?,
                @Expose
                @SerializedName("data")
                val data: List<ComponentData>?
            )

            data class Layout(
                @Expose
                @SerializedName("name")
                val name: String,
                @Expose
                @SerializedName("type")
                val type: String,
                @Expose
                @SerializedName("data")
                val data: LayoutData?
            )
        }

        data class GlobalStyle (
            @SerializedName("darkMode")
            val darkMode: Boolean?,
            @SerializedName("presetKey")
            val presetKey: String?,
            @SerializedName("bgColor")
            val bgColor: String?,
            @SerializedName("primaryColor")
            val primaryColor: String?,
            @SerializedName("secondaryColor")
            val secondaryColor: String?
        )
    }
}

@Parcelize
data class CatalogImage(
    @Expose
    @SerializedName("imageUrl")
    val imageURL: String?,
    @Expose
    @SerializedName("isPrimary")
    val isPrimary: Boolean?,
    @Expose
    @SerializedName("applink")
    val appLink: String?
) : Parcelable
