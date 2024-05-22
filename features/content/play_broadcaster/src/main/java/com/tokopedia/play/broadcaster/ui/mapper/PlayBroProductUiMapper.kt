package com.tokopedia.play.broadcaster.ui.mapper

import com.tokopedia.content.product.picker.seller.model.DiscountedPrice
import com.tokopedia.content.product.picker.seller.model.OriginalPrice
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignStatus
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.content.product.picker.seller.model.paged.PagedDataUiModel
import com.tokopedia.content.product.picker.seller.model.pinnedproduct.PinProductUiModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.content.product.picker.seller.util.PriceFormatUtil
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.broadcaster.domain.model.GetProductsByEtalaseResponse
import com.tokopedia.play.broadcaster.domain.model.campaign.GetProductTagSummarySectionResponse
import com.tokopedia.play.broadcaster.domain.model.socket.SectionedProductTagSocketResponse
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class PlayBroProductUiMapper @Inject constructor(
    private val priceFormatUtil: PriceFormatUtil
) {
    fun mapProductsInEtalase(
        response: GetProductsByEtalaseResponse
    ): PagedDataUiModel<ProductUiModel> {
        return PagedDataUiModel(
            dataList = response.wrapper.products.map { data ->
                ProductUiModel(
                    id = data.id,
                    name = data.name,
                    imageUrl = data.pictures.firstOrNull()?.urlThumbnail.orEmpty(),
                    stock = data.stock.toLong(),
                    price = OriginalPrice(
                        priceFormatUtil.format(BigDecimal(data.price.min.orZero())),
                        data.price.min.orZero()
                    ),
                    hasCommission = false,
                    commissionFmt = "",
                    commission = 0L,
                    extraCommission = false,
                    pinStatus = PinProductUiModel.Empty,
                    number = "",
                    shopBadge = "",
                    shopName = "",
                    rating = "",
                    countSold = ""
                )
            },
            hasNextPage = response.wrapper.pagerCursor.hasNext,
            cursor = response.wrapper.pagerCursor.cursor
        )
    }

    fun mapSectionedProduct(
        response: SectionedProductTagSocketResponse
    ): List<ProductTagSectionUiModel> {
        return response.sections.map {
            ProductTagSectionUiModel(
                name = it.title,
                campaignStatus = mapCampaignStatusFromType(it.type),
                products = it.products.map { product ->
                    ProductUiModel(
                        id = product.id,
                        name = product.name,
                        imageUrl = product.imageUrl,
                        stock = product.quantity,
                        price = if (product.discount <= 0) {
                            OriginalPrice(product.originalPriceFmt, product.originalPrice)
                        } else {
                            DiscountedPrice(
                                originalPrice = product.originalPriceFmt,
                                originalPriceNumber = product.originalPrice,
                                discountPercent = product.discount.toLong(),
                                discountedPrice = product.priceFmt,
                                discountedPriceNumber = product.price
                            )
                        },
                        pinStatus = getPinStatus(isPinned = product.isPinned, canPin = product.isPinnable),
                        number = product.productNumber.toString(),
                        hasCommission = false,
                        commissionFmt = "",
                        commission = 0L,
                        extraCommission = false,
                        shopBadge = "",
                        shopName = "",
                        rating = "",
                        countSold = ""
                    )
                    /** TODO
                     * add shop_badge, shop_name, creator_sold_qty_fmt
                     * after websocket implementation done from BE
                     **/
                }
            )
        }
    }

    fun mapProductTagSection(
        response: GetProductTagSummarySectionResponse
    ): List<ProductTagSectionUiModel> {
        return response.broadcasterGetProductTagSection.sections.map {
            ProductTagSectionUiModel(
                name = it.name,
                campaignStatus = mapCampaignStatusFromType(it.statusFmt),
                products = it.products.map { product ->
                    ProductUiModel(
                        id = product.productID,
                        name = product.productName,
                        hasCommission = product.hasCommission,
                        commissionFmt = product.commissionFmt,
                        commission = product.commission,
                        extraCommission = product.extraCommission,
                        imageUrl = product.imageURL,
                        stock = product.quantity.toLong(),
                        price = if (product.discount == "0") {
                            OriginalPrice(product.originalPriceFmt, product.originalPrice.toDouble())
                        } else {
                            DiscountedPrice(
                                originalPrice = product.originalPriceFmt,
                                originalPriceNumber = product.originalPrice.toDouble(),
                                discountPercent = product.discount.toLong(),
                                discountedPrice = product.priceFmt,
                                discountedPriceNumber = product.price.toDouble()
                            )
                        },
                        pinStatus = getPinStatus(isPinned = product.isPinned, canPin = product.isPinnable),
                        number = product.productNumber.toString(),
                        shopBadge = product.shopBadge,
                        shopName = product.shopName,
                        rating = product.rating,
                        countSold = product.countSold
                    )
                }
            )
        }
    }

    /** Util */
    private fun mapCampaignStatusFromType(type: String): CampaignStatus {
        return when (type.lowercase()) {
            "mendatang", "upcoming" -> CampaignStatus.Ready
            "berlangsung", "active" -> CampaignStatus.Ongoing
            else -> CampaignStatus.Unknown
        }
    }

    /**
     * Pinned Product
     * isPinnable -> not eligible to pin product-> hide pin container [case: out of stock]
     * isPinned -> show [status: Lepas / Pin]
     */
    private fun getPinStatus(isPinned: Boolean, canPin: Boolean): PinProductUiModel =
        PinProductUiModel(isPinned = isPinned, canPin = if (isPinned) true else canPin)
}
