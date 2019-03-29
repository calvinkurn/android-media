package com.tokopedia.updateinactivephone.common.analytics;

public class UpdateInactivePhoneEventConstants {

    public interface Screen {
        String INPUT_OLD_PHONE_SCREEN = "inactive phone number screen";
        String INPUT_NEW_PHONE_SCREEN = "update new phone number screen";
        String SELECT_IMAGE_TO_UPLOAD = "select image to upload screen";
        String WAITING_CONFIRMATION_PAGE = "waiting confirmation page";
        String SUBMIT_SUCCESS_REQUEST_PAGE = "submit request success page";
    }

    interface Event {
        String INACTIVE_PHONE_CLICK = "InactivePhoneClick";
        String INACTIVE_PHONE_VIEW = "InactivePhoneView";
    }

    interface Category {
        String INACTIVE_PHONE_PAGE = "not active phone number page";
        String CHANGE_NOT_ACTIVE_PHONE = "change not active phone number";
        String WAITING_CONFIRMATION_PAGE = "waiting confirmation page";
        String INPUT_ID_AND_BANK_ACCOUNT = "input id card and bank account";
        String SUBMIT_SUCCESS_PAGE = "submit success page";
    }

    interface Action {
        String CLICK_SELUNJANTYA = "click on selanjutnya";
        String CLICK_MASUK = "click on masuk";
        String CLICK_BATAL = "click on batal";
        String VIEW_ON_WAITING_CONFIRMATION_APGE = "view on waiting confirmation page";
        String VIEW_ON_LANJUTKAN = "view on lanjutkan";
        String CLICK_ON_LANJUTKAN = "click on lanjutkan";
        String VIEW_ON_SUBMIT_SUCCESS_PAGE = "view on submit success page";
        String CLICK_KIRIM_PENGAJUAN = "click on kirim pengajuan";
        String VIEW_KIRIM_PENGAJUAN = "view on kirim pengajuan";

    }

    public interface EventLabel {
        String CHANGE_NOT_ACTIVE_PHONE = "change not active phone number";
    }
}
