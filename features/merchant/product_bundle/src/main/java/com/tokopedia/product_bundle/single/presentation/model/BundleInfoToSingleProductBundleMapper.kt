package com.tokopedia.product_bundle.single.presentation.model

import android.content.Context
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.data.model.response.BundleItem
import com.tokopedia.product_bundle.common.data.model.response.Child
import com.tokopedia.product_bundle.common.data.model.response.Preorder
import com.tokopedia.product_bundle.common.util.AtcVariantMapper
import com.tokopedia.product_bundle.common.util.DiscountUtil

object BundleInfoToSingleProductBundleMapper {

    private const val BUNDLE_INFO_ACTIVE: String = "1"
    private const val BUNDLE_INFO_UPCOMING: String = "2"
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
                    (it.status == BUNDLE_INFO_ACTIVE || it.status == BUNDLE_INFO_UPCOMING) &&
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
        SingleProductBundleItem(
            quantity = bundleItem.minOrder,
            bundleName = context.getString(R.string.bundle_item_title_prefix, bundleItem.minOrder),
            productName = bundleItem.name,
            originalPrice = bundleItem.originalPrice,
            discountedPrice = bundleItem.bundlePrice,
            discount = DiscountUtil.getDiscountPercentage(bundleItem.originalPrice,
                bundleItem.bundlePrice),
            imageUrl = bundleItem.picURL,
            preorderDurationWording = getPreorderWording(context, it.preorder),
            productVariant = if (productVariant.hasVariant) productVariant else null
        )
    }

    private fun mapToSelectedItem(
        BundleInfos: List<BundleInfo>,
        selectedBundleId: String,
        selectedProductId: Long,
        emptyVariantProductIds: List<String>
    ) = BundleInfos.map { bundleInfo ->
        val bundleItem = bundleInfo.bundleItems.firstOrNull() ?: BundleItem()
        SingleProductBundleSelectedItem(
            productId = when {
                bundleItem.children.any { it.productID == selectedProductId } -> {
                    selectedProductId.toString()
                }
                bundleItem.selections.isNotEmpty() -> {
                    ""
                }
                else -> {
                    bundleItem.productID.toString()
                }
            },
            bundleId = bundleInfo.bundleID.toString(),
            shopId = bundleInfo.shopID.toString(),
            quantity = bundleItem.minOrder,
            isSelected = bundleInfo.bundleID.toString() == selectedBundleId,
        ).apply {
            if (isSelected) isVariantEmpty = emptyVariantProductIds.isNotEmpty()
        }
    }.apply {
        // autoselect first item, if there is no selected item
        if (!this.any { it.isSelected }) {
            firstOrNull()?.isSelected = true
        }
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