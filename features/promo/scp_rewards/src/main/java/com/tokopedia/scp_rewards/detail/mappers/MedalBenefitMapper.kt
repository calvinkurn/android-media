package com.tokopedia.scp_rewards.detail.mappers

import com.tokopedia.scp_rewards.detail.domain.model.MedaliBenefitList
import com.tokopedia.scp_rewards_widgets.common.model.CtaButton
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel


object MedalBenefitMapper {

    fun mapBenefitApiResponseToBenefitModelList(
        data: MedaliBenefitList?
    ): List<MedalBenefitModel>? {
        return data?.benefitList?.map {
            MedalBenefitModel(
                title = it.title,
                isActive = it.isActive,
                status = it.status,
                appLink = it.appLink,
                tncList = it.tncList?.map { tnc -> tnc.text.orEmpty() },
                medaliImageURL = it.medaliImageURL,
                podiumImageURL = it.podiumImageURL,
                backgroundImageURL = it.backgroundImageURL,
                statusBadgeText = it.statusInfo?.text,
                statusBadgeColor = it.statusInfo?.backgroundColor,
                statusDescription = it.statusDescription,
                expiryCounter = it.expiryCounter,
                additionalInfoText = it.info?.text,
                additionalInfoColor = it.info?.backgroundColor,
                typeImageURL = it.benefitType?.iconImageURL,
                typeBackgroundColor = it.benefitType?.backgroundColor,
                cta = CtaButton(
                    it.benefitCTA?.unifiedStyle, it.benefitCTA?.text, it.benefitCTA?.appLink,
                    it.benefitCTA?.isAutoApply, it.benefitCTA?.couponCode
                )
            )
        }
    }
}
