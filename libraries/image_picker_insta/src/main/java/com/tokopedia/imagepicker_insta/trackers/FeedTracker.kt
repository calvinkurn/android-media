package com.tokopedia.imagepicker_insta.trackers

class FeedTracker(val shopId: String) : TrackerContract {

    override fun onNextButtonClick() {
        val map = mutableMapOf<String, Any>()
        map["event"] = "clickFeed"
        map["eventAction"] = "click next on picker page"
        map["eventCategory"] = "content feed creation"
        map["eventLabel"] = shopId
        map["businessUnit"] = "content"
        map["currentSite"] = "tokopediamarketplace"
        map["sessionIris"] = "sessioniris"
        getTracker().sendGeneralEvent(map)
    }

    override fun onBackButtonFromPicker() {
        val map = mutableMapOf<String, Any>()
        map["event"] = "clickFeed"
        map["eventAction"] = "click back on picker page"
        map["eventCategory"] = "content feed creation"
        map["eventLabel"] = shopId
        map["businessUnit"] = "content"
        map["currentSite"] = "tokopediamarketplace"
        map["sessionIris"] = "sessioniris"
        getTracker().sendGeneralEvent(map)
    }

    override fun onCameraButtonFromPickerClick() {
        val map = mutableMapOf<String, Any>()
        map["event"] = "clickFeed"
        map["eventAction"] = "click camera on picker pag"
        map["eventCategory"] = "content feed creation"
        map["eventLabel"] = shopId
        map["businessUnit"] = "content"
        map["currentSite"] = "tokopediamarketplace"
        map["sessionIris"] = "sessioniris"
        getTracker().sendGeneralEvent(map)
    }

    override fun onRecordButtonClick() {
        val map = mutableMapOf<String, Any>()
        map["event"] = "clickFeed"
        map["eventAction"] = "click circle on record page"
        map["eventCategory"] = "content feed creation"
        map["eventLabel"] = shopId
        map["businessUnit"] = "content"
        map["currentSite"] = "tokopediamarketplace"
        map["sessionIris"] = "sessioniris"
        getTracker().sendGeneralEvent(map)
    }
}