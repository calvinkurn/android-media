package com.tokopedia.feedplus.domain.mapper

import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel

/**
 * Created By : Muhammad Furqan on 12/04/23
 */
object MapperProductsToXProducts {
    fun transform(
        product: FeedCardProductModel,
        campaign: FeedCardCampaignModel,
        sourceType: ContentTaggedProductUiModel.SourceType
    ): ContentTaggedProductUiModel {
        val newCampaign = mapCampaignProduct(product, campaign)
        return ContentTaggedProductUiModel(
            id = product.id,
            parentID = product.parentID,
            showGlobalVariant = product.hasVariant && product.isParent,
            shop = ContentTaggedProductUiModel.Shop(
                id = product.shopId,
                name = product.shopName
            ),
            appLink = product.applink,
            title = product.name,
            imageUrl = product.coverUrl,
            price = if (campaign.isUpcoming) {
                ContentTaggedProductUiModel.CampaignPrice(
                    originalFormattedPrice = product.priceFmt,
                    formattedPrice = product.priceMaskedFmt,
                    price = product.priceMasked
                )
            } else if (product.isDiscount) {
                ContentTaggedProductUiModel.DiscountedPrice(
                    discount = product.discount.toInt(),
                    originalFormattedPrice = product.priceOriginalFmt,
                    formattedPrice = product.priceDiscountFmt,
                    price = product.priceDiscount
                )
            } else {
                ContentTaggedProductUiModel.NormalPrice(
                    formattedPrice = product.priceFmt,
                    price = product.price
                )
            },
            campaign = newCampaign,
            affiliate = product.affiliate.let {
                ContentTaggedProductUiModel.Affiliate(
                    it.id,
                    it.channel
                )
            },
            stock = if (product.isAvailable || sourceType == ContentTaggedProductUiModel.SourceType.NonOrganic)
                ContentTaggedProductUiModel.Stock.Available else ContentTaggedProductUiModel.Stock.OutOfStock
        )
    }

    private fun mapCampaignProduct(
        product: FeedCardProductModel,
        campaign: FeedCardCampaignModel
    ): ContentTaggedProductUiModel.Campaign {
        val status = when (campaign.status) {
            "upcoming" -> {
                ContentTaggedProductUiModel.CampaignStatus.Upcoming
            }
            "ongoing" -> {
                ContentTaggedProductUiModel.CampaignStatus.Ongoing(
                    product.stockWording,
                    product.stockSoldPercentage
                )
            }
            else -> {
                ContentTaggedProductUiModel.CampaignStatus.Unknown
            }
        }
        val type = when (campaign.name) {
            "asgc_flash_sale_toko", "Rilisan Spesial" -> {
                ContentTaggedProductUiModel.CampaignType.FlashSaleToko
            }
            "asgc_rilisan_spesial", "Flash Sale Toko" -> {
                ContentTaggedProductUiModel.CampaignType.RilisanSpecial
            }
            else -> ContentTaggedProductUiModel.CampaignType.NoCampaign
        }
        return ContentTaggedProductUiModel.Campaign(
            status = status,
            type = type,
            isExclusiveForMember = campaign.isExclusiveForMember
        )
    }
}
