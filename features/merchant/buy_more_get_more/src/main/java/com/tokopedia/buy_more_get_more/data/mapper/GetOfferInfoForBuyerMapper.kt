package com.tokopedia.buy_more_get_more.data.mapper

import com.tokopedia.buy_more_get_more.data.response.OfferInfoForBuyerResponse
import com.tokopedia.buy_more_get_more.data.response.OfferInfoForBuyerResponse.*
import com.tokopedia.buy_more_get_more.domain.entity.OfferInfoForBuyer
import com.tokopedia.buy_more_get_more.domain.entity.OfferInfoForBuyer.Offering.Tier
import com.tokopedia.buy_more_get_more.domain.entity.OfferInfoForBuyer.Offering.Tier.Rule
import javax.inject.Inject

class GetOfferInfoForBuyerMapper @Inject constructor() {

    fun map(response: OfferInfoForBuyerResponse): OfferInfoForBuyer {
        return response.let {
            OfferInfoForBuyer(
                responseHeader = response.responseHeader.toResponseHeaderModel(),
                offeringJsonData = response.offeringJsonData,
                offerings = response.offerings.toOfferingUiModel()
            )
        }
    }

    private fun ResponseHeader.toResponseHeaderModel(): OfferInfoForBuyer.ResponseHeader {
        return OfferInfoForBuyer.ResponseHeader(
            success = success,
            error_code = error_code,
            processTime = processTime
        )
    }

    private fun List<Offering>.toOfferingUiModel(): List<OfferInfoForBuyer.Offering> {
        return map {
            OfferInfoForBuyer.Offering(
                id = it.id,
                offerName = it.offerName,
                offerTypeId = it.offerTypeId,
                startDate = it.startDate,
                endDate = it.endDate,
                maxAppliedTier = it.maxAppliedTier,
                tierList = it.tierList.toTierListUiModel()
            )
        }
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
