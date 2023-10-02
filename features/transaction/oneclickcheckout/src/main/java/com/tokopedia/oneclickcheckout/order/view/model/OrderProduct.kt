package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.purchase_platform.common.feature.addonsproduct.data.model.AddOnsProductDataModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.model.EthicalDrugDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData

data class OrderProduct(
    var cartId: String = "",
    var productId: String = "",
    var parentId: String = "",
    var productName: String = "",
    var productPrice: Double = 0.0,
    var finalPrice: Double = 0.0,
    var wholesalePrice: Double = 0.0,
    var wholesalePriceList: List<WholesalePrice> = arrayListOf(),
    var originalPrice: Double = 0.0,
    var initialPrice: Double = 0.0,
    var isSlashPrice: Boolean = false,
    var productImageUrl: String = "",
    var minOrderQuantity: Int = 0,
    var maxOrderQuantity: Int = 0,
    var maxOrderStock: Int = 0,
    var orderQuantity: Int = 0,
    var isFreeOngkir: Boolean = false,
    var isFreeOngkirExtra: Boolean = false,
    var freeShippingName: String = "",
    var weight: Int = 0,
    var weightActual: Int = 0,
    var notes: String = "",
    var maxCharNote: Int = 0,
    var placeholderNote: String = "",
    var isEditingNotes: Boolean = false,
    var cashback: String = "",
    var warehouseId: String = "",
    var isPreOrder: Int = 0,
    var preOrderDuration: Int = 0,
    var categoryId: String = "",
    var category: String = "",
    var lastLevelCategory: String = "",
    var categoryIdentifier: String = "",
    var productFinsurance: Int = 0,
    var campaignId: String = "",
    var productTrackerData: ProductTrackerData = ProductTrackerData(),
    var purchaseProtectionPlanData: PurchaseProtectionPlanData = PurchaseProtectionPlanData(),
    var variant: String = "",
    var productWarningMessage: String = "",
    var productAlertMessage: String = "",
    var slashPriceLabel: String = "",
    var productInformation: List<String> = emptyList(),
    var errorMessage: String = "",
    var isError: Boolean = false,
    var addOn: AddOnGiftingDataModel = AddOnGiftingDataModel(),
    var addOnsProductData: AddOnsProductDataModel = AddOnsProductDataModel(),
    var ethicalDrug: EthicalDrugDataModel = EthicalDrugDataModel(),
    // Analytics
    var hasTriggerViewErrorProductLevelTicker: Boolean = false,
    var isFulfillment: Boolean = false
) {

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
    val prdPrc: Double = 0.0
)

data class ProductTrackerData(
    var attribution: String = "",
    var trackerListName: String = ""
)
