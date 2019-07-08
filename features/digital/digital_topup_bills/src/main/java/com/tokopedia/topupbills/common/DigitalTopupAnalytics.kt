package com.tokopedia.topupbills.common

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils


/**
 * @author by resakemal on 05/07/19.
 */

class DigitalTopupAnalytics {

    fun eventInputNumberManual(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.INPUT_MANUAL_NUMBER,
                "$categoryId - $operatorName"
        ))
    }

    fun eventInputNumberContact(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.INPUT_FROM_CONTACT,
                "$categoryId - $operatorName"
        ))
    }

    fun eventInputNumberWidget(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.INPUT_FROM_WIDGET,
                "$categoryId - $operatorName"
        ))
    }

    fun eventInputNumberFavorites(categoryId: Int, operatorName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.INPUT_FROM_FAVORITE_NUMBER,
                "$categoryId - $operatorName"
        ))
    }

    fun eventClearInputNumber() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLEAR_INPUT_NUMBER,
                ""
        ))
    }

    fun eventClickBackButton(categoryId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_BACK_BUTTON,
                categoryId.toString()
        ))
    }

    fun eventClickSeeMore(categoryId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLICK_SEE_MORE,
                categoryId.toString()
        ))
    }

    fun eventCloseDetailProduct(categoryId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.CLICK_HOMEPAGE,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.CLOSE_DETAIL_PRODUCT,
                categoryId.toString()
        ))
    }

    fun eventClickCopyPromoCode(promoName: String, position: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                DigitalTopupEventTracking.Event.PROMO_CLICK,
                DigitalTopupEventTracking.Category.DIGITAL_HOMEPAGE,
                DigitalTopupEventTracking.Action.COPY_PROMO_DIGITAL,
                "$promoName - $position"
        ))
    }
}
