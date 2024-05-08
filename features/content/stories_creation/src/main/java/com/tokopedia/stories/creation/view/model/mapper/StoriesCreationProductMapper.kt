package com.tokopedia.stories.creation.view.model.mapper

import com.tokopedia.content.product.picker.seller.model.DiscountedPrice
import com.tokopedia.content.product.picker.seller.model.OriginalPrice
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignStatus
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.content.product.picker.seller.model.paged.PagedDataUiModel
import com.tokopedia.content.product.picker.seller.model.pinnedproduct.PinProductUiModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.content.product.picker.seller.util.PriceFormatUtil
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.stories.creation.model.GetStoryProductDetailsResponse
import com.tokopedia.stories.creation.model.GetStoryProductEtalaseResponse
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 13, 2023
 */
class StoriesCreationProductMapper @Inject constructor(
    private val priceFormatUtil: PriceFormatUtil
) {

    fun mapProductEtalase(
        response: GetStoryProductEtalaseResponse
    ): PagedDataUiModel<ProductUiModel> {
        return PagedDataUiModel(
            dataList = response.data.products.map { data ->
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
                    shopName = "",
                    shopBadge = "",
                    rating = "",
                    countSold = ""
                )
            },
            hasNextPage = response.data.pagerCursor.hasNext,
            cursor = response.data.pagerCursor.cursor
        )
    }

    fun mapProductDetails(
        response: GetStoryProductDetailsResponse
    ): List<ProductTagSectionUiModel> {
        return if (response.data.products.isEmpty()) {
            emptyList()
        } else {
            listOf(
                ProductTagSectionUiModel(
                    name = "",
                    campaignStatus = CampaignStatus.Unknown,
                    products = response.data.products.map { product ->
                        ProductUiModel(
                            id = product.productID,
                            name = product.productName,
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
                            pinStatus = PinProductUiModel.Empty,
                            number = "",
                            hasCommission = false,
                            commission = 0L,
                            commissionFmt = "",
                            extraCommission = false,
                            shopName = "",
                            shopBadge = "",
                            rating = "",
                            countSold = ""
                        )
                    }
                )
            )
        }
    }
}
