package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO

/**
 * Created By @ilhamsuaib on 21/05/20
 */

data class GetPostDataResponse(
    @SerializedName("fetchPostWidgetData")
    val getPostWidgetData: GetPostDataModel = GetPostDataModel()
)

data class GetPostDataModel(
    @SerializedName("data")
    val data: List<PostDataModel> = emptyList()
)

data class PostDataModel(
    @SerializedName("datakey")
    val dataKey: String = String.EMPTY,
    @SerializedName("list")
    val list: List<PostItemDataModel> = emptyList(),
    @SerializedName("cta")
    val cta: PostCtaDataModel = PostCtaDataModel(),
    @SerializedName("errorMsg")
    val error: String = String.EMPTY,
    @SerializedName("emphasizeType")
    val emphasizeType: Int? = Int.ZERO,
    @SerializedName("showWidget")
    val showWidget: Boolean = true,
    @SerializedName("widgetDataSign")
    val widgetDataSign: String = String.EMPTY,
)

data class PostItemDataModel(
    @SerializedName("title")
    val title: String = String.EMPTY,
    @SerializedName("url")
    val url: String = String.EMPTY,
    @SerializedName("applink")
    val appLink: String = String.EMPTY,
    @SerializedName("subtitle")
    val subtitle: String = String.EMPTY,
    @SerializedName("featuredMediaURL")
    val featuredMediaURL: String = String.EMPTY,
    @SerializedName("stateText")
    val stateText: String = String.EMPTY,
    @SerializedName("stateMediaURL")
    val stateMediaUrl: String = String.EMPTY,
    @SerializedName("pinned")
    val isPinned: Boolean = false,
    @SerializedName("postItemID")
    val postItemID: String = String.EMPTY,
    @SerializedName("countdownDate")
    val countdownDate: String? = String.EMPTY,
)

data class PostCtaDataModel(
    @SerializedName("text")
    val text: String = String.EMPTY,
    @SerializedName("applink")
    val appLink: String = String.EMPTY
)
