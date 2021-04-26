package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.fulfillment.response.TokoCabangInfo
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.data.PurchaseProtectionPlanDataResponse

data class CartDataResponse(
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("cart_id")
        val cartId: String = "",
        @SerializedName("product")
        val product: ProductDataResponse = ProductDataResponse(),
        @SerializedName("shop")
        val shop: ShopDataResponse = ShopDataResponse(),
        @SerializedName("warehouse")
        val warehouse: WarehouseDataResponse = WarehouseDataResponse(),
        @SerializedName("cart_string")
        val cartString: String = "",
        @SerializedName("payment_profile")
        val paymentProfile: String = "",
        @SerializedName("purchase_protection_plan_data")
        val purchaseProtectionPlanDataResponse: PurchaseProtectionPlanDataResponse = PurchaseProtectionPlanDataResponse(),
        @SerializedName("toko_cabang")
        val tokoCabangInfo: TokoCabangInfo = TokoCabangInfo()
)