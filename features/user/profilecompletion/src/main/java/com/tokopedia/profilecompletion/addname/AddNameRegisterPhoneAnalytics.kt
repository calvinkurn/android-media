package com.tokopedia.profilecompletion.addname

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

/**
 * @author by nisie on 30/04/19.
 * https://docs.google.com/spreadsheets/d/1CBXovkdWu7NMkxrHIOJihMyfuRWNZvxgJd36KxLS25I/edit#gid=2108301255
 */
class AddNameRegisterPhoneAnalytics @Inject constructor() {

    companion object {

    }

    fun trackSuccessRegisterPhoneNumber(userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                "clickRegister",
                "register with phone number page",
                "click on button selesai",
                "success"
        ))

        try {
            TrackApp.getInstance().appsFlyer.sendAppsflyerRegisterEvent(
                    userId,
                    "Phone Number")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun trackClickFinishAddNameButton() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                "clickRegister",
                "register with phone number page",
                "click on button selesai",
                "click"
        ))
    }

    fun trackErrorFinishAddNameButton(errorMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                "clickRegister",
                "register with phone number page",
                "click on button selesai",
                String.format("failed - %s", errorMessage)
        ))
    }
}