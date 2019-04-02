package com.tokopedia.updateinactivephone.common.analytics;

import android.content.Context;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.gcm.utils.RouterUtils;

public class UpdateInactivePhoneEventTracking extends UnifyTracking {

    public static void eventInactivePhoneClick(Context context) {
        sendGTMEvent(context,new EventTracking(
                        UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                        UpdateInactivePhoneEventConstants.Category.INACTIVE_PHONE_PAGE,
                        UpdateInactivePhoneEventConstants.Action.CLICK_SELUNJANTYA,
                        ""
                ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent()
        );
    }

    public static void eventLoginDialogClick(Context context) {
        sendGTMEvent(context,new EventTracking(
                        UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                        UpdateInactivePhoneEventConstants.Category.CHANGE_NOT_ACTIVE_PHONE,
                        UpdateInactivePhoneEventConstants.Action.CLICK_MASUK,
                        ""
                ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent()
        );
    }

    public static void eventCancelDialogClick(Context context) {
        sendGTMEvent(context, new EventTracking(
                        UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                        UpdateInactivePhoneEventConstants.Category.INACTIVE_PHONE_PAGE,
                        UpdateInactivePhoneEventConstants.Action.CLICK_BATAL,
                        ""
                ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent()
        );
    }

    public static void eventViewWaitingForConfirmationPage(Context context) {
        sendGTMEvent(context,new EventTracking(
                        UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_VIEW,
                        UpdateInactivePhoneEventConstants.Category.WAITING_CONFIRMATION_PAGE,
                        UpdateInactivePhoneEventConstants.Action.VIEW_ON_WAITING_CONFIRMATION_APGE,
                        UpdateInactivePhoneEventConstants.EventLabel.CHANGE_NOT_ACTIVE_PHONE
                ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent()
        );
    }

    public static void eventViewSubmitSuccessPage(Context context) {
        sendGTMEvent(context,new EventTracking(
                        UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_VIEW,
                        UpdateInactivePhoneEventConstants.Category.SUBMIT_SUCCESS_PAGE,
                        UpdateInactivePhoneEventConstants.Action.VIEW_ON_SUBMIT_SUCCESS_PAGE,
                        UpdateInactivePhoneEventConstants.EventLabel.CHANGE_NOT_ACTIVE_PHONE
                ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent()
        );
    }

    public static void eventClickKirimPengajuan(Context context) {
        sendGTMEvent(context,new EventTracking(
                        UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                        UpdateInactivePhoneEventConstants.Category.CHANGE_NOT_ACTIVE_PHONE,
                        UpdateInactivePhoneEventConstants.Action.CLICK_KIRIM_PENGAJUAN,
                        ""
                ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent()
        );
    }

    public static void eventViewKirimPengajuan(Context context) {
        sendGTMEvent(context, new EventTracking(
                        UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_VIEW,
                        UpdateInactivePhoneEventConstants.Category.INPUT_ID_AND_BANK_ACCOUNT,
                        UpdateInactivePhoneEventConstants.Action.VIEW_KIRIM_PENGAJUAN,
                        ""
                ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent()
        );
    }

    public static void eventViewPhotoUploadScreen(Context context) {
        sendGTMEvent(context,new EventTracking(
                        UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_VIEW,
                        UpdateInactivePhoneEventConstants.Category.INPUT_ID_AND_BANK_ACCOUNT,
                        UpdateInactivePhoneEventConstants.Action.VIEW_ON_LANJUTKAN,
                        ""
                ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent()
        );
    }

    public static void eventClickPhotoProceed(Context context) {
        sendGTMEvent(context, new EventTracking(
                        UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                        UpdateInactivePhoneEventConstants.Category.INPUT_ID_AND_BANK_ACCOUNT,
                        UpdateInactivePhoneEventConstants.Action.CLICK_ON_LANJUTKAN,
                        ""
                ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent()
        );
    }
}
