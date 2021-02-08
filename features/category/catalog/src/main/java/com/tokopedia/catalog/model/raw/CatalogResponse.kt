package com.tokopedia.catalog.model.raw

import com.google.gson.annotations.SerializedName

data class CatalogResponse (
        val data: CatalogResponseData
) {
    data class CatalogResponseData (
            val catalogGetDetailModular: CatalogGetDetailModular
    ){
        data class CatalogGetDetailModular (
                val header: Header,
                val basicInfo: BasicInfo
        )
        {
            data class Header (
                    val code: Long,
                    val message: String
            )

            data class BasicInfo (
                    val id: String,

                    @SerializedName( "departmentId")
                    val departmentID: String,

                    val name: String,
                    val brand: String,
                    val tag: String,
                    val description: String,
                    val url: String,

                    @SerializedName( "mobileUrl")
                    val mobileURL: String,

                    val catalogImage: List<CatalogImage>,
                    val marketPrice: List<MarketPrice>,
                    val longDescription: List<LongDescription>,
                    val components: List<Component>
            ){

                data class LongDescription (
                        @SerializedName( "Title")
                        val title: String,

                        @SerializedName( "Description")
                        val description: String
                )

                data class MarketPrice (
                        val min: Long,
                        val max: Long,

                        @SerializedName( "min_fmt")
                        val minFmt: String,

                        @SerializedName( "max_fmt")
                        val maxFmt: String,

                        val date: String,
                        val name: String
                )

                data class CatalogImage (
                        @SerializedName( "imageUrl")
                        val imageURL: String,

                        val isPrimary: Boolean
                )

                data class Component (
                        val id: Long,
                        val name: String,
                        val type: String,
                        val sticky: Boolean,
                        val data: List<ComponentData>
                ){

                }
            }
        }
    }
}
