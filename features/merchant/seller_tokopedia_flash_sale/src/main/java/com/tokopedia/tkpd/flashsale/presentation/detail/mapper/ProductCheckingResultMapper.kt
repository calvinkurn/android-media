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

    private fun List<SubmittedProduct.Warehouse>.mapToCheckingResult(soldCount: Int) = map {
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
                rejectionReason = it.rejectionReason,
                soldCount = soldCount.toLong(),
            )
        )
    }

    fun map (item: List<SubmittedProduct>) = item.map {
        mapItem(it)
    }

    fun mapItem (item: SubmittedProduct): ProductCheckingResult {
        val productName = item.name.getVariantName()
        return ProductCheckingResult (
            productId = item.productId.toString(),
            name = productName,
            imageUrl = item.picture,
            isMultiloc = item.isMultiwarehouse,
            checkingDetailResult = ProductCheckingResult.CheckingDetailResult(
                discountedPrice = item.discountedPrice.price.toLong(),
                originalPrice = item.price.price.toLong(),
                discountPercent = item.discount.discount.toInt(),
                stock = item.campaignStock.toLong(),
                statusText = "",
                isSubsidy = false,
                subsidyAmount = 0,
                rejectionReason = "",
                soldCount = item.soldCount.toLong(),
            ),
            locationCheckingResult = item.warehouses.mapToCheckingResult(item.soldCount)
        )
    }

    fun isMultilocOrVariantProduct(selectedProduct: DelegateAdapterItem): Boolean {
        return when (selectedProduct) {
            is OngoingItem -> selectedProduct.isMultiwarehouse || selectedProduct.totalChild.isMoreThanZero()
            is OngoingRejectedItem -> selectedProduct.isMultiwarehouse || selectedProduct.totalChild.isMoreThanZero()
            is FinishedProcessSelectionItem -> selectedProduct.isMultiwarehouse || selectedProduct.totalChild.isMoreThanZero()
            is OnSelectionProcessItem -> selectedProduct.isMultiwarehouse || selectedProduct.totalChild.isMoreThanZero()
            is WaitingForSelectionItem -> selectedProduct.isMultiwarehouse || selectedProduct.totalChild.isMoreThanZero()
            else -> false
        }
    }
}



