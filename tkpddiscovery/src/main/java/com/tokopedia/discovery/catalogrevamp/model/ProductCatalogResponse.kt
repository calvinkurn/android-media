package com.tokopedia.discovery.catalogrevamp.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ProductCatalogResponse(
    @SerializedName("ProductCatalogQuery")
    val productCatalogQuery: ProductCatalogQuery
) {
    data class ProductCatalogQuery(
        @SerializedName("data")
        val `data`: Data,
        @SerializedName("header")
        val header: Header
    ) {
        data class Header(
            @SerializedName("process_time")
            val processTime: Double,
            @SerializedName("status")
            val status: String
        )

        data class Data(
            @SerializedName("catalog")
            val catalog: Catalog
        ) {
            data class Catalog(
                @SerializedName("catalog_breadcrumb")
                val catalogBreadcrumb: ArrayList<CatalogBreadcrumb>,
                @SerializedName("catalog_image")
                val catalogImage: ArrayList<CatalogImage>,
                @SerializedName("catalog_video")
                val catalogVideo: ArrayList<CatalogVideo>,
                @SerializedName("create_by")
                val createBy: Int,
                @SerializedName("create_time")
                val createTime: String,
                @SerializedName("department_id")
                val departmentId: String,
                @SerializedName("description")
                val description: String,
                @SerializedName("id")
                val id: String,
                @SerializedName("identifier")
                val identifier: String,
                @SerializedName("long_description")
                val longDescription: ArrayList<LongDescription>,
                @SerializedName("market_price")
                val marketPrice: ArrayList<MarketPrice>,
                @SerializedName("mobile_url")
                val mobileUrl: String,
                @SerializedName("name")
                val name: String,
                @SerializedName("release_date")
                val releaseDate: String,
                @SerializedName("specification")
                val specification: ArrayList<Specification>,
                @SerializedName("status")
                val status: Int,
                @SerializedName("topthreespec")
                val topthreespec: ArrayList<Topthreespec>,
                @SerializedName("update_by")
                val updateBy: Int,
                @SerializedName("update_time")
                val updateTime: String,
                @SerializedName("url")
                val url: String
            ) {
                data class Topthreespec(
                    @SerializedName("value")
                    val value: String
                )

                data class LongDescription(
                    @SerializedName("description")
                    val description: String,
                    @SerializedName("title")
                    val title: String
                )

                data class CatalogImage(
                    @SerializedName("image_url")
                    val imageUrl: String,
                    @SerializedName("image_url_700")
                    val imageUrl700: String?,
                    @SerializedName("is_primary")
                    val isPrimary: Boolean
                ) : Parcelable {
                    constructor(parcel: Parcel) : this(
                            parcel.readString(),
                            parcel.readString(),
                            parcel.readByte() != 0.toByte())

                    override fun writeToParcel(parcel: Parcel, flags: Int) {
                        parcel.writeString(imageUrl)
                        parcel.writeString(imageUrl700)
                        parcel.writeByte(if (isPrimary) 1 else 0)
                    }

                    override fun describeContents(): Int {
                        return 0
                    }

                    companion object CREATOR : Parcelable.Creator<CatalogImage> {
                        override fun createFromParcel(parcel: Parcel): CatalogImage {
                            return CatalogImage(parcel)
                        }

                        override fun newArray(size: Int): Array<CatalogImage?> {
                            return arrayOfNulls(size)
                        }
                    }
                }

                data class MarketPrice(
                    @SerializedName("date")
                    val date: String,
                    @SerializedName("max")
                    val max: Int,
                    @SerializedName("max_fmt")
                    val maxFmt: String,
                    @SerializedName("min")
                    val min: Int,
                    @SerializedName("min_fmt")
                    val minFmt: String,
                    @SerializedName("name")
                    val name: String
                )

                data class CatalogBreadcrumb(
                    @SerializedName("mobile_url")
                    val mobileUrl: String,
                    @SerializedName("name")
                    val name: String,
                    @SerializedName("url")
                    val url: String
                )

                data class CatalogVideo(
                    @SerializedName("video_url")
                    val videoUrl: String
                )

                data class Specification(
                    @SerializedName("name")
                    val name: String,
                    @SerializedName("row")
                    val row: ArrayList<Row>
                ) : Parcelable {
                    constructor(parcel: Parcel) : this(
                            parcel.readString(),
                            parcel.createTypedArrayList(Row))

                    data class Row(
                        @SerializedName("key")
                        val key: String,
                        @SerializedName("value")
                        val value: ArrayList<String>
                    ) : Parcelable {
                        constructor(parcel: Parcel) : this(
                                parcel.readString(),
                                parcel.createStringArrayList())

                        override fun writeToParcel(parcel: Parcel, flags: Int) {
                            parcel.writeString(key)
                        }

                        override fun describeContents(): Int {
                            return 0
                        }

                        companion object CREATOR : Parcelable.Creator<Row> {
                            override fun createFromParcel(parcel: Parcel): Row {
                                return Row(parcel)
                            }

                            override fun newArray(size: Int): Array<Row?> {
                                return arrayOfNulls(size)
                            }
                        }
                    }

                    override fun writeToParcel(parcel: Parcel, flags: Int) {
                        parcel.writeString(name)
                    }

                    override fun describeContents(): Int {
                        return 0
                    }

                    companion object CREATOR : Parcelable.Creator<Specification> {
                        override fun createFromParcel(parcel: Parcel): Specification {
                            return Specification(parcel)
                        }

                        override fun newArray(size: Int): Array<Specification?> {
                            return arrayOfNulls(size)
                        }
                    }
                }
            }
        }
    }
}