package com.tokopedia.usercomponents.explicit.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class ExplicitAnalytics @Inject constructor() {

    fun trackClickCard(source: String, template: String){
        val data = TrackAppUtils.gtmData(
            EVENT_VIEW_ACCOUNT_IRIS,
            CATEGORY_EXPLICIT_WIDGET,
            ACTION_EXPLICIT_WIDGET_BOX,
            generateLabel(source, template)
        )
        data.putAll(generateCommon())

        sendData(data)
    }

    fun trackClickPositifButton(source: String, template: String){
        val data = TrackAppUtils.gtmData(
            EVENT_VIEW_ACCOUNT,
            CATEGORY_EXPLICIT_WIDGET,
            ACTION_CLICK_ON_YES_BUTTON,
            generateLabel(source, template)
        )
        data.putAll(generateCommon())

        sendData(data)
    }

    fun trackClickNegatifButton(source: String, template: String){
        val data = TrackAppUtils.gtmData(
            EVENT_VIEW_ACCOUNT,
            CATEGORY_EXPLICIT_WIDGET,
            ACTION_CLICK_ON_NO_BUTTON,
            generateLabel(source, template)
        )
        data.putAll(generateCommon())

        sendData(data)
    }

    fun trackClickDismissButton(source: String, template: String){
        val data = TrackAppUtils.gtmData(
            EVENT_VIEW_ACCOUNT,
            CATEGORY_EXPLICIT_WIDGET,
            ACTION_CLICK_ON_CLOSE_BUTTON,
            generateLabel(source, template)
        )
        data.putAll(generateCommon())

        sendData(data)
    }

    private fun generateCommon(): Map<String, String> {
        return mapOf(
            KEY_BUSINESS_UNIT to VALUE_BUSINESS_UNIT,
            KEY_CURRENT_SITE to VALUE_CURRENT_SITE
        )
    }

    private fun generateLabel(source: String, template: String): String {
        return "$source - $template"
    }

    private fun sendData(data: Map<String, Any>){
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    companion object {
        private const val KEY_BUSINESS_UNIT = "businessUnit"
        private const val KEY_CURRENT_SITE = "currentSite"
        private const val VALUE_BUSINESS_UNIT = "user platform"
        private const val VALUE_CURRENT_SITE = "tokopediamarketplace"

        private const val EVENT_VIEW_ACCOUNT_IRIS = "viewAccountIris"
        private const val EVENT_VIEW_ACCOUNT = "viewAccount"

        private const val ACTION_EXPLICIT_WIDGET_BOX = "explicit widget box"
        private const val ACTION_CLICK_ON_YES_BUTTON = "click on yes button"
        private const val ACTION_CLICK_ON_NO_BUTTON = "click on no button"
        private const val ACTION_CLICK_ON_CLOSE_BUTTON = "click on close button"

        private const val CATEGORY_EXPLICIT_WIDGET = "explicit widget"
    }
}