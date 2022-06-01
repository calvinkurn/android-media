package com.tokopedia.shop.flash_sale.data.mapper


import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.flash_sale.data.response.MerchantCampaignBannerGeneratorDataResponse
import com.tokopedia.shop.flash_sale.domain.entity.CampaignBanner
import javax.inject.Inject

class CampaignBannerGeneratorDataMapper @Inject constructor() {

    fun map(response: MerchantCampaignBannerGeneratorDataResponse): CampaignBanner {
        return CampaignBanner(
            response.getMerchantCampaignBannerGeneratorData.campaign.campaignId.toLong().orZero(),
            response.getMerchantCampaignBannerGeneratorData.campaign.name,
            response.getMerchantCampaignBannerGeneratorData.campaign.highlightProducts.products.map { product ->
                CampaignBanner.Product(
                    product.imageUrl,
                    product.campaign.originalPrice,
                    product.campaign.discountedPrice,
                    product.campaign.discountPercentage
                )
            },
            response.getMerchantCampaignBannerGeneratorData.campaign.maxDiscountPercentage,
            response.getMerchantCampaignBannerGeneratorData.campaign.statusId.toInt().orZero(),
            CampaignBanner.Shop(
                response.getMerchantCampaignBannerGeneratorData.shopData.name,
                response.getMerchantCampaignBannerGeneratorData.shopData.domain,
                response.getMerchantCampaignBannerGeneratorData.shopData.logo,
                response.getMerchantCampaignBannerGeneratorData.shopData.isGold,
                response.getMerchantCampaignBannerGeneratorData.shopData.isOfficial,
                response.getMerchantCampaignBannerGeneratorData.shopData.badge.imageURL
            )
        )

    }

}