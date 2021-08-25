package com.tokopedia.product_bundle.common.data.mapper

import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.data.model.response.Child
import com.tokopedia.product_bundle.common.data.model.response.GetBundleInfoResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

object InventoryErrorMapper {
    fun mapToInventoryError(getBundleInfoResponse: Result<GetBundleInfoResponse>,
                            selectedProductIds: List<Long>,
                            selectedBundleId: Long): InventoryError {
        if (getBundleInfoResponse is Fail) return InventoryError()
        val bundleInfo = (getBundleInfoResponse as Success).data.getBundleInfo.bundleInfo
        val selectedBundleItem = bundleInfo.firstOrNull {
            it.bundleID == selectedBundleId && it.isStockAvailable(selectedProductIds)
        }
        val unselectedBundleItem = bundleInfo.firstOrNull {
            it.bundleID != selectedBundleId && it.isStockAvailable()
        }
        val isBundleAvailable = selectedBundleItem != null // null means not found
        val isAnotherBundleAvailable = unselectedBundleItem != null

        return when {
            isBundleAvailable -> {
                InventoryError(type = InventoryErrorType.NO_ERROR)
            }
            isAnotherBundleAvailable -> {
                InventoryError(type = InventoryErrorType.BUNDLE_EMPTY_ANOTHER_BUNDLE_AVAILABLE, bundleId = selectedBundleId)
            }
            else -> {
                InventoryError(type = InventoryErrorType.BUNDLE_EMPTY, bundleId = selectedBundleId)
            }
        }
    }

    private fun BundleInfo.isStockAvailable(selectedProductIds: List<Long> = emptyList()) = bundleItems.any {
        (it.productID.isProductIdSelected(selectedProductIds) == it.stock > 0) ||
                it.children.isStockAvailable(selectedProductIds)
    }

    private fun List<Child>.isStockAvailable(selectedProductIds: List<Long>) = all {
        it.productID.isProductIdSelected(selectedProductIds) && it.stock > 0
    }

    private fun Long.isProductIdSelected(selectedProductIds: List<Long>) = selectedProductIds.any {
        it == this
    }
}

enum class InventoryErrorType{
    NO_ERROR,
    BUNDLE_EMPTY,
    BUNDLE_EMPTY_ANOTHER_BUNDLE_AVAILABLE,
    VARIANT_EMPTY_ANOTHER_BUNDLE_AVAILABLE,
    VARIANT_EMPTY_ANOTHER_BUNDLE_NOT_AVAILABLE
}

data class InventoryError (
    var type: InventoryErrorType = InventoryErrorType.NO_ERROR,
    var bundleId: Long = 0L
)