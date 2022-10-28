package com.tokopedia.shop.flashsale.data.mapper


import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.flashsale.common.constant.DateConstant
import com.tokopedia.shop.flashsale.common.extension.toDate
import com.tokopedia.shop.flashsale.data.response.BannerGeneratorDataResponse
import com.tokopedia.shop.flashsale.domain.entity.CampaignBanner
import javax.inject.Inject

class BannerGeneratorDataMapper @Inject constructor() {

    fun map(response: BannerGeneratorDataResponse): CampaignBanner {
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
            ),
            response.getMerchantCampaignBannerGeneratorData.campaign.startDate.toDate(DateConstant.DATE_TIME),
            response.getMerchantCampaignBannerGeneratorData.campaign.endDate.toDate(DateConstant.DATE_TIME)
        )

    }

}