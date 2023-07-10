package com.tokopedia.feedplus.domain.mapper

import com.tokopedia.feed.component.product.FeedTaggedProductUiModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel

/**
 * Created By : Muhammad Furqan on 12/04/23
 */
object MapperProductsToXProducts {
    fun transform(
        product: FeedCardProductModel,
        campaign: FeedCardCampaignModel
    ): FeedTaggedProductUiModel {
        val newCampaign = mapCampaignProduct(product, campaign)
        return FeedTaggedProductUiModel(
            id = product.id,
            shop = FeedTaggedProductUiModel.Shop(
                id = product.shopId,
                name = product.shopName
            ),
            appLink = product.applink,
            title = product.name,
            imageUrl = product.coverUrl,
            price = if (campaign.isUpcoming) {
                FeedTaggedProductUiModel.CampaignPrice(
                    originalFormattedPrice = product.priceFmt,
                    formattedPrice = product.priceMaskedFmt,
                    price = product.priceMasked
                )
            } else if (product.isDiscount) {
                FeedTaggedProductUiModel.DiscountedPrice(
                    discount = product.discount.toInt(),
                    originalFormattedPrice = product.priceOriginalFmt,
                    formattedPrice = product.priceDiscountFmt,
                    price = product.priceDiscount
                )
            } else {
                FeedTaggedProductUiModel.NormalPrice(
                    formattedPrice = product.priceFmt,
                    price = product.price
                )
            },
            campaign = newCampaign,
            stock = if (product.isAvailable) FeedTaggedProductUiModel.Stock.Available else FeedTaggedProductUiModel.Stock.OutOfStock //TODO() = adjust rules
        )
    }

    private fun mapCampaignProduct(
        product: FeedCardProductModel,
        campaign: FeedCardCampaignModel
    ): FeedTaggedProductUiModel.Campaign {
        val status = when (campaign.status) {
            "upcoming" -> {
                FeedTaggedProductUiModel.CampaignStatus.Upcoming
            }
            "ongoing" -> {
                FeedTaggedProductUiModel.CampaignStatus.Ongoing(
                    product.stockWording,
                    product.stockSoldPercentage
                )
            }
            else -> {
                FeedTaggedProductUiModel.CampaignStatus.Unknown
            }
        }
        val type = when (campaign.name) {
            "asgc_flash_sale_toko", "Rilisan Spesial" -> {
                FeedTaggedProductUiModel.CampaignType.FlashSaleToko
            }
            "asgc_rilisan_spesial", "Flash Sale Toko" -> {
                FeedTaggedProductUiModel.CampaignType.RilisanSpecial
            }
            else -> FeedTaggedProductUiModel.CampaignType.NoCampaign
        }
        return FeedTaggedProductUiModel.Campaign(
            status = status,
            type = type,
            isExclusiveForMember = campaign.isExclusiveForMember
        )
    }
}
