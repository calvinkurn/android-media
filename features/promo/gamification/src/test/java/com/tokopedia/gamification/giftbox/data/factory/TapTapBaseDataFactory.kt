package com.tokopedia.gamification.giftbox.data.factory

import com.tokopedia.gamification.data.entity.CrackBenefitEntity
import com.tokopedia.gamification.data.entity.CrackResultEntity
import com.tokopedia.gamification.data.entity.ResponseCrackResultEntity
import com.tokopedia.gamification.giftbox.data.entities.Benefits
import com.tokopedia.gamification.giftbox.data.entities.CouponDetailResponse
import com.tokopedia.gamification.giftbox.data.entities.GameRemindMe
import com.tokopedia.gamification.giftbox.data.entities.GamiCrack
import com.tokopedia.gamification.giftbox.data.entities.GetCouponDetail
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxRewardEntity
import com.tokopedia.gamification.giftbox.data.entities.Recommendation
import com.tokopedia.gamification.giftbox.data.entities.RemindMeEntity
import com.tokopedia.gamification.giftbox.data.entities.ResultStatus
import com.tokopedia.gamification.giftbox.presentation.fragments.DisplayType
import com.tokopedia.gamification.taptap.data.entiity.ActionButton
import com.tokopedia.gamification.taptap.data.entiity.GamiTapEggHome
import com.tokopedia.gamification.taptap.data.entiity.TapTapBaseEntity

object TapTapBaseDataFactory {
    fun createTapTapBaseEntity() = TapTapBaseEntity(
        GamiTapEggHome(
            actionButton = arrayListOf(
                ActionButton(
                    applink = "tokopedia://giftbox/button1",
                    backgroundColor = "#FF1234",
                    isDisable = false,
                    text = "Action 1",
                    type = "type",
                    url = "https://tokopedia.com/giftbox/button1"
                ),
                ActionButton(
                    applink = "tokopedia://giftbox/button2",
                    backgroundColor = "#FE1231",
                    isDisable = true,
                    text = "Action 2",
                    type = "type",
                    url = "https://tokopedia.com/giftbox/button2"
                )
            )
        )
    )

    fun createTapTapCrackEntity() = ResponseCrackResultEntity(
        crackResultEntity = CrackResultEntity(
            campaignId = 221231,
            imageUrl = "https://images.tokopedia.com",
            benefits = listOf(
                CrackBenefitEntity(
                    color = "#12123",
                    size = "12",
                    templateText = "images",
                    benefitType = "type"
                )
            ),
            benefitType = "type",
            benefitLabel = "good type"
        )
    )

    fun createCouponDetailResponse(referenceId: String?) = CouponDetailResponse(
        couponDetailList = listOf(
            GetCouponDetail(
                minimumUsageLabel = "23",
                minimumUsage = "12",
                icon = "https://images.tokopedia.com/icon",
                imageUrl = "https://images.tokopedia.com/image",
                referenceId = referenceId,
            )
        )
    )

    fun createRemindMeEntity(isReminder: Boolean) = RemindMeEntity(
        GameRemindMe(
            resultStatus = ResultStatus(
                code = "200",
                message = arrayListOf("message"),
                reason = ""
            ),
            requestToSetReminder = isReminder
        )
    )

    fun createGiftBoxRewardEntity(
        referenceId: String?,
        displayType: String = DisplayType.UNDEFINED,
        benefits: List<Benefits>? = listOf
        (
            Benefits(
                color = "#12123",
                text = "you got benefit",
                isBigPrize = false,
                isAutoApply = false,
                autoApplyMsg = "auto",
                dummyCode = "#112FFF",
                benefitType = null,
                displayType = displayType,
                imageUrl = null,
                referenceID = referenceId
            )
        )
    ) = GiftBoxRewardEntity(
        gamiCrack = GamiCrack(
            resultStatus = ResultStatus(
                code = "200",
                message = listOf("success"),
                reason = "no reason"
            ),
            imageUrl = "https://images.tokopedia.com",
            benefitText = listOf(),
            benefits = benefits,
            actionButton = listOf(),
            recommendation = Recommendation(
                isShow = false,
                shopId = "1233312",
                pageName = "home"
            )
        )
    )
}
