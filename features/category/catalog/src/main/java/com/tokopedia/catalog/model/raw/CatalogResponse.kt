package com.tokopedia.catalog.model.raw

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class CatalogResponseData (
        @SerializedName( "catalogGetDetailModular")
        val catalogGetDetailModular: CatalogGetDetailModular
){
    data class CatalogGetDetailModular (
            @SerializedName( "basicInfo")
            val basicInfo: BasicInfo,
            @SerializedName( "components")
            val components: List<BasicInfo.Component>
    )
    {
        data class BasicInfo (
                @SerializedName( "id")
                val id: String,

                @SerializedName( "departmentId")
                val departmentID: String,

                @SerializedName( "name")
                val name: String,
                @SerializedName( "brand")
                val brand: String,
                @SerializedName( "tag")
                val tag: String,
                @SerializedName( "description")
                val description: String,
                @SerializedName( "url")
                val url: String,

                @SerializedName( "mobileUrl")
                val mobileURL: String,

                @SerializedName( "catalogImage")
                val catalogImage: ArrayList<CatalogImage>,
                @SerializedName( "marketPrice")
                val marketPrice: List<MarketPrice>,
                @SerializedName( "longDescription")
                val longDescription: List<LongDescription>
        ){

            data class LongDescription (
                    @SerializedName( "Title")
                    val title: String,

                    @SerializedName( "Description")
                    val description: String
            )

            data class MarketPrice (
                    @SerializedName( "min")
                    val min: Long,
                    @SerializedName( "max")
                    val max: Long,

                    @SerializedName( "min_fmt")
                    val minFmt: String,

                    @SerializedName( "max_fmt")
                    val maxFmt: String,

                    @SerializedName( "date")
                    val date: String,
                    @SerializedName( "name")
                    val name: String
            )


            data class Component (
                    @SerializedName( "id")
                    val id: Long,
                    @SerializedName( "name")
                    val name: String,
                    @SerializedName( "type")
                    val type: String,
                    @SerializedName( "sticky")
                    val sticky: Boolean,
                    @SerializedName( "data")
                    val data: List<ComponentData>
            ){

            }
        }
    }
}

@Parcelize
data class CatalogImage (
        @SerializedName( "imageUrl")
        val imageURL: String,
        @SerializedName( "isPrimary")
        val isPrimary: Boolean
): Parcelable

