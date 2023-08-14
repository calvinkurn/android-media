package com.tokopedia.feedcomponent.domain.mapper

import com.tokopedia.feed.component.product.ContentTaggedProductUiModel
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCampaign
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct

/**
 * Created By : Muhammad Furqan on 19/06/23
 */
object ProductMapper {
    fun transform(
        product: FeedXProduct,
        campaign: FeedXCampaign,
        sourceType: ContentTaggedProductUiModel.SourceType
    ): ContentTaggedProductUiModel {
        val newCampaign = mapCampaignProduct(product, campaign)
        return ContentTaggedProductUiModel(
            id = product.id,
            shop = ContentTaggedProductUiModel.Shop(
                id = product.shopID,
                name = product.shopName
            ),
            appLink = product.appLink,
            title = product.name,
            imageUrl = product.coverURL,
            price = if (campaign.isUpcoming) {
                ContentTaggedProductUiModel.CampaignPrice(
                    originalFormattedPrice = product.priceFmt,
                    formattedPrice = product.priceMaskedFmt,
                    price = product.priceMasked.toDouble()
                )
            } else if (product.isDiscount) {
                ContentTaggedProductUiModel.DiscountedPrice(
                    discount = product.discount,
                    originalFormattedPrice = product.priceOriginalFmt,
                    formattedPrice = product.priceDiscountFmt,
                    price = product.priceDiscount.toDouble()
                )
            } else {
                ContentTaggedProductUiModel.NormalPrice(
                    formattedPrice = product.priceFmt,
                    price = product.price.toDouble()
                )
            },
            campaign = newCampaign,
            affiliate = ContentTaggedProductUiModel.Affiliate(
                id = product.affiliate.id,
                channel = product.affiliate.channel
            ),
            parentID = product.parentID,
            showGlobalVariant = product.hasVariant && product.isParent,
            stock = if (product.isAvailable || sourceType == ContentTaggedProductUiModel.SourceType.NonOrganic)
                ContentTaggedProductUiModel.Stock.Available else ContentTaggedProductUiModel.Stock.OutOfStock
        )
    }

    private fun mapCampaignProduct(
        product: FeedXProduct,
        campaign: FeedXCampaign
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
            isExclusiveForMember = campaign.isRSFollowersRestrictionOn
        )
    }
}
