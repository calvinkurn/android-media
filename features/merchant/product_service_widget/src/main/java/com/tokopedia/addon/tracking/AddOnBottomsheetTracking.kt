package com.tokopedia.addon.tracking

import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AddOnBottomsheetTracking @Inject constructor(
    private val userSession: UserSessionInterface
): BaseTrackerConst() {

    companion object {
        private const val DELIMITER_VALUE = ","
        private const val DELIMITER_LABEL_VALUE = " - "

        private const val EVENT = "clickPG"
        private const val ACTION_SELECT = "addon option"
        private const val ACTION_SAVE = "simpan option"
        private const val ACTION_CLOSE = "close option"
        private const val CATEGORY = "addon"
        private const val BUSINESS_UNIT = "physical goods"
    }

    private fun generateEventLabel(
        addOnGroupUIModels: List<AddOnGroupUIModel>,
        cartId: String,
        pageSource: String
    ): String {
        val selectedAddOn = addOnGroupUIModels.flatMap {
            it.addon
        }.filter {
            it.isSelected
        }
        val shopId = selectedAddOn.firstOrNull()?.shopId.orEmpty()
        val productId = addOnGroupUIModels.firstOrNull()?.productId.orZero().toString()
        val addonId = selectedAddOn.joinToString(DELIMITER_VALUE) { it.id }
        val addonType = selectedAddOn.joinToString(DELIMITER_VALUE) { it.addOnType.toString() }
        val customerId = userSession.userId
        return listOf(pageSource, shopId, productId, addonId, addonType, customerId, cartId)
            .joinToString(DELIMITER_LABEL_VALUE)
    }

    fun sendClickAddonOptionEvent(
        addOnGroupUIModels: List<AddOnGroupUIModel>,
        cartId: String,
        pageSource: String
    ) {
        val eventLabel = generateEventLabel(addOnGroupUIModels, cartId, pageSource)
        sendClickAddonOptionEvent(eventLabel)
    }

    fun sendClickSimpanAddonEvent(
        addOnGroupUIModels: List<AddOnGroupUIModel>,
        cartId: String,
        pageSource: String
    ) {
        val eventLabel = generateEventLabel(addOnGroupUIModels, cartId, pageSource)
        sendClickSimpanAddonEvent(eventLabel)
    }

    fun sendClickCloseBottomsheetEvent(
        addOnGroupUIModels: List<AddOnGroupUIModel>,
        cartId: String,
        pageSource: String
    ) {
        val eventLabel = generateEventLabel(addOnGroupUIModels, cartId, pageSource)
        sendClickCloseBottomsheetEvent(eventLabel)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4031
    // Tracker ID: 44718
    fun sendClickAddonOptionEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction(listOf(Event.CLICK, ACTION_SELECT).joinToString(DELIMITER_LABEL_VALUE))
            .setEventCategory(CATEGORY)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerId.KEY, "44718")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CurrentSite.DEFAULT)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4031
    // Tracker ID: 44719
    fun sendClickSimpanAddonEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction(listOf(Event.CLICK, ACTION_SAVE).joinToString(DELIMITER_LABEL_VALUE))
            .setEventCategory(CATEGORY)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerId.KEY, "44719")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CurrentSite.DEFAULT)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4031
    // Tracker ID: 44720
    fun sendClickCloseBottomsheetEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction(listOf(Event.CLICK, ACTION_CLOSE).joinToString(DELIMITER_LABEL_VALUE))
            .setEventCategory(CATEGORY)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerId.KEY, "44720")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CurrentSite.DEFAULT)
            .build()
            .send()
    }
}
