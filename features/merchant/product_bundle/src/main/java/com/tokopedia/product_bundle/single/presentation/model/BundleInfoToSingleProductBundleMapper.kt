package com.tokopedia.product_bundle.single.presentation.model

import android.content.Context
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.data.model.response.BundleItem
import com.tokopedia.product_bundle.common.data.model.response.PreOrder
import com.tokopedia.product_bundle.common.util.AtcVariantMapper
import com.tokopedia.product_bundle.common.util.DiscountUtil

object BundleInfoToSingleProductBundleMapper {

    private const val PREORDER_STATUS_ACTIVE: String = "ACTIVE"
    private const val PREORDER_TYPE_DAY: Int = 1
    private const val PREORDER_TYPE_MONTH: Int = 2
    private const val BUNDLEITEM_STATUS_SHOW: String = "SHOW"

    fun mapToSingleProductBundle (context: Context, bundleInfo: BundleInfo) = SingleProductBundleUiModel(
        preorderDurationWording = getPreorderWording(context, bundleInfo.preorder),
        items = mapToBundleItem(context, bundleInfo.bundleItems),
        selectedItems = mapToSelectedItem(bundleInfo.bundleItems)
    )

    private fun getPreorderWording(context: Context, preorder: PreOrder): String? {
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

    private fun mapToBundleItem(context: Context, bundleItems: List<BundleItem>) = bundleItems.filter {
        it.status == BUNDLEITEM_STATUS_SHOW
    }.map {
        val productVariant = AtcVariantMapper.mapToProductVariant(it)
        SingleProductBundleItem(
            quantity = it.minOrder,
            bundleName = context.getString(R.string.bundle_item_title_prefix, it.minOrder),
            productName = it.name,
            originalPrice = it.originalPrice.toDouble(),
            discountedPrice = it.bundlePrice.toDouble(),
            discount = DiscountUtil.getDiscountPercentage(it.originalPrice.toDouble(), it.bundlePrice.toDouble()),
            imageUrl = it.picURL,
            productVariant = if (productVariant.hasVariant) productVariant else null
        )
    }

    private fun mapToSelectedItem(bundleItems: List<BundleItem>) = bundleItems.map {
        SingleProductBundleSelectedItem(productId = if (it.hasVariant) "" else it.productID.toString())
    }
}