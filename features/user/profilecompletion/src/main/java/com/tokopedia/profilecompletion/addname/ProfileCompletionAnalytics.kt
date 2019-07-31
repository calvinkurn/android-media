package com.tokopedia.profilecompletion.addname

import com.tokopedia.track.TrackApp
import javax.inject.Inject

/**
 * @author by nisie on 30/04/19.
 */
class ProfileCompletionAnalytics @Inject constructor() {

    companion object {

    }

    fun trackSuccessRegisterPhoneNumber(userId: String) {
        try {
            TrackApp.getInstance().appsFlyer.sendAppsflyerRegisterEvent(
                    userId,
                    "Phone Number")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}