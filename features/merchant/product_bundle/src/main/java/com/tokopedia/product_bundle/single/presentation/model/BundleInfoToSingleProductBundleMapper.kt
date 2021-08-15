package com.tokopedia.product_bundle.single.presentation.model

import android.content.Context
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.data.model.response.BundleItem
import com.tokopedia.product_bundle.common.data.model.response.Preorder
import com.tokopedia.product_bundle.common.util.AtcVariantMapper
import com.tokopedia.product_bundle.common.util.DiscountUtil

object BundleInfoToSingleProductBundleMapper {

    private const val PREORDER_STATUS_ACTIVE: String = "ACTIVE"
    private const val PREORDER_TYPE_DAY: Int = 1
    private const val PREORDER_TYPE_MONTH: Int = 2
    private const val BUNDLEINFO_STATUS_SHOW: String = "1"
    private const val BUNDLEITEM_STATUS_SHOW: String = "SHOW"

    fun mapToSingleProductBundle (
        context: Context, bundleInfo: List<BundleInfo>,
        selectedProductId: Long) = SingleProductBundleUiModel(
        preorderDurationWording = getPreorderWording(context, bundleInfo[0].preorder),
        items = mapToBundleItem(context, bundleInfo),
        selectedItems = mapToSelectedItem(bundleInfo, selectedProductId)
    )

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

    private fun mapToBundleItem(context: Context, bundleInfos: List<BundleInfo>) = bundleInfos.filter {
        it.status == BUNDLEINFO_STATUS_SHOW
    }.map {
        val bundleItem = it.bundleItems.firstOrNull() ?: BundleItem()
        val productVariant = AtcVariantMapper.mapToProductVariant(bundleItem)
        SingleProductBundleItem(
            quantity = bundleItem.minOrder,
            bundleName = context.getString(R.string.bundle_item_title_prefix, bundleItem.minOrder),
            productName = bundleItem.name,
            originalPrice = bundleItem.originalPrice.toDouble(),
            discountedPrice = bundleItem.bundlePrice.toDouble(),
            discount = DiscountUtil.getDiscountPercentage(bundleItem.originalPrice.toDouble(), bundleItem.bundlePrice.toDouble()),
            imageUrl = bundleItem.picURL,
            productVariant = if (productVariant.hasVariant) productVariant else null
        )
    }

    private fun mapToSelectedItem(BundleInfos: List<BundleInfo>, selectedProductId: Long) = BundleInfos.map { bundleInfo ->
        val bundleItem = bundleInfo.bundleItems.firstOrNull() ?: BundleItem()
        SingleProductBundleSelectedItem(
            productId = if (true) "" else bundleItem.productID.toString(),
            isSelected = bundleItem.children.any { it.productID == selectedProductId }
        )
    }
}