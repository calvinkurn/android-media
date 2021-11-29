package com.tokopedia.product_bundle.common.data.mapper

import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.data.model.response.Child
import com.tokopedia.product_bundle.common.data.model.response.GetBundleInfo
import com.tokopedia.product_bundle.common.data.model.response.GetBundleInfoResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

object InventoryErrorMapper {
    fun mapToInventoryError(getBundleInfoResponse: Result<GetBundleInfoResponse>,
                            selectedBundleId: Long,
                            selectedProductIds: List<Long>
    ): InventoryError {
        if (getBundleInfoResponse is Fail) return InventoryError()
        if (selectedBundleId == 0L) return InventoryError()

        val bundleInfo = (getBundleInfoResponse as Success).data.getBundleInfo
        val isBundleAvailable = bundleInfo.isStockAvailable(selectedBundleId, selectedProductIds)
        val isOtherBundleAvailable = bundleInfo.isOtherStockAvailable(selectedBundleId, selectedProductIds)
        val isOtherVariantAvailable = bundleInfo.isOtherVariantStockAvailable(selectedBundleId)
        val emptyVariantProductIds = bundleInfo.getEmptyVariantProductIds(selectedBundleId, selectedProductIds)

        return when {
            isBundleAvailable -> {
                InventoryError(type = InventoryErrorType.NO_ERROR)
            }
            isOtherBundleAvailable && isOtherVariantAvailable -> {
                InventoryError(type = InventoryErrorType.OTHER_BUNDLE_AND_VARIANT_AVAILABLE, emptyVariantProductIds)
            }
            isOtherVariantAvailable && emptyVariantProductIds.isNotEmpty() -> {
                InventoryError(type = InventoryErrorType.OTHER_VARIANT_AVAILABLE, emptyVariantProductIds)
            }
            isOtherBundleAvailable -> {
                InventoryError(type = InventoryErrorType.OTHER_BUNDLE_AVAILABLE, emptyVariantProductIds)
            }
            else -> {
                InventoryError(type = InventoryErrorType.BUNDLE_EMPTY)
            }
        }
    }

    private fun GetBundleInfo.isStockAvailable(selectedBundleId: Long, selectedProductIds: List<Long> = emptyList()) = bundleInfo.any {
        it.bundleID == selectedBundleId && it.isStockAvailable(selectedProductIds)
    }

    private fun GetBundleInfo.isOtherStockAvailable(selectedBundleId: Long, selectedProductIds: List<Long> = emptyList()) = bundleInfo.any {
        it.bundleID != selectedBundleId && it.isStockAvailable(selectedProductIds)
    }

    private fun GetBundleInfo.isOtherVariantStockAvailable(selectedBundleId: Long): Boolean = bundleInfo.find {
        it.bundleID == selectedBundleId
    }?.bundleItems?.filter {
        it.children.isNotEmpty()
    }?.any {
        it.children.isStockAvailable()
    } ?: false

    private fun GetBundleInfo.getEmptyVariantProductIds(selectedBundleId: Long, selectedProductIds: List<Long> = emptyList()) = bundleInfo.find {
        it.bundleID == selectedBundleId
    }?.bundleItems?.filter {
        it.children.isNotEmpty() && !it.children.isStockAvailable(selectedProductIds)
    }?.map {
        it.productID
    }.orEmpty()

    private fun BundleInfo.isStockAvailable(selectedProductIds: List<Long> = emptyList()) = bundleItems.all {
        it.stock > 0 || it.children.isStockAvailable(selectedProductIds)
    }

    private fun List<Child>.isStockAvailable(selectedProductIds: List<Long> = emptyList()) = any {
        it.productID.isProductIdSelected(selectedProductIds) && it.stock > 0
    }

    private fun Long.isProductIdSelected(selectedProductIds: List<Long>) =
        if (selectedProductIds.isEmpty()) true
        else selectedProductIds.any {
            it == this
        }
}

enum class InventoryErrorType{
    NO_ERROR,
    BUNDLE_EMPTY,
    OTHER_BUNDLE_AND_VARIANT_AVAILABLE,
    OTHER_BUNDLE_AVAILABLE,
    OTHER_VARIANT_AVAILABLE
}

data class InventoryError (
    var type: InventoryErrorType = InventoryErrorType.NO_ERROR,
    var emptyVariantProductIds: List<Long> = emptyList()
)