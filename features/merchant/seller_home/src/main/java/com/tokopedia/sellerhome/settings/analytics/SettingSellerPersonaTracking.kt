package com.tokopedia.sellerhome.settings.analytics

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.track.builder.Tracker

/**
 * Created by @ilhamsuaib on 11/02/23.
 * data tracker : https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3630
 */

object SettingSellerPersonaTracking {

    private const val SETTINGS_CLICK_SELLER_PERSONA = "settings - click seller persona"
    private const val OTHER_TAB = "others tab"
    private const val TRACKER_ID = "trackerId"
    private const val BUSINESS_UNIT = "Physical Goods"
    private const val TOKOPEDIA_MARKET_PLACE = "tokopediamarketplace"
    private const val CLICK_PG = "clickPG"

    fun sendSettingsClickSellerPersonaEvent() {
        println("Tracker : sendSettingsClickSellerPersonaEvent")
        Tracker.Builder()
            .setEvent(CLICK_PG)
            .setEventAction(SETTINGS_CLICK_SELLER_PERSONA)
            .setEventCategory(OTHER_TAB)
            .setEventLabel(String.EMPTY)
            .setCustomProperty(TRACKER_ID, "40032")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }
}