package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.fulfillment.response.TokoCabangInfo

class GroupShopOccResponse(
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("ShipmentInformationResponse")
        val shipmentInformation: ShipmentInformationResponse = ShipmentInformationResponse(),
        @SerializedName("errors_unblocking")
        val unblockingErrors: List<String> = emptyList(),
        @SerializedName("is_disable_change_courier")
        val isDisableChangeCourier: Boolean = false,
        @SerializedName("auto_courier_selection")
        val autoCourierSelection: Boolean = false,
        @SerializedName("bo_metadata")
        val boMetadata: BoMetadata = BoMetadata(),
        @SerializedName("courier_selection_error")
        val courierSelectionError: CourierSelectionError = CourierSelectionError(),
        @SerializedName("shop")
        val shop: ShopDataResponse = ShopDataResponse(),
        @SerializedName("cart_string")
        val cartString: String = "",
        @SerializedName("payment_profile")
        val paymentProfile: String = "",
        @SerializedName("toko_cabang")
        val tokoCabangInfo: TokoCabangInfo = TokoCabangInfo(),
        @SerializedName("warehouse")
        val warehouse: WarehouseDataResponse = WarehouseDataResponse(),
        @SerializedName("cart_details")
        val cartDetails: List<ProductDataResponse> = emptyList()
)

class CourierSelectionError(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("description")
        val description: String = ""
)