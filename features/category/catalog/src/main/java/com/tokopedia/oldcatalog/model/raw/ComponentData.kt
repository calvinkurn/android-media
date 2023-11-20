package com.tokopedia.oldcatalog.model.raw

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ComponentData(
    @SerializedName("name")
    val name: String?,
    @SerializedName("key")
    val key: String?,
    @SerializedName("value")
    val value: String?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("row")
    val specificationsRow: List<SpecificationsRow>? = listOf(),
    @SerializedName("url")
    val url: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("videoId")
    val videoId: String?,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("author")
    val author: String?,

    @SerializedName("id")
    val id: String?,
    @SerializedName("brand")
    val brand: String?,
    @SerializedName("catalogImage")
    val catalogImage: ArrayList<CatalogImage>?,
    @SerializedName("marketPrice")
    val marketPrice: List<CatalogResponseData.CatalogGetDetailModular.BasicInfo.MarketPrice>?,
    @SerializedName("topSpec")
    val topSpecifications: List<SpecificationsRow>?,
    @SerializedName("fullSpec")
    val fullSpecifications: List<ComponentData>?,

    @Expose
    @SerializedName("avgRating")
    val avgRating: String?,
    @Expose
    @SerializedName("reviews")
    val reviews: ArrayList<CatalogProductReviewResponse.CatalogGetProductReview.ReviewData.Review?>?,
    @Expose
    @SerializedName("totalHelpfulReview")
    val totalHelpfulReview: String?,
    @Expose
    @SerializedName("spec_list")
    var specList: ArrayList<SpecList>? = arrayListOf(),
    @Expose
    @SerializedName("compared_data")
    val comparedData: ComparedData? = null,
    @SerializedName("category_name")
    val categoryName: String?,
    @SerializedName("category_identifier")
    val categoryIdentifier: String?,
    @SerializedName("catalog_count")
    val catalogCount: String?,
    @SerializedName("catalogs")
    var catalogs: ArrayList<CatalogImage>? = arrayListOf()

) {
    @Parcelize
    data class SpecificationsRow(
        @SerializedName("key")
        val key: String,
        @SerializedName("value")
        val value: String
    ) : Parcelable

    @Parcelize
    data class SpecList(
        @Expose
        @SerializedName("title")
        val comparisonTitle: String?,
        @Expose
        @SerializedName("sub_card")
        val subcard: ArrayList<Subcard>? = arrayListOf(),
        @Expose
        @SerializedName("is_expanded")
        var isExpanded: Boolean? = false
    ) : Parcelable {
        @Parcelize
        data class Subcard(
            @SerializedName("sub_title")
            val subTitle: String?,
            @SerializedName("left_data")
            val leftData: String?,
            @SerializedName("right_data")
            val rightData: String?,

            val featureLeftData: ComparisonNewModel?,
            val featureRightData: ComparisonNewModel?
        ) : Parcelable
    }

    data class ComparedData(
        @SerializedName("id")
        val id: String?,
        @SerializedName("brand")
        val brand: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("url")
        val url: String?,
        @SerializedName("catalogImage")
        val catalogImage: ArrayList<CatalogImage>?,
        @SerializedName("marketPrice")
        val marketPrice: List<CatalogResponseData.CatalogGetDetailModular.BasicInfo.MarketPrice>?
    )
}

@Parcelize
data class FullSpecificationsComponentData(
    val name: String?,
    val icon: String?,
    val specificationsRow: List<ComponentData.SpecificationsRow>?
) : Parcelable

@Parcelize
data class TopSpecificationsComponentData(
    val key: String?,
    val value: String?,
    val icon: String?
) : Parcelable

@Parcelize
data class VideoComponentData(
    val url: String?,
    val type: String?,
    val videoId: String?,
    val thumbnail: String?,
    val title: String?,
    val author: String?
) : Parcelable

@Parcelize
data class ComparisionModel(
    val id: String?,
    val brand: String?,
    val name: String?,
    val price: String?,
    val url: String?,

    val key: String?,
    val value: String?
) : Parcelable

@Parcelize
data class ComparisonNewModel(
    val id: String?,
    val brand: String?,
    val name: String?,
    val price: String?,
    val imageUrl: String?,
    val appLink: String?
) : Parcelable

@Parcelize
data class ReviewComponentData(
    val catalogName: String?,
    val catalogId: String?,
    val avgRating: String?,
    val reviews: ArrayList<CatalogProductReviewResponse.CatalogGetProductReview.ReviewData.Review?>?,
    val totalHelpfulReview: String?
) : Parcelable
