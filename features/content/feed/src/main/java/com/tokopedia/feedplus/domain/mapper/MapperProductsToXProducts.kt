package com.tokopedia.feedplus.domain.mapper

import com.tokopedia.feed.component.product.FeedTaggedProductUiModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import okhttp3.internal.format

/**
 * Created By : Muhammad Furqan on 12/04/23
 */
object MapperProductsToXProducts {
    fun transform(product: FeedCardProductModel, campaign: FeedCardCampaignModel): FeedTaggedProductUiModel {
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
                    formattedPrice = product.priceFmt,
                    price = product.price
                )
            } else {
                FeedTaggedProductUiModel.NormalPrice(
                    formattedPrice = product.priceFmt,
                    price = product.price
                )
            },
            campaign = newCampaign
        )
    }

    private fun mapCampaignProduct(product: FeedCardProductModel, campaign: FeedCardCampaignModel) : FeedTaggedProductUiModel.Campaign {
        val status = when (campaign.status) {
            "upcoming" -> {
                FeedTaggedProductUiModel.CampaignStatus.Upcoming
            }
            "ongoing" -> {
                FeedTaggedProductUiModel.CampaignStatus.OnGoing(product.stockWording, product.stockSoldPercentage)
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
            type = type
        )
    }
}
