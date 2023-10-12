package com.tokopedia.productbundlewidget.model

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.getNumberFormatted
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.BUNDLE_TYPE_SINGLE
import com.tokopedia.product_bundle.common.data.mapper.BundleDataMapper.getValidBundleInfo
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.data.model.response.BundleItem
import com.tokopedia.product_bundle.common.data.model.response.Preorder
import com.tokopedia.product_bundle.common.data.model.response.ShopInformation
import com.tokopedia.product_bundle.common.util.DiscountUtil
import com.tokopedia.product_service_widget.R
import javax.inject.Inject

class ProductBundleWidgetUiMapper @Inject constructor(@ApplicationContext private val context: Context) {

    fun groupAndMap(bundleInfo: List<BundleInfo>): List<BundleUiModel> {
        val groupedBundle = bundleInfo
            .getValidBundleInfo()
            .groupBy { it.groupID }
        return groupedBundle.groupBundleToBundleUi()
    }

    fun mapToBundleUi(bundleInfos: List<BundleInfo>): BundleUiModel {
        val bundleInfo = bundleInfos.firstOrNull() ?: BundleInfo()
        val bundleType = bundleInfo.type.toBundleType()
        val shopInfo = bundleInfo.shopInformation.toBundleShopInfo()
        return BundleUiModel(
            bundleName = bundleInfo.name,
            bundleType = bundleType,
            actionButtonText = context.getString(R.string.bundlewidget_action_button_text),
            bundleDetails = if (bundleType == BundleTypes.SINGLE_BUNDLE) bundleInfos.toSingleBundleDetails(shopInfo)
            else bundleInfo.bundleItems.toMultipleBundleDetails(bundleInfo, shopInfo)
        )
    }

    private fun ShopInformation.toBundleShopInfo(): BundleShopUiModel? {
        if (shopId.isZero()) return null
        return BundleShopUiModel(
            shopId = shopId.toString(),
            shopName = shopName,
            shopIconUrl = shopBadge,
        )
    }

    private fun Map<Long, List<BundleInfo>>.groupBundleToBundleUi(): List<BundleUiModel> {
        return map { mapToBundleUi(it.value) }
    }

    private fun String.toBundleType(): BundleTypes {
        return if (this == BUNDLE_TYPE_SINGLE.toString()) BundleTypes.SINGLE_BUNDLE
        else BundleTypes.MULTIPLE_BUNDLE
    }

    private fun List<BundleInfo>.toSingleBundleDetails(shopInfo: BundleShopUiModel?): List<BundleDetailUiModel> {
        return map {
            val bundleItem = it.bundleItems.firstOrNull()
            val hasVariant = bundleItem?.children?.isNotEmpty().orFalse()
            val minOrder = if (hasVariant) {
                val child = bundleItem?.children?.minByOrNull { child ->
                    child.bundlePrice
                }
                child?.minOrder.orZero()
            } else {
                bundleItem?.minOrder.orZero()
            }

            val bundlePrice = bundleItem?.getPreviewBundlePrice().orZero() * minOrder
            val originalPrice = bundleItem?.getPreviewOriginalPrice().orZero() * minOrder
            val preorder = getPreorderWording(context, it.preorder)
            val productSoldInfo = it.getProductSoldInfo()

            initializeBundleDetail(originalPrice, bundlePrice, shopInfo, it.bundleItems).apply {
                this.minOrder = minOrder
                this.minOrderWording = context.getString(R.string.bundlewidget_min_order_format, minOrder.getNumberFormatted())
                this.bundleId = it.bundleID.toString()
                this.bundleName = it.name
                this.preOrderInfo = preorder.orEmpty()
                this.isPreOrder = !preorder.isNullOrBlank()
                this.useProductSoldInfo = productSoldInfo.isNotBlank()
                this.productSoldInfo = productSoldInfo
            }
        }
    }

    private fun List<BundleItem>.toMultipleBundleDetails(
        bundleInfo: BundleInfo,
        shopInfo: BundleShopUiModel?
    ): List<BundleDetailUiModel> {
        val bundlePrice = sumOf { it.getMultipliedBundlePrice() }
        val originalPrice = sumOf { it.getMultipliedOriginalPrice() }
        val preorder = getPreorderWording(context, bundleInfo.preorder)
        val productSoldInfo = bundleInfo.getProductSoldInfo()

        return listOf(
            initializeBundleDetail(originalPrice, bundlePrice, shopInfo, this).apply {
                this.bundleId = bundleInfo.bundleID.toString()
                this.bundleName = bundleInfo.name
                this.preOrderInfo = preorder.orEmpty()
                this.isPreOrder = !preorder.isNullOrBlank()
                this.useProductSoldInfo = productSoldInfo.isNotBlank()
                this.productSoldInfo = productSoldInfo
            }
        )
    }

    private fun initializeBundleDetail(
        originalPrice: Double,
        bundlePrice: Double,
        shopInfo: BundleShopUiModel?,
        bundleItems: List<BundleItem>
    ): BundleDetailUiModel {
        val slashPrice = originalPrice - bundlePrice
        val discountPercentage = DiscountUtil.getDiscountPercentage(originalPrice, bundlePrice)
        return BundleDetailUiModel(
            originalPrice = originalPrice.getCurrencyFormatted(),
            displayPrice = bundlePrice.getCurrencyFormatted(),
            discountPercentage = discountPercentage,
            savingAmountWording = context.getString(R.string.bundlewidget_saving_amount_format, slashPrice.getCurrencyFormatted()),
            shopInfo = shopInfo,
            products = bundleItems.map {
                BundleProductUiModel(
                    productId = it.productID.toString(),
                    productName = it.name,
                    productImageUrl = it.picURL,
                    hasVariant = it.children.isNotEmpty(),
                    productCount = it.getPreviewMinOrder()
                )
            }
        )
    }

    private fun getPreorderWording(context: Context, preorder: Preorder): String? {
        if (preorder.status == PREORDER_STATUS_ACTIVE) {
            return context.getString(R.string.bundlewidget_preorder_format, preorder.processTime,
                getTimeUnitWording(context, preorder.processTypeNum))
        }
        return null
    }

    private fun getTimeUnitWording(context: Context, processTypeNum: Int): String {
        return when (processTypeNum) {
            ProductBundleConstants.PREORDER_TYPE_DAY -> context.getString(R.string.preorder_time_unit_day)
            ProductBundleConstants.PREORDER_TYPE_WEEK -> context.getString(R.string.preorder_time_unit_week)
            ProductBundleConstants.PREORDER_TYPE_MONTH -> context.getString(R.string.preorder_time_unit_month)
            else -> ""
        }
    }

    private fun BundleInfo.getProductSoldInfo(): String {
        val totalSold = bundleStats.totalSold.toIntSafely()
        return if (totalSold >= PRODUCT_SOLD_INFO_MIN_THRESHOLD)
            context.getString(R.string.product_bundle_bundle_sold, totalSold.thousandFormatted())
        else ""
    }

    companion object {
        private const val PREORDER_STATUS_ACTIVE: String = "ACTIVE"
        private const val PRODUCT_SOLD_INFO_MIN_THRESHOLD = 1
    }
}
