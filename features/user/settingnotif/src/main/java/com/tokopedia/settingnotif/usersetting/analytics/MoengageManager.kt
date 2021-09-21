package com.tokopedia.settingnotif.usersetting.analytics

import com.tokopedia.track.TrackApp

interface MoengageManager {
    fun setNewsletterEmailPref(value: Boolean)
    fun setPushPreference(value: Boolean)
}

open class MoengageManagerImpl : MoengageManager {

    override fun setNewsletterEmailPref(value: Boolean) {
        TrackApp.getInstance().moEngage.setNewsletterEmailPref(value)
    }

    override fun setPushPreference(value: Boolean) {
        TrackApp.getInstance().moEngage.setPushPreference(value)
    }

}