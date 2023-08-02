package com.tokopedia.sessioncommon.tracker

import com.tokopedia.track.builder.Tracker

object OclTracker {
    
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3937
    // Tracker ID: 43359
    fun sendClickOnButtonSimpanLoginAccountEvent () {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button simpan login account")
            .setEventCategory("one click login")
            .setEventLabel("click")
            .setCustomProperty("trackerId", "43359")
            .setBusinessUnit(BUSSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3937
    // Tracker ID: 43360
    fun sendClickOnButtonNantiLoginAccountEvent () {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button nanti login account")
            .setEventCategory("one click login")
            .setEventLabel("")
            .setCustomProperty("trackerId", "43360")
            .setBusinessUnit(BUSSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3937
    // Tracker ID: 43361
    fun sendClickOnCloseButtonEvent () {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on close button")
            .setEventCategory("one click login")
            .setEventLabel("")
            .setCustomProperty("trackerId", "43361")
            .setBusinessUnit(BUSSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3937
    // Tracker ID: 43362
    fun sendClickOnOneClickLoginEvent (label: String) {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on one click login")
            .setEventCategory("one click login")
            .setEventLabel(label)
            .setCustomProperty("trackerId", "43362")
            .setBusinessUnit(BUSSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3937
    // Tracker ID: 43363
    fun sendClickOnButtonHapusEvent () {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button hapus")
            .setEventCategory("one click login")
            .setEventLabel("")
            .setCustomProperty("trackerId", "43363")
            .setBusinessUnit(BUSSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3937
    // Tracker ID: 43364
    fun sendClickOnMasukKeAkunLainEvent () {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on masuk ke akun lain")
            .setEventCategory("one click login")
            .setEventLabel("")
            .setCustomProperty("trackerId", "43364")
            .setBusinessUnit(BUSSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3937
    // Tracker ID: 43365
    fun sendClickOnButtonDaftarEvent () {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button daftar")
            .setEventCategory("one click login")
            .setEventLabel("")
            .setCustomProperty("trackerId", "43365")
            .setBusinessUnit(BUSSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3937
    // Tracker ID: 43375
    fun sendClickOnButtonHapusDialogEvent () {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button hapus")
            .setEventCategory("remove one click login")
            .setEventLabel("")
            .setCustomProperty("trackerId", "43375")
            .setBusinessUnit(BUSSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3937
    // Tracker ID: 43376
    fun sendClickOnButtonBatalEvent () {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button batal")
            .setEventCategory("remove one click login")
            .setEventLabel("")
            .setCustomProperty("trackerId", "43376")
            .setBusinessUnit(BUSSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    private const val BUSSINESS_UNIT = "user platform"
    private const val CURRENT_SITE = "tokopediamarketplace"

    const val LABEL_SUCCESS = "success"
    const val LABEL_FAILED = "failed"
    const val LABEL_CLICK = "click"
}
