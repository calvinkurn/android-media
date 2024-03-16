package com.tokopedia.topads.sdk.v2.base

import android.content.Context
import android.view.View
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.topads.sdk.common.constants.TopAdsConstants
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.LabelGroup
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.v2.uimodel.ShopAdsWithSingleProductModel
import com.tokopedia.unifycomponents.UnifyButton
import java.lang.ref.WeakReference
import java.util.ArrayList

abstract class BaseBannerAdsRendering(
    val view: View?,
    val contextRef: WeakReference<Context>,
    val topAdsUrlHitter: TopAdsUrlHitter? = null,
    val isReimagine: Boolean = false
) {
    abstract fun render(cpmModel: CpmModel, cpmData: CpmData, appLink: String, adsClickUrl: String)

    val className: (String) -> String = { currentClassName ->
        "com.tokopedia.topads.sdk.widget.TopAdsBannerView.$currentClassName"
    }

    protected fun getProductCardModels(products: List<Product>, hasAddProductToCartButton: Boolean): ArrayList<ProductCardModel> {
        return ArrayList<ProductCardModel>().apply {
            products.map {
                add(getProductCardViewModel(it, hasAddProductToCartButton))
            }
        }
    }

    private fun getProductCardViewModel(product: Product, hasAddProductToCartButton: Boolean): ProductCardModel {
        val isAvailAble = checkIfDTAvailable(product.labelGroupList)
        val productCardModel = ProductCardModel(
            productImageUrl = product.imageProduct.imageUrl,
            productName = product.name,
            discountPercentage = if (product.campaign.discountPercentage != 0) "${product.campaign.discountPercentage}%" else "",
            formattedPrice = product.priceFormat,
            reviewCount = product.countReviewFormat.toIntOrZero(),
            ratingString = product.productRatingFormat,
            freeOngkir = ProductCardModel.FreeOngkir(
                product.freeOngkir.isActive,
                product.freeOngkir.imageUrl
            ),
            hasAddToCartButton = hasAddProductToCartButton,
            addToCartButtonType = UnifyButton.Type.MAIN,
            stockBarPercentage = product.stock_info.soldStockPercentage,
            stockBarLabel = product.stock_info.stockWording,
            stockBarLabelColor = product.stock_info.stockColour,
            shopBadgeList = product.badges.map {
                ProductCardModel.ShopBadge(
                    imageUrl = it.imageUrl,
                    title = it.title,
                    isShown = it.isShow
                )
            }
        )
        return getProductModelOnCondition(product, isAvailAble, productCardModel)
    }

    private fun checkIfDTAvailable(labelGroupList: List<LabelGroup>): Boolean {
        var isAvailable = false
        run breaking@{
            labelGroupList.forEach {
                if (it.position == TopAdsConstants.FULFILLMENT && it.title == TopAdsConstants.DILYANI_TOKOPEDIA) {
                    isAvailable = true
                    return@breaking
                }
            }
        }
        return isAvailable
    }

    private fun getProductModelOnCondition(
        product: Product,
        isAvailAble: Boolean,
        productCardModel: ProductCardModel
    ): ProductCardModel {
        val labelGroupList = ArrayList<ProductCardModel.LabelGroup>().apply {
            product.labelGroupList.filter { it.position != "integrity" }.mapTo(this) { labelGroup ->
                ProductCardModel.LabelGroup(
                    position = labelGroup.position,
                    title = labelGroup.title,
                    type = labelGroup.type,
                    imageUrl = labelGroup.imageUrl,
                    styleList = labelGroup.styleList.map { style ->
                        ProductCardModel.LabelGroup.Style(style.key, style.value)
                    }
                )
            }
        }

        if (isAvailAble) {
            return if (!product.campaign.originalPrice.isNullOrEmpty()) {
                productCardModel.copy(
                    slashedPrice = product.campaign.originalPrice,
                    labelGroupList = labelGroupList
                )
            } else {
                productCardModel.copy(
                    labelGroupList = labelGroupList
                )
            }
        }

        return productCardModel.copy(
            slashedPrice = product.campaign.originalPrice,
            countSoldRating = product.headlineProductRatingAverage,
            labelGroupList = labelGroupList
        )
    }

    protected fun getSingleAdsProductModel(
        cpmData: CpmData,
        appLink: String,
        adsClickUrl: String,
        topAdsBannerClickListener: TopAdsBannerClickListener?,
        hasAddProductToCartButton: Boolean,
        impressionListener: TopAdsItemImpressionListener?
    ): ShopAdsWithSingleProductModel {
        return ShopAdsWithSingleProductModel(
            isOfficial = cpmData.cpm.cpmShop.isOfficial,
            isPMPro = cpmData.cpm.cpmShop.isPMPro,
            isPowerMerchant = cpmData.cpm.cpmShop.isPowerMerchant,
            shopBadge = cpmData.cpm.badges.firstOrNull()?.imageUrl ?: "",
            shopName = cpmData.cpm.cpmShop.name,
            shopImageUrl = cpmData.cpm.cpmImage.fullEcs,
            slogan = cpmData.cpm.cpmShop.slogan,
            shopWidgetImageUrl = cpmData.cpm.widgetImageUrl,
            merchantVouchers = cpmData.cpm.cpmShop.merchantVouchers,
            listItem = cpmData.cpm.cpmShop.products.firstOrNull(),
            shopApplink = appLink,
            adsClickUrl = adsClickUrl,
            hasAddToCartButton = hasAddProductToCartButton,
            variant = cpmData.cpm.layout,
            topAdsBannerClickListener = topAdsBannerClickListener,
            impressionListener = impressionListener,
            cpmData = cpmData,
            impressHolder = cpmData.cpm.cpmShop.imageShop
        )
    }
}
