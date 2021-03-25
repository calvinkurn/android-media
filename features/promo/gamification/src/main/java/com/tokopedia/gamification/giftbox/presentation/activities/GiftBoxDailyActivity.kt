package com.tokopedia.gamification.giftbox.presentation.activities

import androidx.fragment.app.Fragment
import com.tokopedia.gamification.giftbox.analytics.GtmEvents
import com.tokopedia.gamification.giftbox.presentation.fragments.GiftBoxDailyFragment

class GiftBoxDailyActivity : BaseGiftBoxActivity() {
    var fragment: GiftBoxDailyFragment? = null

    override fun getDestinationFragment(): Fragment {
        fragment = GiftBoxDailyFragment()
        return fragment!!
    }

    override fun onBackPressed() {
        if (fragment != null) {
            val canGoBack = fragment!!.onBackPressed()
            if (canGoBack) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun sendScreenAnalytics() {
        GtmEvents.openDailyGiftBoxPage(userSession.userId)
    }
}