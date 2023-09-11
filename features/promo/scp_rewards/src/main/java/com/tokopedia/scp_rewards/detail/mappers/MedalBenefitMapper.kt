package com.tokopedia.scp_rewards.detail.mappers

import com.tokopedia.scp_rewards.detail.domain.model.Category
import com.tokopedia.scp_rewards.detail.domain.model.Cta
import com.tokopedia.scp_rewards.detail.domain.model.MedaliBenefit
import com.tokopedia.scp_rewards_widgets.common.model.CtaButton
import com.tokopedia.scp_rewards_widgets.model.FilterModel
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel

object MedalBenefitMapper {

    fun mapBenefitApiResponseToBenefitModelList(
        list: List<MedaliBenefit>?
    ): List<MedalBenefitModel>? {
        return list?.map {
            MedalBenefitModel(
                title = it.title,
                isActive = it.isActive,
                status = it.status,
                url = it.url,
                appLink = it.appLink,
                tncList = it.tncList?.map { tnc -> tnc.text.orEmpty() },
                medaliImageURL = it.medaliImageURL,
                podiumImageURL = it.podiumImageURL,
                backgroundImageURL = it.backgroundImageURL,
                statusBadgeText = it.statusInfo?.text,
                statusBadgeColor = it.statusInfo?.backgroundColor,
                statusDescription = it.statusDescription,
                additionalInfoText = it.info?.text,
                additionalInfoColor = it.info?.backgroundColor,
                typeImageURL = it.benefitType?.iconImageURL,
                typeBackgroundColor = it.benefitType?.backgroundColor,
                cta = CtaButton(
                    it.benefitCTA?.unifiedStyle,
                    it.benefitCTA?.text,
                    it.benefitCTA?.appLink,
                    it.benefitCTA?.url,
                    it.benefitCTA?.isAutoApply,
                    it.benefitCTA?.couponCode
                ),
                categoryIds = it.categoryIdList
            )
        }
    }

    fun mapBenefitApiResponseCtaToCta(cta: Cta?) = com.tokopedia.scp_rewards_widgets.medal.Cta(
        text = cta?.text,
        appLink = cta?.appLink,
        deepLink = cta?.url,
        isShown = true,
        style = cta?.unifiedStyle
    )

    fun mapCategoryApiResponseFilter(list: List<Category>?): List<FilterModel>? {
        return list?.map {
            FilterModel(
                id = it.id,
                text = it.text,
                iconImageURL = it.iconImageURL
            )
        }
    }
}
