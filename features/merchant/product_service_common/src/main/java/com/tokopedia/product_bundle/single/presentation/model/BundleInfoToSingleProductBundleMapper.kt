package com.tokopedia.product_bundle.single.presentation.model

import android.content.Context
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.data.model.response.BundleItem
import com.tokopedia.product_bundle.common.data.model.response.Child
import com.tokopedia.product_bundle.common.data.model.response.Preorder
import com.tokopedia.product_bundle.common.util.AtcVariantMapper
import com.tokopedia.product_bundle.common.util.DiscountUtil
import com.tokopedia.product_service_common.R

object BundleInfoToSingleProductBundleMapper {

    private const val BUNDLE_INFO_ACTIVE: String = "1"
    private const val BUNDLE_ITEM_SHOW: String = "1"
    private const val BUNDLE_ITEM_ACTIVE: String = "ACTIVE"
    private const val PREORDER_STATUS_ACTIVE: String = "ACTIVE"
    private const val PREORDER_TYPE_DAY: Int = 1
    private const val PREORDER_TYPE_MONTH: Int = 2

    fun mapToSingleProductBundle (
        context: Context, bundleInfo: List<BundleInfo>,
        selectedBundleId: String,
        selectedProductId: Long,
        emptyVariantProductIds: List<String>
    ): SingleProductBundleUiModel {
        val filteredBundleInfo = bundleInfo.filter {
            val bundleItem = it.bundleItems.firstOrNull()
            return@filter bundleItem != null &&
                    it.status == BUNDLE_INFO_ACTIVE &&
                    bundleItem.status == BUNDLE_ITEM_SHOW &&
                    bundleItem.productStatus == BUNDLE_ITEM_ACTIVE &&
                    it.isStockAvailable() &&
                    it.isMinOrderValid()
        }
        return SingleProductBundleUiModel(
            items = mapToBundleItem(context, filteredBundleInfo),
            selectedItems = mapToSelectedItem(filteredBundleInfo, selectedBundleId,
                selectedProductId, emptyVariantProductIds)
        )
    }

    private fun getPreorderWording(context: Context, preorder: Preorder): String? {
        if (preorder.status == PREORDER_STATUS_ACTIVE) {
            return "${preorder.processTime} ${getTimeUnitWording(context, preorder.processTypeNum)}"
        }
        return null
    }

    private fun getTimeUnitWording(context: Context, processTypeNum: Int): String {
        return when (processTypeNum) {
            PREORDER_TYPE_DAY -> context.getString(R.string.preorder_time_unit_day)
            PREORDER_TYPE_MONTH -> context.getString(R.string.preorder_time_unit_month)
            else -> ""
        }
    }

    private fun mapToBundleItem(context: Context, bundleInfos: List<BundleInfo>) = bundleInfos.map {
        val bundleItem = it.bundleItems.firstOrNull() ?: BundleItem()
        val productVariant = AtcVariantMapper.mapToProductVariant(bundleItem)
        if (productVariant.hasVariant) {
            val child = productVariant.children.minByOrNull { child ->
                child.finalPrice
            }
            SingleProductBundleItem(
                quantity = child?.stock?.minimumOrder.toIntOrZero(),
                productName = bundleItem.name,
                originalPrice = child?.finalMainPrice.orZero(),
                discountedPrice = child?.finalPrice.orZero(),
                discount = child?.campaign?.discountedPercentage?.toInt().orZero(),
                imageUrl = bundleItem.picURL,
                preorderDurationWording = getPreorderWording(context, it.preorder),
                productVariant = productVariant
            )
        } else {
            SingleProductBundleItem(
                quantity = bundleItem.minOrder,
                productName = bundleItem.name,
                originalPrice = bundleItem.originalPrice,
                discountedPrice = bundleItem.bundlePrice,
                discount = DiscountUtil.getDiscountPercentage(bundleItem.originalPrice,
                    bundleItem.bundlePrice),
                imageUrl = bundleItem.picURL,
                preorderDurationWording = getPreorderWording(context, it.preorder)
            )
        }
    }

    private fun mapToSelectedItem(
        bundleInfos: List<BundleInfo>,
        selectedBundleId: String,
        selectedProductId: Long,
        emptyVariantProductIds: List<String>
    ) = bundleInfos.map { bundleInfo ->
        val bundleItem = bundleInfo.bundleItems.firstOrNull() ?: BundleItem()
        SingleProductBundleSelectedItem(
            productId = when {
                isBundleVariantSelected(selectedBundleId, selectedProductId, bundleInfo) -> {
                    // when bundle variant selected
                    selectedProductId.toString()
                }
                bundleItem.selections.isNotEmpty() -> {
                    // when bundle variant not selected
                    ""
                }
                else -> {
                    // when bundle non-variant (both selected and not selected)
                    bundleItem.productID.toString()
                }
            },
            bundleId = bundleInfo.bundleID.toString(),
            shopId = bundleInfo.shopID.toString(),
            quantity = bundleItem.minOrder,
            isSelected = bundleInfo.bundleID.toString() == selectedBundleId,
            warehouseId = bundleInfo.warehouseID.toString()
        ).apply {
            if (isSelected) isVariantEmpty = emptyVariantProductIds.isNotEmpty()
        }
    }.apply {
        // autoselect first item, if there is no selected item
        if (!this.any { it.isSelected }) {
            firstOrNull()?.isSelected = true
        }
    }

    private fun isBundleVariantSelected(
        selectedBundleId: String,
        selectedProductId: Long,
        bundleInfo: BundleInfo
    ): Boolean {
        val bundleItem = bundleInfo.bundleItems.firstOrNull() ?: BundleItem()
        return selectedBundleId == bundleInfo.bundleID.toString()
                && bundleItem.children.any { it.productID == selectedProductId }
    }

    private fun BundleInfo.isStockAvailable() = bundleItems.any {
        it.stock > 0 || it.children.isStockAvailable()
    }

    private fun List<Child>.isStockAvailable() = any {
        it.stock > 0
    }

    private fun BundleInfo.isMinOrderValid() = bundleItems.any {
        it.minOrder > 0 || it.children.isMinOrderValid()
    }

    private fun List<Child>.isMinOrderValid() = any {
        it.minOrder > 0
    }
}