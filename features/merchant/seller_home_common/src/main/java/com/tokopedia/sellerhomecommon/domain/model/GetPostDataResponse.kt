package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 21/05/20
 */

data class GetPostDataResponse(
    @SerializedName("fetchPostWidgetData")
    val getPostWidgetData: GetPostDataModel?
)

data class GetPostDataModel(
    @SerializedName("data")
    val data: List<PostDataModel>?
)

data class PostDataModel(
    @SerializedName("datakey")
    val dataKey: String? = "",
    @SerializedName("list")
    val list: List<PostItemDataModel>? = emptyList(),
    @SerializedName("cta")
    val cta: PostCtaDataModel? = PostCtaDataModel(),
    @SerializedName("errorMsg")
    val error: String? = "",
    @SerializedName("emphasizeType")
    val emphasizeType: Int? = 0,
    @SerializedName("showWidget")
    val showWidget: Boolean? = true
)

data class PostItemDataModel(
    @SerializedName("title")
    val title: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("applink")
    val appLink: String?,
    @SerializedName("subtitle")
    val subtitle: String?,
    @SerializedName("featuredMediaURL")
    val featuredMediaURL: String?,
    @SerializedName("stateText")
    val stateText: String? = "",
    @SerializedName("stateMediaURL")
    val stateMediaUrl: String? = "",
    @SerializedName("countdownDate")
    val countdownDate: String? = "",
    @SerializedName("pinned")
    val isPinned: Boolean = false
)

data class PostCtaDataModel(
    @SerializedName("text")
    val text: String? = "",
    @SerializedName("applink")
    val appLink: String? = "",
)
