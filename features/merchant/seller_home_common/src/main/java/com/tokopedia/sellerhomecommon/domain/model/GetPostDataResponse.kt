package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 21/05/20
 */

data class GetPostDataResponse(
        @SerializedName("fetchPostWidgetData")
        @Expose
        val getPostWidgetData: GetPostDataModel?
)

data class GetPostDataModel(
        @SerializedName("data")
        @Expose
        val data: List<PostDataModel>?
)

data class PostDataModel(
        @SerializedName("datakey")
        @Expose
        val dataKey: String? = "",
        @SerializedName("list")
        @Expose
        val list: List<PostItemDataModel>? = emptyList(),
        @SerializedName("cta")
        @Expose
        val cta: PostCtaDataModel? = PostCtaDataModel(),
        @SerializedName("errorMsg")
        @Expose
        val error: String? = "",
        @SerializedName("emphasizeType")
        @Expose
        val emphasizeType: Int? = 0,
        @SerializedName("showWidget")
        @Expose
        val showWidget: Boolean? = true
)

data class PostItemDataModel(
        @SerializedName("title")
        @Expose
        val title: String?,
        @SerializedName("url")
        @Expose
        val url: String?,
        @SerializedName("applink")
        @Expose
        val appLink: String?,
        @SerializedName("subtitle")
        @Expose
        val subtitle: String?,
        @SerializedName("featuredMediaURL")
        @Expose
        val featuredMediaURL: String?,
        @SerializedName("stateText")
        @Expose
        val stateText: String? = "",
        @SerializedName("stateMediaURL")
        @Expose
        val stateMediaUrl: String? = "",
        @SerializedName("pinned")
        @Expose
        val isPinned: Boolean = false
)

data class PostCtaDataModel(
        @SerializedName("text")
        @Expose
        val text: String? = "",
        @SerializedName("applink")
        @Expose
        val appLink: String? = "",
)