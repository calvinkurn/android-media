package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.oneclickcheckout.common.data.model.OrderItem
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData

data class OrderProduct(
        var cartId: String = "",
        var productId: Long = 0,
        var parentId: String = "",
        var productName: String = "",
        var productPrice: Long = 0,
        var finalPrice: Long = 0,
        var wholesalePrice: Long = 0,
        var wholesalePriceList: List<WholesalePrice> = arrayListOf(),
        var originalPrice: Long = 0,
        var initialPrice: Long = 0,
        var isSlashPrice: Boolean = false,
        var productImageUrl: String = "",
        var minOrderQuantity: Int = 0,
        var maxOrderQuantity: Int = 0,
        var maxOrderStock: Int = 0,
        var orderQuantity: Int = 0,
        var isFreeOngkir: Boolean = false,
        var isFreeOngkirExtra: Boolean = false,
        var weight: Int = 0,
        var weightActual: Int = 0,
        var quantity: QuantityUiModel = QuantityUiModel(),
        var notes: String = "",
        var maxCharNote: Int = 0,
        var isEditingNotes: Boolean = false,
        var cashback: String = "",
        var warehouseId: Long = 0,
        var isPreOrder: Int = 0,
        var preOrderDuration: Int = 0,
        var categoryId: String = "",
        var category: String = "",
        var productFinsurance: Int = 0,
        var campaignId: String = "",
        var productTrackerData: ProductTrackerData = ProductTrackerData(),
        var tickerMessage: ProductTickerMessage = ProductTickerMessage(),
        var purchaseProtectionPlanData: PurchaseProtectionPlanData = PurchaseProtectionPlanData(),
        var variant: String = "",
        var productWarningMessage: String = "",
        var productAlertMessage: String = "",
        var slashPriceLabel: String = "",
        var productInformation: List<String> = emptyList(),
        var errorMessage: String = "",
        var isError: Boolean = false,

        // Analytics
        var hasTriggerViewErrorProductLevelTicker: Boolean = false
) : OrderItem {

    fun hasParentId(): Boolean {
        return parentId.isNotEmpty() && parentId != "0"
    }
}

data class WholesalePrice(
        val qtyMinFmt: String = "",
        val qtyMaxFmt: String = "",
        val prdPrcFmt: String = "",
        val qtyMin: Int = 0,
        val qtyMax: Int = 0,
        val prdPrc: Long = 0
)

data class ProductTrackerData(
        var attribution: String = "",
        var trackerListName: String = ""
)

data class QuantityUiModel(
        var minOrderQuantity: Int = 0,
        var maxOrderQuantity: Int = 0,
        var maxOrderStock: Int = 0,
        var orderQuantity: Int = 0
)

data class ProductTickerMessage(
        val message: String = "",
        val replacement: List<ProductTickerMessageReplacement> = emptyList()
)

data class ProductTickerMessageReplacement(
        val identifier: String = "",
        val value: String = ""
)