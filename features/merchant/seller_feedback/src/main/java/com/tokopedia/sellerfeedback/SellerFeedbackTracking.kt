package com.tokopedia.sellerfeedback

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object SellerFeedbackTracking {
    private const val EVENT_CATEGORY_HOMEPAGE = "seller app - home"
    private const val EVENT_LABEL_HOMEPAGE = ""

    object Click {
        private const val EVENT_CLICK_HOMEPAGE = "clickHomepage"
        private const val EVENT_ACTION_CLICK_PUT_ATTACHMENT = "click feedback button - put attachment"
        private const val EVENT_ACTION_CLICK_REMOVE_ATTACHMENT = "click feedback button - remove"
        private const val EVENT_ACTION_CLICK_SUBMIT = "click feedback button - submit"

        fun eventClickPutAttachment() {
            val mapEvent = TrackAppUtils.gtmData(
                    EVENT_CLICK_HOMEPAGE,
                    EVENT_CATEGORY_HOMEPAGE,
                    EVENT_ACTION_CLICK_PUT_ATTACHMENT,
                    EVENT_LABEL_HOMEPAGE
            )
            TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
        }

        fun eventClickRemoveAttachment() {
            val mapEvent = TrackAppUtils.gtmData(
                    EVENT_CLICK_HOMEPAGE,
                    EVENT_CATEGORY_HOMEPAGE,
                    EVENT_ACTION_CLICK_REMOVE_ATTACHMENT,
                    EVENT_LABEL_HOMEPAGE
            )
            TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
        }

        fun eventClickSubmit() {
            val mapEvent = TrackAppUtils.gtmData(
                    EVENT_CLICK_HOMEPAGE,
                    EVENT_CATEGORY_HOMEPAGE,
                    EVENT_ACTION_CLICK_SUBMIT,
                    EVENT_LABEL_HOMEPAGE
            )
            TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
        }
    }
}