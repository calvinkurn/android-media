package com.tokopedia.gamification.giftbox.presentation.entities

import com.tokopedia.gamification.giftbox.data.entities.GetCouponDetail

data class RewardSummaryItem(val couponDetail: GetCouponDetail?, val simpleReward: SimpleReward?)
data class SimpleReward(val imageUrl: String, val text: String)