package com.tokopedia.shop_widget.buy_more_save_more.data.mapper

import com.tokopedia.campaign.data.response.OfferInfoForBuyerResponse
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingInfoForBuyerUiModel
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingInfoForBuyerUiModel.Offering
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingInfoForBuyerUiModel.Offering.Tier
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingInfoForBuyerUiModel.Offering.Tier.Rule
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingInfoForBuyerUiModel.ResponseHeader
import com.tokopedia.shop_widget.buy_more_save_more.entity.enums.Status
import javax.inject.Inject

class GetOfferingInfoForBuyerMapper@Inject constructor() {

    fun map(response: OfferInfoForBuyerResponse): OfferingInfoForBuyerUiModel {
        return OfferingInfoForBuyerUiModel(
            responseHeader = response.offeringInforBuyer.responseHeader.toResponseHeaderModel(),
            offeringJsonData = response.offeringInforBuyer.offeringJsonData,
            nearestWarehouseIds = response.offeringInforBuyer.nearestWarehouseIds,
            offerings = response.offeringInforBuyer.offerings.toOfferingUiModel()
        )
    }

    private fun OfferInfoForBuyerResponse.ResponseHeader.toResponseHeaderModel(): ResponseHeader {
        return ResponseHeader(
            success = success,
            status = Status.values().firstOrNull { value ->
                value.code == errorCode
            } ?: Status.SUCCESS,
            processTime = processTime
        )
    }

    private fun List<OfferInfoForBuyerResponse.Offering>.toOfferingUiModel(): List<Offering> {
        return map {
            Offering(
                id = it.id,
                shopData = Offering.ShopData(
                    shopId = it.shopData.shopId,
                    shopName = it.shopData.shopName,
                    badge = it.shopData.badge
                ),
                offerName = it.offerName,
                offerTypeId = it.offerTypeId,
                startDate = it.startDate,
                endDate = it.endDate,
                maxAppliedTier = it.maxAppliedTier,
                tierList = it.tierList.toTierListUiModel(),
                tnc = it.tnc,
                olpLink = it.olpLink,
                olpAppLink = it.olpAppLink,
                upsellWording = it.upsellWording
            )
        }
    }

    private fun List<OfferInfoForBuyerResponse.Offering.Tier>.toTierListUiModel(): List<Tier> {
        return map {
            Tier(
                tierId = it.tierId,
                level = it.level,
                tierWording = it.tierWording,
                rules = it.rules.toRuleListUiModel(),
                benefits = it.benefits.toBenefitListUiModel(),
                attributes = it.attributes
            )
        }
    }

    private fun List<OfferInfoForBuyerResponse.Offering.Tier.Rule>.toRuleListUiModel(): List<Rule> {
        return map {
            Rule(
                typeId = it.typeId,
                operation = it.operation,
                value = it.value
            )
        }
    }

    private fun List<OfferInfoForBuyerResponse.Offering.Tier.Benefit>.toBenefitListUiModel(): List<Tier.Benefit> {
        return map {
            Tier.Benefit(
                typeId = it.typeId,
                value = it.value,
                products = it.products.toProductBenefitListUiModel()
            )
        }
    }

    private fun List<OfferInfoForBuyerResponse.Offering.Tier.Benefit.ProductBenefit>.toProductBenefitListUiModel(): List<Tier.Benefit.ProductBenefit> {
        return map {
            Tier.Benefit.ProductBenefit(
                productId = it.productId,
                image = it.image,
                priority = it.priority
            )
        }
    }
}
