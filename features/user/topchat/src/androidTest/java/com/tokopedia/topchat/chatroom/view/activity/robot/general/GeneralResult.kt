package com.tokopedia.topchat.chatroom.view.activity.robot.general

import android.content.Intent
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData

object GeneralResult {

    fun openPageWithApplink(applink: String) {
        intended(hasData(applink))
    }

    fun openPageWithIntent(intent: Intent) {
        intended(hasData(intent.data))
    }
}