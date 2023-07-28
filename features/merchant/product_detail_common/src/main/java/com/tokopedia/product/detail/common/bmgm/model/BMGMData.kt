package com.tokopedia.product.detail.common.bmgm.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.bmgm.ui.model.BMGMUiModel

/**
 * Created by yovi.putra on 28/07/23"
 * Project name: android-tokopedia-core
 **/

data class BMGMData(
    @SerializedName("backgroundColor")
    @Expose
    val backgroundColor: String = "",
    @SerializedName("titleColor")
    @Expose
    val titleColor: String = "",
    @SerializedName("iconUrl")
    @Expose
    val iconUrl: String = "",
    @SerializedName("titles")
    @Expose
    val titles: List<String> = emptyList(),
    @SerializedName("action")
    @Expose
    val action: Action = Action(),
    @SerializedName("contents")
    @Expose
    val contents: List<Content> = emptyList(),
    @SerializedName("loadMoreText")
    @Expose
    val loadMoreText: String = "",
) {

    data class Action(
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("link")
        @Expose
        val link: String = "",
    )

    data class Content(
        @SerializedName("imageUrl")
        @Expose
        val imageUrl: String = "",
    )
}

fun BMGMData.Content.asUiModel() = BMGMUiModel.Product(
    imageUrl = imageUrl
)

fun List<BMGMData.Content>.asUiModel() = map { it.asUiModel() }

fun BMGMData.Action.asUiModel() = BMGMUiModel.Action(
    type = type,
    link = link
)

fun BMGMData.asUiModel() = BMGMUiModel(
    titles = titles,
    iconUrl = iconUrl,
    products = contents.asUiModel(),
    backgroundColor = backgroundColor,
    action = action.asUiModel(),
    loadMoreText = loadMoreText,
    titleColor = titleColor
)
