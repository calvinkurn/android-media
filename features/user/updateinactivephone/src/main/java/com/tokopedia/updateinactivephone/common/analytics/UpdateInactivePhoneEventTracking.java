package com.tokopedia.updateinactivephone.common.analytics;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;

public class UpdateInactivePhoneEventTracking extends UnifyTracking {

    public static void eventInactivePhoneClick() {
        sendGTMEvent(new EventTracking(
                        UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                        UpdateInactivePhoneEventConstants.Category.INACTIVE_PHONE_PAGE,
                        UpdateInactivePhoneEventConstants.Action.CLICK_SELUNJANTYA,
                        ""
                ).setUserId().getEvent()
        );
    }

    public static void eventLoginDialogClick() {
        sendGTMEvent(new EventTracking(
                        UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                        UpdateInactivePhoneEventConstants.Category.CHANGE_NOT_ACTIVE_PHONE,
                        UpdateInactivePhoneEventConstants.Action.CLICK_MASUK,
                        ""
                ).setUserId().getEvent()
        );
    }

    public static void eventCancelDialogClick() {
        sendGTMEvent(new EventTracking(
                        UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                        UpdateInactivePhoneEventConstants.Category.INACTIVE_PHONE_PAGE,
                        UpdateInactivePhoneEventConstants.Action.CLICK_BATAL,
                        ""
                ).setUserId().getEvent()
        );
    }

    public static void eventViewWaitingForConfirmationPage() {
        sendGTMEvent(new EventTracking(
                        UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_VIEW,
                        UpdateInactivePhoneEventConstants.Category.WAITING_CONFIRMATION_PAGE,
                        UpdateInactivePhoneEventConstants.Action.VIEW_ON_WAITING_CONFIRMATION_APGE,
                        UpdateInactivePhoneEventConstants.EventLabel.CHANGE_NOT_ACTIVE_PHONE
                ).setUserId().getEvent()
        );
    }

    public static void eventViewSubmitSuccessPage() {
        sendGTMEvent(new EventTracking(
                        UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_VIEW,
                        UpdateInactivePhoneEventConstants.Category.SUBMIT_SUCCESS_PAGE,
                        UpdateInactivePhoneEventConstants.Action.VIEW_ON_SUBMIT_SUCCESS_PAGE,
                        UpdateInactivePhoneEventConstants.EventLabel.CHANGE_NOT_ACTIVE_PHONE
                ).setUserId().getEvent()
        );
    }

    public static void eventClickKirimPengajuan() {
        sendGTMEvent(new EventTracking(
                        UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                        UpdateInactivePhoneEventConstants.Category.CHANGE_NOT_ACTIVE_PHONE,
                        UpdateInactivePhoneEventConstants.Action.CLICK_KIRIM_PENGAJUAN,
                        ""
                ).setUserId().getEvent()
        );
    }

    public static void eventViewKirimPengajuan() {
        sendGTMEvent(new EventTracking(
                        UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_VIEW,
                        UpdateInactivePhoneEventConstants.Category.INPUT_ID_AND_BANK_ACCOUNT,
                        UpdateInactivePhoneEventConstants.Action.VIEW_KIRIM_PENGAJUAN,
                        ""
                ).setUserId().getEvent()
        );
    }

    public static void eventViewPhotoUploadScreen() {
        sendGTMEvent(new EventTracking(
                        UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_VIEW,
                        UpdateInactivePhoneEventConstants.Category.INPUT_ID_AND_BANK_ACCOUNT,
                        UpdateInactivePhoneEventConstants.Action.VIEW_ON_LANJUTKAN,
                        ""
                ).setUserId().getEvent()
        );
    }

    public static void eventClickPhotoProceed() {
        sendGTMEvent(new EventTracking(
                        UpdateInactivePhoneEventConstants.Event.INACTIVE_PHONE_CLICK,
                        UpdateInactivePhoneEventConstants.Category.INPUT_ID_AND_BANK_ACCOUNT,
                        UpdateInactivePhoneEventConstants.Action.CLICK_ON_LANJUTKAN,
                        ""
                ).setUserId().getEvent()
        );
    }
}
