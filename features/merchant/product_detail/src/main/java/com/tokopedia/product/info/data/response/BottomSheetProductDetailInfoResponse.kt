package com.tokopedia.product.info.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BottomSheetProductDetailInfoResponse(
    @SerializedName("pdpGetDetailBottomSheet")
    @Expose
    val response: PdpGetDetailBottomSheet = PdpGetDetailBottomSheet()
)

data class PdpGetDetailBottomSheet(
    @SerializedName("bottomsheetData")
    @Expose
    val bottomsheetData: List<BottomSheetItem> = listOf(),
    @SerializedName("dataShopNotes")
    @Expose
    val dataShopNotes: DataShopNotes = DataShopNotes(),
    @SerializedName("discussion")
    @Expose
    val discussion: Discussion = Discussion(),
    @SerializedName("error")
    @Expose
    val error: Error = Error()
)

data class BottomSheetItem(
    @SerializedName("componentName")
    @Expose
    val componentName: String = "",
    @SerializedName("isApplink")
    @Expose
    val isApplink: Boolean = false,
    @SerializedName("isShowable")
    @Expose
    val isShowable: Boolean = false,
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("value")
    @Expose
    val value: String = "",
    @SerializedName("applink")
    @Expose
    val applink: String = "",
    @SerializedName("icon")
    @Expose
    val icon: String = "",
    @SerializedName("row")
    @Expose
    val row: List<ItemCatalog> = listOf()
)

data class DataShopNotes(
    @SerializedName("error")
    @Expose
    val error: String = "",
    @SerializedName("shopNotesData")
    @Expose
    val shopNotesData: List<ShopNotesData> = listOf()
)

data class ShopNotesData(
    @SerializedName("shopNotesID")
    @Expose
    val shopNotesId: String = "",
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("content")
    @Expose
    val content: String = "",
    @SerializedName("isTerms")
    @Expose
    val isTerms: Boolean = false,
    @SerializedName("position")
    @Expose
    val position: Int = 0,
    @SerializedName("url")
    @Expose
    val url: String = "",
    @SerializedName("updateTime")
    @Expose
    val updateTime: String = "",
    @SerializedName("updateTimeUTC")
    @Expose
    val updateTimeUTC: String = ""
)

data class Discussion(
    @SerializedName("buttonCopy")
    @Expose
    val buttonCopy: String = "",
    @SerializedName("buttonType")
    @Expose
    val buttonType: String = "",
    @SerializedName("title")
    @Expose
    val title: String = ""
)

data class Error(
    @SerializedName("Code")
    @Expose
    val code: Int = 0,
    @SerializedName("DevMessage")
    @Expose
    val devMessage: String = "",
    @SerializedName("Message")
    @Expose
    val message: String = ""
)

data class ItemCatalog(
    @SerializedName("key")
    @Expose
    val key: String = "",
    @SerializedName("value")
    @Expose
    val value: String = ""
)