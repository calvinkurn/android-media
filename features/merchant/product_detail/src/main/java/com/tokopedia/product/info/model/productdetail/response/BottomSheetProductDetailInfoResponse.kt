package com.tokopedia.product.info.model.productdetail.response

import com.google.gson.annotations.SerializedName

data class BottomSheetProductDetailInfoResponse(
        @SerializedName("pdpGetDetailBottomSheet")
        val response: PdpGetDetailBottomSheet = PdpGetDetailBottomSheet()
)

data class PdpGetDetailBottomSheet(
        @SerializedName("bottomsheetData")
        val bottomsheetData: List<BottomSheetItem> = listOf(),
        @SerializedName("dataShopNotes")
        val dataShopNotes: DataShopNotes = DataShopNotes(),
        @SerializedName("discussion")
        val discussion: Discussion = Discussion(),
        @SerializedName("error")
        val error: Error = Error()
)

data class BottomSheetItem(
        @SerializedName("componentName")
        val componentName: String = "",
        @SerializedName("isApplink")
        val isApplink: Boolean = false,
        @SerializedName("isShowable")
        val isShowable: Boolean = false,
        @SerializedName("title")
        val title: String = "",
        @SerializedName("value")
        val value: String = ""
)

data class DataShopNotes(
        @SerializedName("error")
        val error: String = "",
        @SerializedName("shopNotesData")
        val shopNotesData: List<ShopNotesData> = listOf()
)

data class ShopNotesData(
        @SerializedName("shopNotesID")
        val shopNotesId: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("content")
        val content: String = "",
        @SerializedName("isTerms")
        val isTerms: Boolean = false,
        @SerializedName("position")
        val position: Int = 0,
        @SerializedName("url")
        val url: String = "",
        @SerializedName("updateTime")
        val updateTime: String = "",
        @SerializedName("updateTimeUTC")
        val updateTimeUTC: String = ""
)

data class Discussion(
        @SerializedName("buttonCopy")
        val buttonCopy: String = "",
        @SerializedName("buttonType")
        val buttonType: String = "",
        @SerializedName("title")
        val title: String = ""
)

data class Error(
        @SerializedName("Code")
        val code: Int = 0,
        @SerializedName("DevMessage")
        val devMessage: String = "",
        @SerializedName("Message")
        val message: String = ""
)