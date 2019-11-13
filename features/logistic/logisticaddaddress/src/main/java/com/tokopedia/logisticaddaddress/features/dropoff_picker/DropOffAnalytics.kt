package com.tokopedia.logisticaddaddress.features.dropoff_picker

import com.tokopedia.logisticaddaddress.features.dropoff_picker.model.DropoffNearbyModel
import com.tokopedia.logisticaddaddress.utils.getDescription
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class DropOffAnalytics @Inject constructor(){

    private fun sendTracker(event: String, eventCategory: String, eventAction: String,
                            eventLabel: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                event, eventCategory, eventAction, eventLabel))
    }


    private fun sendTracker(event: String, eventCategory: String, eventAction: String) {
        sendTracker(event, eventCategory, eventAction, "")
    }

    fun trackUserClickNantiSaja() {
        sendTracker(Event.CLICK_TRADE_IN,
                Category.LOCATION_SELECTION,
                Action.CLICK_NANTI_SAJA)
    }

    fun trackUserClickIzinkan() {
        sendTracker(Event.CLICK_TRADE_IN,
                Category.LOCATION_SELECTION,
                Action.CLICK_IZINKAN)
    }

    fun trackClickSearchBarGpsOff(keyword: String) {
        sendTracker(Event.CLICK_TRADE_IN,
                Category.LOCATION_SELECTION,
                Action.CLICK_SEARCHBAR_GPS_OFF,
                keyword)
    }

    fun trackClickActivateGps() {
        sendTracker(Event.CLICK_TRADE_IN,
                Category.LOCATION_SELECTION,
                Action.CLICK_AKTIFKAN_GPS)
    }

    fun trackSelectStoreListFirst(addressTitle: String, addressValue: String, storeType: String) {
        val label = "$addressTitle - $addressValue - $storeType"
        sendTracker(Event.CLICK_TRADE_IN,
                Category.LOCATION_SELECTION,
                Action.SELECT_STORE_LIST_NEAREST,
                label)
    }

    fun trackExpandList() {
        sendTracker(Event.CLICK_TRADE_IN,
                Category.LOCATION_SELECTION,
                Action.EXPAND_LIST)
    }

    fun trackClickMap() {
        sendTracker(Event.CLICK_TRADE_IN,
                Category.LOCATION_SELECTION,
                Action.CLICK_MAP)
    }

    fun trackClickSearchBarGpsOn(keyword: String) {
        sendTracker(Event.CLICK_TRADE_IN,
                Category.LOCATION_SELECTION,
                Action.CLICK_SEARCHBAR_GPS_ON,
                keyword)
    }

    fun trackSelectStoreListAll(addressTitle: String, addressValue: String, storeType: String) {
        val label = "$addressTitle - $addressValue - $storeType"
        sendTracker(Event.CLICK_TRADE_IN,
                Category.LOCATION_SELECTION,
                Action.SELECT_STORE_LIST_ALL,
                label)
    }

    fun trackSelectIndoMaretMap(model: DropoffNearbyModel) {
        val label = "${model.addrName} - ${model.getDescription()} - ${model.type}"
        sendTracker(Event.CLICK_TRADE_IN,
                Category.LOCATION_SELECTION,
                Action.SELECT_INDOMARET_MAP,
                label)
    }

    fun trackClickBatalOnDetail(model: DropoffNearbyModel) {
        val label = "${model.addrName} - ${model.getDescription()} - ${model.type}"
        sendTracker(Event.CLICK_TRADE_IN,
                Category.LOCATION_SELECTION,
                Action.CLICK_BATAL_DETAIL,
                label)
    }

    fun trackClickPilihOnDetail(model: DropoffNearbyModel) {
        val label = "${model.addrName} - ${model.getDescription()} - ${model.type}"
        sendTracker(Event.CLICK_TRADE_IN,
                Category.LOCATION_SELECTION,
                Action.CLICK_PILIH_DETAIL,
                label)
    }

    fun trackSearchKeyword(keyword: String) {
        sendTracker(Event.CLICK_TRADE_IN,
                Category.LOCATION_SELECTION,
                Action.SEARCH_KEYWORD,
                keyword)
    }

    fun trackSelectLandmarkFromKeyword(keyword: String, addressTitle: String, addressValue: String) {
        val label = "$keyword - $addressTitle - $addressValue"
        sendTracker(Event.CLICK_TRADE_IN,
                Category.LOCATION_SELECTION,
                Action.SELECT_LANDMARK_FROM_KEYWORD,
                label)
    }
}

private object Event {
    const val CLICK_TRADE_IN = "clickTradeIn"
}

private object Category {
    const val LOCATION_SELECTION = "click - nanti saja button on gps pop up"
}

private object Action {
    const val CLICK_NANTI_SAJA = "click - nanti saja button on gps pop up"
    const val CLICK_IZINKAN = "click - izinkan button on gps pop up"
    const val CLICK_SEARCHBAR_GPS_OFF = "click - search bar - gps inactive"
    const val CLICK_SEARCHBAR_GPS_ON = "click - search bar - gps active"
    const val CLICK_AKTIFKAN_GPS = "click - aktifkan gps button"
    const val SELECT_STORE_LIST_NEAREST = "click - select indomart from list - nearest"
    const val SELECT_STORE_LIST_ALL = "click - select indomart from list - show all"
    const val CLICK_MAP = "click - map"
    const val EXPAND_LIST = "click - expand list button"
    const val SELECT_INDOMARET_MAP = "click - select indomart from map"
    const val CLICK_BATAL_DETAIL = "click - batal button on select indomart from map"
    const val CLICK_PILIH_DETAIL = "click - pilih button on select indomart from map"
    const val SEARCH_KEYWORD = "click - search landmark from keyword"
    const val SELECT_LANDMARK_FROM_KEYWORD = "click - select landmark indomart from keyword"
}