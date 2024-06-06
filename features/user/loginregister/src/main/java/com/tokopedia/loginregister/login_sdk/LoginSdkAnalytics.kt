package com.tokopedia.loginregister.login_sdk

import com.tokopedia.track.builder.Tracker

object LoginSdkAnalytics {

    private const val BUSSINESS_UNIT = "user platform"
    private const val CURRENT_SITE = "tokopediamarketplace"

    const val LABEL_SUCCESS = "success"
    const val LABEL_FAILED = "failed"


    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4572
    // Tracker ID: 50774
    fun sendViewTokopediaSsoPageEvent (eventLabel: String) {
        Tracker.Builder()
            .setEvent("viewAccountIris")
            .setEventAction("view tokopedia sso page")
            .setEventCategory("tokopedia sso page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "36196")
            .setBusinessUnit(BUSSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4572
    // Tracker ID: 50775
    fun sendClickOnButtonBackEvent (eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button back")
            .setEventCategory("tokopedia sso page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "36197")
            .setBusinessUnit(BUSSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4572
    // Tracker ID: 50776
    fun sendClickOnButtonSyaratDanKetentuanTokopediaEvent() {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button syarat dan ketentuan tokopedia")
            .setEventCategory("tokopedia sso page")
            .setEventLabel("")
            .setCustomProperty("trackerId", "36198")
            .setBusinessUnit(BUSSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4572
    // Tracker ID: 50777
    fun sendClickOnButtonKebijakanPrivasiTokopediaEvent () {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button kebijakan privasi tokopedia")
            .setEventCategory("tokopedia sso page")
            .setEventLabel("")
            .setCustomProperty("trackerId", "36199")
            .setBusinessUnit(BUSSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4572
    // Tracker ID: 50778
    fun sendClickOnButtonSyaratDanKetentuanEvent (eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button syarat dan ketentuan")
            .setEventCategory("tokopedia sso page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "36200")
            .setBusinessUnit(BUSSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4572
    // Tracker ID: 50779
    fun sendClickOnButtonKebijakanPrivasiEvent (eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button kebijakan privasi")
            .setEventCategory("tokopedia sso page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "36201")
            .setBusinessUnit(BUSSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4572
    // Tracker ID: 50780
    fun sendClickOnButtonLanjutDanIzinkanEvent (eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button lanjut dan izinkan")
            .setEventCategory("tokopedia sso page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "36202")
            .setBusinessUnit(BUSSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    const val LABEL_CLICK = "click"
}
