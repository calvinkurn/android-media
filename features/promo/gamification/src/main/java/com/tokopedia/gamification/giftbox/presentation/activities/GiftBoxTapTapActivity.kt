package com.tokopedia.gamification.giftbox.presentation.activities

import androidx.fragment.app.Fragment
import com.tokopedia.gamification.giftbox.analytics.GtmGiftTapTap
import com.tokopedia.gamification.giftbox.presentation.fragments.GiftBoxTapTapFragment

class GiftBoxTapTapActivity : BaseGiftBoxActivity() {

    var fragment: GiftBoxTapTapFragment? = null
    override fun getDestinationFragment(): Fragment {
        fragment = GiftBoxTapTapFragment()
        return fragment!!
    }

    override fun onBackPressed() {
        GtmGiftTapTap.clickMainBackButton()
        if (fragment != null) {
            val canGoBack = fragment!!.onBackPressed()
            if (canGoBack) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }
}