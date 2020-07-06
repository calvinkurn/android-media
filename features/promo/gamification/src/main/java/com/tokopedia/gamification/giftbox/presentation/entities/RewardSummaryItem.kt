package com.tokopedia.gamification.giftbox.presentation.entities

import com.tokopedia.gamification.data.entity.CrackBenefitEntity
import com.tokopedia.gamification.data.entity.CrackButtonEntity
import com.tokopedia.gamification.giftbox.data.entities.GetCouponDetail

data class RewardSummaryItem(val couponDetail: GetCouponDetail?, val benfit: CrackBenefitEntity, val crackButtonEntity: CrackButtonEntity? = null)