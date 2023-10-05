package com.tokopedia.feedcomponent.domain.mapper

import com.tokopedia.feed.component.product.FeedTaggedProductUiModel
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCampaign
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct

/**
 * Created By : Muhammad Furqan on 19/06/23
 */
object ProductMapper {
    fun transform(
        product: FeedXProduct,
        campaign: FeedXCampaign,
        sourceType: FeedTaggedProductUiModel.SourceType
    ): FeedTaggedProductUiModel {
        val newCampaign = mapCampaignProduct(product, campaign)
        return FeedTaggedProductUiModel(
            id = product.id,
            shop = FeedTaggedProductUiModel.Shop(
                id = product.shopID,
                name = product.shopName
            ),
            appLink = product.appLink,
            title = product.name,
            imageUrl = product.coverURL,
            price = if (campaign.isUpcoming) {
                FeedTaggedProductUiModel.CampaignPrice(
                    originalFormattedPrice = product.priceFmt,
                    formattedPrice = product.priceMaskedFmt,
                    price = product.priceMasked.toDouble()
                )
            } else if (product.isDiscount) {
                FeedTaggedProductUiModel.DiscountedPrice(
                    discount = product.discount,
                    originalFormattedPrice = product.priceOriginalFmt,
                    formattedPrice = product.priceDiscountFmt,
                    price = product.priceDiscount.toDouble()
                )
            } else {
                FeedTaggedProductUiModel.NormalPrice(
                    formattedPrice = product.priceFmt,
                    price = product.price.toDouble()
                )
            },
            campaign = newCampaign,
            affiliate = FeedTaggedProductUiModel.Affiliate(
                id = product.affiliate.id,
                channel = product.affiliate.channel
            ),
            parentID = product.parentID,
            showGlobalVariant = product.hasVariant && product.isParent,
            stock = if (product.isAvailable || sourceType == FeedTaggedProductUiModel.SourceType.NonOrganic)
                FeedTaggedProductUiModel.Stock.Available else FeedTaggedProductUiModel.Stock.OutOfStock
        )
    }

    private fun mapCampaignProduct(
        product: FeedXProduct,
        campaign: FeedXCampaign
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
            isExclusiveForMember = campaign.isRSFollowersRestrictionOn
        )
    }
}
