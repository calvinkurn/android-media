package com.tokopedia.productbundlewidget.model

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.BUNDLE_TYPE_SINGLE
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.data.model.response.BundleItem
import com.tokopedia.product_bundle.common.data.model.response.ShopInformation
import com.tokopedia.product_bundle.common.util.DiscountUtil
import com.tokopedia.product_service_widget.R
import javax.inject.Inject

class ProductBundleWidgetUiMapper @Inject constructor(@ApplicationContext private val context: Context) {

    fun groupAndMap(bundleInfo: List<BundleInfo>): List<BundleUiModel> {
        val groupedBundle = bundleInfo.groupBy { it.groupID }
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
            else bundleInfo.bundleItems.toMultipleBundleDetails(bundleInfo.bundleID, shopInfo)
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
            val bundlePrice = bundleItem?.getPreviewBundlePrice().orZero()
            val originalPrice = bundleItem?.getPreviewOriginalPrice().orZero()

            initializeBundleDetail(originalPrice, bundlePrice, shopInfo, it.bundleItems).apply {
                this.minOrder = minOrder
                this.minOrderWording = context.getString(R.string.bundlewidget_min_order_format, minOrder)
                this.bundleId = it.bundleID.toString()
            }
        }
    }

    private fun List<BundleItem>.toMultipleBundleDetails(
        bundleId: Long,
        shopInfo: BundleShopUiModel?
    ): List<BundleDetailUiModel> {
        val bundlePrice = sumOf { it.getPreviewBundlePrice() }
        val originalPrice = sumOf { it.getPreviewOriginalPrice() }
        return listOf(
            initializeBundleDetail(originalPrice, bundlePrice, shopInfo, this).apply {
                this.bundleId = bundleId.toString()
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
                    hasVariant = it.children.isNotEmpty()
                )
            }
        )
    }
}
