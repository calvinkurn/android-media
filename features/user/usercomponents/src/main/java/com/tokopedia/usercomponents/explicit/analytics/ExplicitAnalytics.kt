package com.tokopedia.usercomponents.explicit.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class ExplicitAnalytics @Inject constructor() {

    fun trackClickCard(source: String, template: String, pagePath: String, pageType: String) {
        val data = TrackAppUtils.gtmData(
            EVENT_VIEW_ACCOUNT_IRIS,
            CATEGORY_EXPLICIT_WIDGET,
            concatVariable(ACTION_IMPRESSION, CATEGORY_EXPLICIT_WIDGET),
            concatVariable(source, template)
        )
        data.putAll(generateCommon(pagePath, pageType))

        sendData(data)
    }

    fun trackClickPositifButton(
        source: String,
        template: String,
        pagePath: String,
        pageType: String
    ) {
        val data = TrackAppUtils.gtmData(
            EVENT_VIEW_ACCOUNT,
            CATEGORY_EXPLICIT_WIDGET,
            ACTION_CLICK_ON_YES_BUTTON,
            concatVariable(source, template)
        )
        data.putAll(generateCommon(pagePath, pageType))

        sendData(data)
    }

    fun trackClickNegatifButton(
        source: String,
        template: String,
        pagePath: String,
        pageType: String
    ) {
        val data = TrackAppUtils.gtmData(
            EVENT_VIEW_ACCOUNT,
            CATEGORY_EXPLICIT_WIDGET,
            ACTION_CLICK_ON_NO_BUTTON,
            concatVariable(source, template)
        )
        data.putAll(generateCommon(pagePath, pageType))

        sendData(data)
    }

    fun trackClickDismissButton(
        source: String,
        template: String,
        pagePath: String,
        pageType: String
    ) {
        val data = TrackAppUtils.gtmData(
            EVENT_VIEW_ACCOUNT,
            CATEGORY_EXPLICIT_WIDGET,
            ACTION_CLICK_ON_CLOSE_BUTTON,
            concatVariable(source, template)
        )
        data.putAll(generateCommon(pagePath, pageType))

        sendData(data)
    }

    private fun generateCommon(pagePath: String, pageType: String): Map<String, String> {
        return mapOf(
            KEY_BUSINESS_UNIT to VALUE_BUSINESS_UNIT,
            KEY_CURRENT_SITE to VALUE_CURRENT_SITE,
            KEY_PAGE_PATH to pagePath,
            KEY_PAGE_TYPE to pageType
        )
    }

    private fun concatVariable(variable1: String, variable2: String): String {
        return "$variable1 - $variable2"
    }

    private fun sendData(data: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    companion object {
        private const val KEY_PAGE_PATH = "pagePath"
        private const val KEY_PAGE_TYPE = "pageType"
        private const val KEY_BUSINESS_UNIT = "businessUnit"
        private const val KEY_CURRENT_SITE = "currentSite"
        private const val VALUE_BUSINESS_UNIT = "user platform"
        private const val VALUE_CURRENT_SITE = "tokopediamarketplace"

        private const val EVENT_VIEW_ACCOUNT_IRIS = "viewAccountIris"
        private const val EVENT_VIEW_ACCOUNT = "viewAccount"

        private const val ACTION_IMPRESSION = "impression"
        private const val ACTION_CLICK_ON_YES_BUTTON = "click on yes button"
        private const val ACTION_CLICK_ON_NO_BUTTON = "click on no button"
        private const val ACTION_CLICK_ON_CLOSE_BUTTON = "click on close button"

        private const val CATEGORY_EXPLICIT_WIDGET = "explicit widget"
    }
}