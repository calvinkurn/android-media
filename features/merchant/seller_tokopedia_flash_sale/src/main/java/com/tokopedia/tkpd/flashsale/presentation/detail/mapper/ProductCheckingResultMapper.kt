package com.tokopedia.tkpd.flashsale.presentation.detail.mapper

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tkpd.flashsale.domain.entity.ProductCheckingResult
import com.tokopedia.tkpd.flashsale.domain.entity.SubmittedProduct
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.ongoing.item.OngoingItem
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.ongoing.item.OngoingRejectedItem
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.FinishedProcessSelectionItem
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.OnSelectionProcessItem
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.WaitingForSelectionItem
import com.tokopedia.unifycomponents.Label

object ProductCheckingResultMapper {

    private const val PRODUCT_NAME_DELIMITER_REMOTE = " - "
    private const val VARIANT_NAME_DELIMITER_REMOTE = ", "
    private const val VARIANT_NAME_DELIMITER_LOCAL = " | "
    private const val PRODUCT_STATUS_REGISTERED = 0L
    private const val PRODUCT_STATUS_ACCEPTED = 1L
    private const val PRODUCT_STATUS_REFUSED = 2L
    private const val ONGOING_TAB = "ongoing"

    private fun String.getVariantName() =
        split(PRODUCT_NAME_DELIMITER_REMOTE).lastOrNull().orEmpty().replace(
            VARIANT_NAME_DELIMITER_REMOTE, VARIANT_NAME_DELIMITER_LOCAL)

    private fun List<SubmittedProduct.Warehouse>.mapToCheckingResult(displayProductSold: Boolean, tabName: String) = map {
        val originalPrice = it.price.toLong()
        val discountPercent = it.discountSetup?.discount.orZero()
        val discountedPrice = it.discountSetup?.price.orZero().toLong()
        ProductCheckingResult.LocationCheckingResult(
            warehouseId = it.warehouseId.toString(),
            cityName = it.name,
            soldCount = if (displayProductSold) it.soldCount else null,
            checkingDetailResult = ProductCheckingResult.CheckingDetailResult(
                discountedPrice = discountedPrice,
                originalPrice = originalPrice,
                discountPercent = discountPercent.toInt(),
                stock = it.discountSetup?.stock.orZero(),
                statusId = it.statusId,
                statusText = it.getStatusText(tabName),
                statusLabelType = it.getLabelType(),
                isSubsidy = it.subsidy.hasSubsidy,
                subsidyAmount = it.subsidy.subsidyAmount,
                rejectionReason = it.getRejectionReason()
            )
        )
    }

    private fun SubmittedProduct.Warehouse.getStatusText(tabName: String): String {
        return if (tabName == ONGOING_TAB) {
            ""
        } else {
            statusText
        }
    }

    private fun SubmittedProduct.Warehouse.getLabelType(): Int {
        return when(statusId) {
            PRODUCT_STATUS_REGISTERED -> Label.HIGHLIGHT_LIGHT_ORANGE
            PRODUCT_STATUS_ACCEPTED -> Label.HIGHLIGHT_LIGHT_GREEN
            PRODUCT_STATUS_REFUSED -> Label.HIGHLIGHT_LIGHT_RED
            else -> Label.HIGHLIGHT_LIGHT_GREEN
        }
    }

    private fun SubmittedProduct.Warehouse.getRejectionReason(): String {
        return if (statusId == PRODUCT_STATUS_REFUSED) rejectionReason else ""
    }

    fun map (
        item: List<SubmittedProduct>,
        displayProductSold: Boolean = false,
        fallbackProductImage: String = "",
        tabName: String = ""
    ) = item.map {
        mapItem(it, displayProductSold, fallbackProductImage, tabName)
    }

    fun mapItem (
        item: SubmittedProduct,
        displayProductSold: Boolean = false,
        fallbackProductImage: String = "",
        tabName: String = ""
    ): ProductCheckingResult {
        val productName = item.name.getVariantName()
        val warehouses = item.warehouses.mapToCheckingResult(displayProductSold, tabName)
        return ProductCheckingResult (
            productId = item.productId.toString(),
            name = productName,
            imageUrl = item.picture.ifEmpty { fallbackProductImage },
            isMultiloc = item.isMultiwarehouse,
            checkingDetailResult = warehouses.firstOrNull()?.checkingDetailResult
                ?: ProductCheckingResult.CheckingDetailResult(),
            locationCheckingResult = warehouses,
            soldCount = if (displayProductSold) item.soldCount else null,
        )
    }

    fun mapFromAdapterItem(
        selectedProduct: DelegateAdapterItem,
        displayProductSold: Boolean,
        tabName: String
    ): ProductCheckingResult {
        val warehouses = getWarehouses(selectedProduct)
        return ProductCheckingResult (
            productId = getProductId(selectedProduct),
            imageUrl = getImageUrl(selectedProduct),
            isMultiloc = true,
            locationCheckingResult = warehouses.mapToCheckingResult(displayProductSold, tabName)
        )
    }

    fun isVariantProduct(selectedProduct: DelegateAdapterItem): Boolean {
        return when (selectedProduct) {
            is OngoingItem -> selectedProduct.isParentProduct
            is OngoingRejectedItem -> selectedProduct.isParentProduct
            is FinishedProcessSelectionItem -> selectedProduct.isParentProduct
            is OnSelectionProcessItem -> selectedProduct.isParentProduct
            is WaitingForSelectionItem -> selectedProduct.isParentProduct
            else -> false
        }
    }

    fun isMultiloc(selectedProduct: DelegateAdapterItem): Boolean {
        return when (selectedProduct) {
            is OngoingItem -> selectedProduct.isMultiwarehouse
            is OngoingRejectedItem -> selectedProduct.isMultiwarehouse
            is FinishedProcessSelectionItem -> selectedProduct.isMultiwarehouse
            is OnSelectionProcessItem -> selectedProduct.isMultiwarehouse
            is WaitingForSelectionItem -> selectedProduct.isMultiwarehouse
            else -> false
        }
    }

    fun getProductName(selectedProduct: DelegateAdapterItem): String {
        return when (selectedProduct) {
            is OngoingItem -> selectedProduct.name
            is OngoingRejectedItem -> selectedProduct.name
            is FinishedProcessSelectionItem -> selectedProduct.name
            is OnSelectionProcessItem -> selectedProduct.name
            is WaitingForSelectionItem -> selectedProduct.name
            else -> ""
        }
    }

    fun getWarehouses(selectedProduct: DelegateAdapterItem): List<SubmittedProduct.Warehouse> {
        return when (selectedProduct) {
            is OngoingItem -> selectedProduct.warehouses
            is OngoingRejectedItem -> selectedProduct.warehouses
            is FinishedProcessSelectionItem -> selectedProduct.warehouses
            is OnSelectionProcessItem -> selectedProduct.warehouses
            is WaitingForSelectionItem -> selectedProduct.warehouses.orEmpty()
            else -> emptyList()
        }
    }

    fun getProductId(selectedProduct: DelegateAdapterItem): String {
        return when (selectedProduct) {
            is OngoingItem -> selectedProduct.productId
            is OngoingRejectedItem -> selectedProduct.productId
            is FinishedProcessSelectionItem -> selectedProduct.productId
            is OnSelectionProcessItem -> selectedProduct.productId
            is WaitingForSelectionItem -> selectedProduct.productId
            else -> ""
        }.toString()
    }

    fun getImageUrl(selectedProduct: DelegateAdapterItem): String {
        return when (selectedProduct) {
            is OngoingItem -> selectedProduct.picture
            is OngoingRejectedItem -> selectedProduct.picture
            is FinishedProcessSelectionItem -> selectedProduct.picture
            is OnSelectionProcessItem -> selectedProduct.picture
            is WaitingForSelectionItem -> selectedProduct.picture
            else -> ""
        }.toString()
    }

    fun getProductSold(selectedProduct: DelegateAdapterItem): String {
        return when (selectedProduct) {
            is OngoingItem -> selectedProduct.soldCount
            is OngoingRejectedItem -> selectedProduct.soldCount
            is FinishedProcessSelectionItem -> ""
            is OnSelectionProcessItem -> ""
            is WaitingForSelectionItem -> ""
            else -> ""
        }.toString()
    }
}



