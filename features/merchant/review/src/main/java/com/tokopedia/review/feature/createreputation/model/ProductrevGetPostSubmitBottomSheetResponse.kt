package com.tokopedia.review.feature.createreputation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetPostSubmitBottomSheetResponse(
    @SerializedName("type")
    @Expose
    val type: String? = null,
    @SerializedName("title")
    @Expose
    val title: String? = null,
    @SerializedName("description")
    @Expose
    val description: String? = null,
    @SerializedName("imageURL")
    @Expose
    val imageUrl: String? = null,
    @SerializedName("buttonList")
    @Expose
    val buttonList: List<Button>? = null
) {
    data class Button(
        @SerializedName("type")
        @Expose
        val type: String? = null,
        @SerializedName("text")
        @Expose
        val text: String? = null,
        @SerializedName("unifyType")
        @Expose
        val unifyType: String? = null,
        @SerializedName("unifyVariant")
        @Expose
        val unifyVariant: String? = null,
        @SerializedName("unifySize")
        @Expose
        val unifySize: String? = null,
        @SerializedName("webLink")
        @Expose
        val webLink: String? = null,
        @SerializedName("appLink")
        @Expose
        val appLink: String? = null,
    ) {
        companion object {
            const val TYPE_STANDARD = "standard"
            const val TYPE_CLOSE = "close"
            const val TYPE_WEB_VIEW = "web_view"
        }
    }

    fun isShowBottomSheet() = type == TYPE_BOTTOM_SHEET

    fun getToasterText(userName: String): String? {
        return description?.replace(VARIABLE_USER_NAME, userName)
    }

    companion object {
        private const val TYPE_BOTTOM_SHEET = "standard"
        private const val VARIABLE_USER_NAME = "@Name"
    }
}
