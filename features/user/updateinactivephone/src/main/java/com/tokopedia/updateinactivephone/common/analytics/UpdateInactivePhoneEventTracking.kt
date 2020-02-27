package com.tokopedia.updateinactivephone.common.analytics

import android.content.Context
import com.tokopedia.core.analytics.TrackingUtils

import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.core.analytics.nishikino.model.EventTracking
import com.tokopedia.core.gcm.utils.RouterUtils

class UpdateInactivePhoneEventTracking : UnifyTracking() {
    companion object {

        fun eventInactivePhoneClick(context: Context) {
            TrackingUtils.sendGTMEvent(context, EventTracking(
                    UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                    UpdateInactivePhoneEventConstants.Category.INACTIVE_PHONE_PAGE,
                    UpdateInactivePhoneEventConstants.Action.CLICK_SELUNJANTYA,
                    ""
            ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().userId).event
            )
        }

        fun eventLoginDialogClick(context: Context) {
            TrackingUtils.sendGTMEvent(context, EventTracking(
                    UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                    UpdateInactivePhoneEventConstants.Category.CHANGE_NOT_ACTIVE_PHONE,
                    UpdateInactivePhoneEventConstants.Action.CLICK_MASUK,
                    ""
            ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().userId).event
            )
        }

        fun eventCancelDialogClick(context: Context) {
            TrackingUtils.sendGTMEvent(context, EventTracking(
                    UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                    UpdateInactivePhoneEventConstants.Category.INACTIVE_PHONE_PAGE,
                    UpdateInactivePhoneEventConstants.Action.CLICK_BATAL,
                    ""
            ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().userId).event
            )
        }

        fun eventViewWaitingForConfirmationPage(context: Context) {
            TrackingUtils.sendGTMEvent(context, EventTracking(
                    UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_VIEW,
                    UpdateInactivePhoneEventConstants.Category.WAITING_CONFIRMATION_PAGE,
                    UpdateInactivePhoneEventConstants.Action.VIEW_ON_WAITING_CONFIRMATION_APGE,
                    UpdateInactivePhoneEventConstants.EventLabel.CHANGE_NOT_ACTIVE_PHONE
            ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().userId).event
            )
        }

        fun eventViewSubmitSuccessPage(context: Context) {
            TrackingUtils.sendGTMEvent(context, EventTracking(
                    UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_VIEW,
                    UpdateInactivePhoneEventConstants.Category.SUBMIT_SUCCESS_PAGE,
                    UpdateInactivePhoneEventConstants.Action.VIEW_ON_SUBMIT_SUCCESS_PAGE,
                    UpdateInactivePhoneEventConstants.EventLabel.CHANGE_NOT_ACTIVE_PHONE
            ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().userId).event
            )
        }

        fun eventClickKirimPengajuan(context: Context) {
            TrackingUtils.sendGTMEvent(context, EventTracking(
                    UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                    UpdateInactivePhoneEventConstants.Category.CHANGE_NOT_ACTIVE_PHONE,
                    UpdateInactivePhoneEventConstants.Action.CLICK_KIRIM_PENGAJUAN,
                    ""
            ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().userId).event
            )
        }

        fun eventViewKirimPengajuan(context: Context) {
            TrackingUtils.sendGTMEvent(context, EventTracking(
                    UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_VIEW,
                    UpdateInactivePhoneEventConstants.Category.INPUT_ID_AND_BANK_ACCOUNT,
                    UpdateInactivePhoneEventConstants.Action.VIEW_KIRIM_PENGAJUAN,
                    ""
            ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().userId).event
            )
        }

        fun eventViewPhotoUploadScreen(context: Context) {
            TrackingUtils.sendGTMEvent(context, EventTracking(
                    UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_VIEW,
                    UpdateInactivePhoneEventConstants.Category.INPUT_ID_AND_BANK_ACCOUNT,
                    UpdateInactivePhoneEventConstants.Action.VIEW_ON_LANJUTKAN,
                    ""
            ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().userId).event
            )
        }

        fun eventClickPhotoProceed(context: Context) {
            TrackingUtils.sendGTMEvent(context, EventTracking(
                    UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                    UpdateInactivePhoneEventConstants.Category.INPUT_ID_AND_BANK_ACCOUNT,
                    UpdateInactivePhoneEventConstants.Action.CLICK_ON_LANJUTKAN,
                    ""
            ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().userId).event
            )
        }
    }
}
