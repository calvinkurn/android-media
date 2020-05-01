package com.tokopedia.gamification.giftbox.presentation.activities

import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.presentation.fragments.GiftBoxTapTapFragment

class GiftBoxTapTapActivity : BaseGiftBoxActivity() {

    override fun getLayout() = R.layout.activity_gift_box_daily
    override fun getDestinationFragment() = GiftBoxTapTapFragment()

}