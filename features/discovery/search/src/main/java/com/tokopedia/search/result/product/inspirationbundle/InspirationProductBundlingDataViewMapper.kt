package com.tokopedia.search.result.product.inspirationbundle

import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.TYPE_INSPIRATION_CAROUSEL_SINGLE_BUNDLING
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.shop.common.widget.bundle.enum.BundleTypes
import com.tokopedia.shop.common.widget.bundle.model.BundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleShopUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel
import com.tokopedia.utils.currency.CurrencyFormatUtil

object InspirationProductBundlingDataViewMapper {
    private const val SAVING_AMOUNT_FORMAT = "Hemat <b>%s</b>"

    internal fun InspirationCarouselDataView.convertToInspirationProductBundleDataView(
        keyword: String,
        externalReference: String,
    ): InspirationProductBundleDataView {
        val bundleList = options.map { option ->
                option.toBundleDataView(
                    title,
                    type,
                    trackingOption,
                    option.dimension90,
                    keyword,
                    externalReference,
                )
            }
        return InspirationProductBundleDataView(
            title,
            type,
            position,
            layout,
            trackingOption,
            bundleList,
        )
    }

    private fun InspirationCarouselDataView.Option.toBundleDataView(
        title: String,
        type: String,
        trackingOption: Int,
        dimension90: String,
        keyword: String,
        externalReference: String,
    ): InspirationProductBundleDataView.BundleDataView {
        val bundleDetailUiModel = if (type == TYPE_INSPIRATION_CAROUSEL_SINGLE_BUNDLING) {
            convertToSingleBundle()
        } else {
            convertToMultipleBundle()
        }
        return InspirationProductBundleDataView.BundleDataView(
            carouselTitle = title,
            applink = applink,
            componentId = componentId,
            bundle = bundleDetailUiModel,
            trackingOption = trackingOption,
            dimension90 = dimension90,
            type = type,
            keyword = keyword,
            externalReference = externalReference,
            option = this,
        )
    }

    private fun InspirationCarouselDataView.Option.convertToSingleBundle(): BundleUiModel {
        return BundleUiModel(
            bundleName = title,
            bundleType = BundleTypes.SINGLE_BUNDLE,
            bundleDetails = product.map { it.convertToSingleBundleDetail(bundle) }
        )
    }

    private fun InspirationCarouselDataView.Option.Product.convertToSingleBundleDetail(
        bundle: InspirationCarouselDataView.Bundle,
    ): BundleDetailUiModel {
        return BundleDetailUiModel(
            bundleId = bundleId,
            minOrderWording = label,
            originalPrice = originalPrice,
            displayPrice = priceStr,
            displayPriceRaw = price.toLong(),
            savingAmountWording = SAVING_AMOUNT_FORMAT.format(discount),
            discountPercentage = discountPercentage,
            shopInfo = BundleShopUiModel(
                shopId = "",
                shopName = bundle.shop.name,
                shopIconUrl = bundle.shop.url,
            ),
            products = listOf(convertToBundleProductDataView(this)),
            productSoldInfo = bundle.countSold,
            useProductSoldInfo = true,
        )
    }

    private fun InspirationCarouselDataView.Option.convertToMultipleBundle(): BundleUiModel {
        return BundleUiModel(
            bundleName = title,
            bundleDetails = listOf(convertToMultipleBundleDetail())
        )
    }

    private fun InspirationCarouselDataView.Option.convertToMultipleBundleDetail(): BundleDetailUiModel {
        return BundleDetailUiModel(
            originalPrice = bundle.originalPrice,
            displayPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(bundle.price, false),
            displayPriceRaw = bundle.price,
            savingAmountWording = SAVING_AMOUNT_FORMAT.format(bundle.discount),
            discountPercentage = bundle.discountPercentage,
            shopInfo = BundleShopUiModel(
                shopId = "",
                shopName = bundle.shop.name,
                shopIconUrl = bundle.shop.url,
            ),
            products = convertToBundleProductDataView(product),
            productSoldInfo = bundle.countSold,
            useProductSoldInfo = true,
        )
    }

    private fun convertToBundleProductDataView(
        product: InspirationCarouselDataView.Option.Product,
    ): BundleProductUiModel {
        return BundleProductUiModel(
            productId = product.id,
            productName = product.name,
            productImageUrl = product.imgUrl,
            productAppLink = product.applink,
        )
    }

    private fun convertToBundleProductDataView(
        products: List<InspirationCarouselDataView.Option.Product>,
    ): List<BundleProductUiModel> {
        return products.map { product -> convertToBundleProductDataView(product) }
    }
}