package com.tokopedia.gamification.giftbox.domain

import com.tokopedia.gamification.giftbox.data.di.GIFT_BOX_DAILY_REWARD
import javax.inject.Inject
import javax.inject.Named

class GiftBoxDailyRewardUseCase @Inject constructor(@Named(GIFT_BOX_DAILY_REWARD) val queryString: String) {

    fun getData() {}
}