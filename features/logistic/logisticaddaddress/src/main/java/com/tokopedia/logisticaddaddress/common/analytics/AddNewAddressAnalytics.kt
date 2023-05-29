package com.tokopedia.logisticaddaddress.common.analytics

import com.tokopedia.logisticaddaddress.common.AddressConstants.LOGISTIC_LABEL
import com.tokopedia.logisticaddaddress.common.AddressConstants.NON_LOGISTIC_LABEL
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by fwidjaja on 2019-06-19.
 */
object AddNewAddressAnalytics {

    private const val CLICK_ADDRESS = "clickAddress"
    private const val CART_CHANGE_ADDRESS_NEGATIVE = "cart change address negative"
    private const val CLICK_CHIPS_KOTA_KECAMATAN = "click chips kota/kecamatan"
    private const val CLICK_SUGGESTION_KOTA_KECAMATAN = "click suggestion kota/kecamatan"
    private const val CLICK_BACK_ARROW_ON_NEGATIVE_PAGE = "click back arrow on negative page"

    private fun sendEventCategoryActionLabel(
        event: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                event,
                eventCategory,
                eventAction,
                eventLabel
            )
        )
    }

    fun eventClickChipsKotaKecamatanChangeAddressNegative(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(
            CLICK_ADDRESS,
            CART_CHANGE_ADDRESS_NEGATIVE,
            CLICK_CHIPS_KOTA_KECAMATAN,
            getAnalyticsLabel(isFullFlow, isLogisticLabel)
        )
    }

    fun eventClickSuggestionKotaKecamatanChangeAddressNegative(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(
            CLICK_ADDRESS,
            CART_CHANGE_ADDRESS_NEGATIVE,
            CLICK_SUGGESTION_KOTA_KECAMATAN,
            getAnalyticsLabel(isFullFlow, isLogisticLabel)
        )
    }

    fun eventClickBackArrowOnNegativePage(isFullFlow: Boolean, isLogisticLabel: Boolean) {
        sendEventCategoryActionLabel(
            CLICK_ADDRESS,
            CART_CHANGE_ADDRESS_NEGATIVE,
            CLICK_BACK_ARROW_ON_NEGATIVE_PAGE,
            getAnalyticsLabel(isFullFlow, isLogisticLabel)
        )
    }

    private fun getAnalyticsLabel(flow: Boolean, logistic: Boolean) = if (flow && logistic) LOGISTIC_LABEL else NON_LOGISTIC_LABEL
}
