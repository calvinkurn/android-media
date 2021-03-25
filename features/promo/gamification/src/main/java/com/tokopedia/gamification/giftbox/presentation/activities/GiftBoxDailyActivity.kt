package com.tokopedia.gamification.giftbox.presentation.activities

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.gamification.giftbox.analytics.GtmEvents
import com.tokopedia.gamification.giftbox.presentation.fragments.GiftBoxDailyFragment

class GiftBoxDailyActivity : BaseGiftBoxActivity() {
    var fragment: GiftBoxDailyFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sendOpenScreenEvent()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        sendOpenScreenEvent()
    }

    private fun sendOpenScreenEvent(){
        GtmEvents.openDailyGiftBoxPage(userSession.userId)
    }
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
}