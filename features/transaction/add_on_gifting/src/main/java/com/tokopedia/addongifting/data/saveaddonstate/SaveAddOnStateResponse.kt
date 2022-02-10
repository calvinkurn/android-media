package com.tokopedia.addongifting.data.saveaddonstate

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.addongifting.data.AddOnBottomSheetResult
import com.tokopedia.purchase_platform.common.feature.addongifting.data.AddOnButtonResult
import com.tokopedia.purchase_platform.common.feature.addongifting.data.AddOnDataResult
import com.tokopedia.purchase_platform.common.feature.addongifting.data.AddOnMetadataResult
import com.tokopedia.purchase_platform.common.feature.addongifting.data.AddOnNoteResult
import com.tokopedia.purchase_platform.common.feature.addongifting.data.AddOnResult
import com.tokopedia.purchase_platform.common.feature.addongifting.data.ProductResult
import com.tokopedia.purchase_platform.common.feature.addongifting.data.TickerResult

data class SaveAddOnStateResponse(
        @SerializedName("add_ons")
        val addOns: List<AddOnResult> = emptyList()
)

data class AddOnResponse(
        @SerializedName("add_on_bottomsheet")
        val addOnBottomSheet: AddOnBottomSheetResult = AddOnBottomSheetResult(),
        @SerializedName("add_on_button")
        val addOnButton: AddOnButtonResult = AddOnButtonResult(),
        @SerializedName("add_on_data")
        val addOnData: List<AddOnDataResult> = emptyList(),
        @SerializedName("add_on_key")
        val addOnKey: String = "",
        @SerializedName("add_on_level")
        val addOnLevel: String = "",
        @SerializedName("status")
        val status: Int = 0
)

data class AddOnBottomSheetResponse(
        @SerializedName("description")
        val description: String = "",
        @SerializedName("header_title")
        val headerTitle: String = "",
        @SerializedName("products")
        val products: List<ProductResult> = emptyList(),
        @SerializedName("ticker")
        val ticker: TickerResult = TickerResult()
)

data class AddOnButtonResponse(
        @SerializedName("action")
        val action: Int = 0,
        @SerializedName("description")
        val description: String = "",
        @SerializedName("left_icon_url")
        val leftIconUrl: String = "",
        @SerializedName("right_icon_url")
        val rightIconUrl: String = "",
        @SerializedName("title")
        val title: String = ""
)

data class AddOnDataResponse(
        @SerializedName("add_on_id")
        val addOnId: String = "",
        @SerializedName("add_on_metadata")
        val addOnMetadata: AddOnMetadataResult = AddOnMetadataResult(),
        @SerializedName("add_on_price")
        val addOnPrice: Int = 0,
        @SerializedName("add_on_qty")
        val addOnQty: Int = 0
)

data class AddOnMetadataResponse(
        @SerializedName("add_on_note")
        val addOnNote: AddOnNoteResult = AddOnNoteResult()
)

data class AddOnNoteResponse(
        @SerializedName("from")
        val from: String = "",
        @SerializedName("is_custom_note")
        val isCustomNote: Boolean = false,
        @SerializedName("notes")
        val notes: String = "",
        @SerializedName("to")
        val to: String = ""
)

data class ProductResponse(
        @SerializedName("product_image_url")
        val productImageUrl: String = "",
        @SerializedName("product_name")
        val productName: String = ""
)

data class TickerResponse(
        @SerializedName("text")
        val text: String = ""
)