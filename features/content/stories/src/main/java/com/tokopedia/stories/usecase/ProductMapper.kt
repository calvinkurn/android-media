package com.tokopedia.stories.usecase

import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.stories.uimodel.StoriesCampaignStatus
import com.tokopedia.stories.uimodel.StoriesCampaignType
import com.tokopedia.stories.usecase.response.StoriesProductResponse
import javax.inject.Inject

/**
 * @author by astidhiyaa on 21/08/23
 */
class ProductMapper @Inject constructor() {
    fun mapProducts(
        response: StoriesProductResponse.Data,
        shopId: String
    ): List<ContentTaggedProductUiModel> {
        return response.products.map { product ->
            val campaign = mapCampaign(response.campaign, product)
            ContentTaggedProductUiModel(
                id = product.id,
                shop = ContentTaggedProductUiModel.Shop(
                    id = shopId,
                    name = "", //TODO()
                ),
                appLink = product.appLink,
                title = product.name,
                imageUrl = product.imageUrl,
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
                campaign = campaign,
                affiliate = ContentTaggedProductUiModel.Affiliate.Empty,
                parentID = product.parentID,
                showGlobalVariant = product.hasVariant && product.isParent,
                stock = if (product.isStockAvailable) ContentTaggedProductUiModel.Stock.Available else ContentTaggedProductUiModel.Stock.OutOfStock //TODO() : ASGC always available
            )
        }
    }

    private fun mapCampaign(
        campaign: StoriesProductResponse.Data.Campaign,
        product: StoriesProductResponse.Data.Product
    ): ContentTaggedProductUiModel.Campaign {

        val status = when (StoriesCampaignStatus.convertValue(campaign.status)) {
            StoriesCampaignStatus.Upcoming -> ContentTaggedProductUiModel.CampaignStatus.Upcoming
            StoriesCampaignStatus.Ongoing -> ContentTaggedProductUiModel.CampaignStatus.Ongoing(
                product.stockWording, product.stockSoldPercentage
            )

            else -> ContentTaggedProductUiModel.CampaignStatus.Unknown
        }

        val type = when (StoriesCampaignType.convertValue(campaign.name)) {
            StoriesCampaignType.ASGCFlashSale, StoriesCampaignType.RilisanSpesial -> ContentTaggedProductUiModel.CampaignType.FlashSaleToko
            StoriesCampaignType.ASGCRilisanSpesial, StoriesCampaignType.FlashSaleToko -> ContentTaggedProductUiModel.CampaignType.RilisanSpecial
            else -> ContentTaggedProductUiModel.CampaignType.NoCampaign
        }
        return ContentTaggedProductUiModel.Campaign(
            type = type, status = status, isExclusiveForMember = campaign.isFollowRestriction
        )
    }
}


