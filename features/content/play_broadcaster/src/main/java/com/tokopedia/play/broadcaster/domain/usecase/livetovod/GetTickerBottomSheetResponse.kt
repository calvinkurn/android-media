package com.tokopedia.play.broadcaster.domain.usecase.livetovod

import com.google.gson.annotations.SerializedName

data class GetTickerBottomSheetResponse(
    @SerializedName("broadcasterGetTickerBottomsheetConfig")
    val data: Data = Data()
) {
    companion object {
        const val TYPE_BOTTOM_SHEET = 1
        const val TYPE_TICKER = 2

        const val PAGE_PREPARATION = "live_preparation"
        const val PAGE_REPORT = "live_report"
    }
}

data class Data(
    @SerializedName("page")
    val page: String = "",
    @SerializedName("type")
    val type: Int = 0,
    @SerializedName("imageURL")
    val imageURL: String = "",
    @SerializedName("mainText")
    val mainText: List<MainText> = emptyList(),
    @SerializedName("bottomText")
    val bottomText: BottomText = BottomText(),
)

data class MainText(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("action")
    val action: List<Action> = emptyList(),
)

data class BottomText(
    @SerializedName("description")
    val description: String = "",
    @SerializedName("action")
    val action: List<Action> = emptyList(),
)

data class Action(
    @SerializedName("index")
    val index: String = "",
    @SerializedName("text")
    val text: String = "",
    @SerializedName("link")
    val link: String = "",
)
