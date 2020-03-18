package com.tokopedia.gamification.giftbox.domain

import com.tokopedia.gamification.giftbox.data.di.GIFT_BOX_DAILY
import javax.inject.Inject
import javax.inject.Named

class GiftBoxDailyUseCase @Inject constructor(@Named(GIFT_BOX_DAILY) val queryString: String) {

    fun getData(){}

}