package com.tokopedia.gamification.giftbox.presentation.activities

import com.tokopedia.gamification.giftbox.presentation.fragments.GiftBoxTapTapFragment

class GiftBoxTapTapActivity : BaseGiftBoxActivity() {

    override fun getLayout() = com.tokopedia.gamification.R.layout.activity_gift_box_daily
    override fun getDestinationFragment() = GiftBoxTapTapFragment()

}