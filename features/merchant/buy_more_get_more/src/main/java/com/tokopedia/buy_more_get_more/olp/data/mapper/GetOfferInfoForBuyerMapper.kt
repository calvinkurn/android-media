package com.tokopedia.buy_more_get_more.olp.data.mapper

import com.tokopedia.buy_more_get_more.olp.data.response.OfferInfoForBuyerResponse
import com.tokopedia.buy_more_get_more.olp.data.response.OfferInfoForBuyerResponse.*
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.Offering.*
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel.Offering.Tier.Rule
import javax.inject.Inject

class GetOfferInfoForBuyerMapper @Inject constructor() {

    fun map(response: OfferInfoForBuyerResponse): OfferInfoForBuyerUiModel {
        return OfferInfoForBuyerUiModel(
            responseHeader = response.offeringInforBuyer.responseHeader.toResponseHeaderModel(),
            offeringJsonData = response.offeringInforBuyer.offeringJsonData,
            offerings = response.offeringInforBuyer.offerings.toOfferingUiModel()
        )
    }

    private fun ResponseHeader.toResponseHeaderModel(): OfferInfoForBuyerUiModel.ResponseHeader {
        return OfferInfoForBuyerUiModel.ResponseHeader(
            success = success,
            error_code = errorCode,
            processTime = processTime
        )
    }

    private fun List<Offering>.toOfferingUiModel(): List<OfferInfoForBuyerUiModel.Offering> {
        return map {
            OfferInfoForBuyerUiModel.Offering(
                id = it.id,
                offerName = it.offerName,
                offerTypeId = it.offerTypeId,
                startDate = it.startDate,
                endDate = it.endDate,
                maxAppliedTier = it.maxAppliedTier,
                tierList = it.tierList.toTierListUiModel(),
                shopData = it.shopData.toShopData()
            )
        }
    }

    private fun Offering.ShopData.toShopData(): ShopData {
        return ShopData(
            shopId = shopId,
            shopName = shopName,
            badge = badge
        )
    }

    private fun List<Offering.Tier>.toTierListUiModel(): List<Tier> {
        return map {
            Tier(
                tierId = it.tierId,
                level = it.level,
                rules = it.rules.toRuleListUiModel(),
                benefits = it.benefits.toBenefitListUiModel(),
                attributes = it.attributes
            )
        }
    }

    private fun List<Offering.Tier.Rule>.toRuleListUiModel(): List<Rule> {
        return map {
            Rule(
                typeId = it.typeId,
                operation = it.operation,
                value = it.value
            )
        }
    }

    private fun List<Offering.Tier.Benefit>.toBenefitListUiModel(): List<Tier.Benefit> {
        return map {
            Tier.Benefit(
                typeId = it.typeId,
                value = it.value
            )
        }
    }
}
