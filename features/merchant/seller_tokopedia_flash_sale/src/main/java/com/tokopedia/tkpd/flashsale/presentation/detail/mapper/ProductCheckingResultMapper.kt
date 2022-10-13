package com.tokopedia.tkpd.flashsale.presentation.detail.mapper

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tkpd.flashsale.domain.entity.ProductCheckingResult
import com.tokopedia.tkpd.flashsale.domain.entity.SubmittedProduct
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.ongoing.item.OngoingItem
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.ongoing.item.OngoingRejectedItem
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.FinishedProcessSelectionItem
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.OnSelectionProcessItem
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.registered.item.WaitingForSelectionItem
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.DiscountUtil

object ProductCheckingResultMapper {

    private const val PRODUCT_NAME_DELIMITER_REMOTE = " - "
    private const val VARIANT_NAME_DELIMITER_REMOTE = ", "
    private const val VARIANT_NAME_DELIMITER_LOCAL = " | "

    private fun String.getVariantName() =
        split(PRODUCT_NAME_DELIMITER_REMOTE).lastOrNull().orEmpty().replace(
            VARIANT_NAME_DELIMITER_REMOTE, VARIANT_NAME_DELIMITER_LOCAL)

    private fun List<SubmittedProduct.Warehouse>.mapToCheckingResult() = map {
        val originalPrice = it.discountSetup?.price.orZero().toLong()
        val discountPercent = it.discountSetup?.discount.orZero()
        val discountedPrice = DiscountUtil.calculatePrice(discountPercent, originalPrice)
        ProductCheckingResult.LocationCheckingResult(
            warehouseId = it.warehouseId.toString(),
            cityName = it.name,
            checkingDetailResult = ProductCheckingResult.CheckingDetailResult(
                discountedPrice = discountedPrice,
                originalPrice = originalPrice,
                discountPercent = discountPercent.toInt(),
                stock = it.discountSetup?.stock.orZero(),
                statusText = it.statusText,
                isSubsidy = it.subsidy.hasSubsidy,
                subsidyAmount = it.subsidy.subsidyAmount,
                rejectionReason = it.rejectionReason
            )
        )
    }

    fun map (item: List<SubmittedProduct>) = item.map {
        mapItem(it)
    }

    fun mapItem (item: SubmittedProduct): ProductCheckingResult {
        val productName = item.name.getVariantName()
        val warehouses = item.warehouses.mapToCheckingResult()
        return ProductCheckingResult (
            productId = item.productId.toString(),
            name = productName,
            imageUrl = item.picture,
            isMultiloc = item.isMultiwarehouse,
            checkingDetailResult = warehouses.firstOrNull()?.checkingDetailResult
                ?: ProductCheckingResult.CheckingDetailResult(),
            locationCheckingResult = warehouses
        )
    }

    fun mapFromWarehouses (selectedProduct: DelegateAdapterItem): ProductCheckingResult {
        val warehouses = getWarehouses(selectedProduct)
        return ProductCheckingResult (
            productId = getProductId(selectedProduct),
            imageUrl = getImageUrl(selectedProduct),
            isMultiloc = true,
            locationCheckingResult = warehouses.mapToCheckingResult()
        )
    }

    fun isVariantProduct(selectedProduct: DelegateAdapterItem): Boolean {
        return when (selectedProduct) {
            is OngoingItem -> selectedProduct.totalChild.isMoreThanZero()
            is OngoingRejectedItem -> selectedProduct.totalChild.isMoreThanZero()
            is FinishedProcessSelectionItem -> selectedProduct.totalChild.isMoreThanZero()
            is OnSelectionProcessItem -> selectedProduct.totalChild.isMoreThanZero()
            is WaitingForSelectionItem -> selectedProduct.totalChild.isMoreThanZero()
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
}



