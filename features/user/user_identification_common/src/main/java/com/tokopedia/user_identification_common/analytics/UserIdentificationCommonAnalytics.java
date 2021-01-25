package com.tokopedia.user_identification_common.analytics;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

public class UserIdentificationCommonAnalytics {

    private final int projectID;

    private static class Event {
        private static final String CLICK_ACCOUNT = "clickAccount";
        private static final String VIEW_KYC = "viewKYC";
        private static final String VIEW_ACCOUNT_IRIS= "viewAccountIris";
        private static final String CLICK_KYC = "clickKYC";
        private static final String VIEW_TRADEIN = "viewTradeIn";
        private static final String CLICK_TRADEIN = "clickTradeIn";
    }

    private static class Action {
        private static final String CLICK_ON_BUTTON_BACK = "click on button back";

        private static final String VIEW_KYC_ONBOARDING = "view on KYC onboarding";
        private static final String CLICK_BACK_ONBOARDING = "click on back KYC onboarding";
        private static final String CLICK_NEXT_ONBOARDING = "click on lanjut kyc onboarding";

        private static final String VIEW_KTP_PAGE = "view on panduan KTP KYC";
        private static final String CLICK_BACK_KTP_PAGE = "click on back panduan KTP KYC";
        private static final String CLICK_NEXT_KTP_PAGE = "click on ambil foto KTP";
        private static final String CLICK_ON_BUTTON_AMBIL_KTP_PAGE = "click on button ambil foto ktp";
        private static final String VIEW_ERROR_IMAGE_TOO_LARGE_KTP = "view error message foto melebihi ukuran maksimum";

        private static final String VIEW_OPEN_CAMERA_KTP = "view on open camera KTP";
        private static final String CLICK_BACK_CAMERA_KTP = "click on close ambil foto KTP";
        private static final String CLICK_SHUTTER_CAMERA_KTP = "click on foto KTP";
        private static final String CLICK_ON_BUTTON_CAPTURE_CAMERA_KTP = "click on button capture";
        private static final String CLICK_FLIP_CAMERA_KTP = "click on change camera foto KTP";
        private static final String VIEW_IMAGE_PREVIEW_KTP = "view on take foto KTP";
        private static final String CLICK_CLOSE_IMAGE_PREVIEW_KTP = "click on close take foto KTP";
        private static final String CLICK_RECAPTURE_KTP = "click on foto ulang KTP";
        private static final String CLICK_BUTTON_RECAPTURE_KTP = "click on button foto ulang";
        private static final String CLICK_NEXT_IMAGE_PREVIEW_KTP = "click on lanjut take foto KTP";
        private static final String CLICK_ON_BUTTON_LANJUT_PREVIEW_KTP = "click on button lanjut";

        private static final String VIEW_SELFIE_PAGE = "view on panduan selfie KTP";
        private static final String CLICK_NEXT_SELFIE_PAGE = "click on ambil foto diri bersama KTP";
        private static final String CLICK_ON_BUTTON_VERIFIKASI_WAJAH_PAGE = "click on button mulai verifikasi wajah";
        private static final String CLICK_BACK_SELFIE_PAGE = "click on back panduan selfie KTP";
        private static final String VIEW_ERROR_IMAGE_TOO_LARGE_SELFIE = "view on error maximum foto size selfie KTP";

        private static final String VIEW_OPEN_CAMERA_SELFIE = "view on open camera selfie bersama KTP";
        private static final String CLICK_BACK_CAMERA_SELFIE = "click on back selfie bersama KTP";
        private static final String CLICK_SHUTTER_CAMERA_SELFIE = "click on foto selfie bersama KTP";
        private static final String CLICK_FLIP_CAMERA_SELFIE = "click on change camera selfie bersama KTP";
        private static final String VIEW_IMAGE_PREVIEW_SELFIE = "view on take foto selfie bersama KTP";
        private static final String CLICK_CLOSE_IMAGE_PREVIEW_SELFIE = "click on back take selfie KTP";
        private static final String CLICK_RECAPTURE_SELFIE = "click on foto ulang take selfie KTP";
        private static final String CLICK_NEXT_IMAGE_PREVIEW_SELFIE = "click on lanjut take selfie KTP";

        private static final String VIEW_FINAL_FORM_PAGE = "view on preview KTP and selfie";
        private static final String CLICK_BACK_FINAL_FORM_PAGE = "click on back preview KTP and selfie";
        private static final String CLICK_CHANGE_KTP_FINAL_FORM_PAGE = "click on ubah foto KTP";
        private static final String CLICK_RETAKE_KTP_FINAL_FORM_PAGE = "click on button foto ulang ktp";
        private static final String CLICK_CHANGE_SELFIE_FINAL_FORM_PAGE = "click on ubah foto selfie KTP";
        private static final String CLICK_RETAKE_SELFIE_FINAL_FORM_PAGE = "click on button verifikasi ulang wajah";
        private static final String CLICK_TERMS_AND_CONDITION_FINAL_FORM_PAGE = "click on syarat dan ketentuan";
        private static final String CLICK_UPLOAD_PHOTOS = "click on unggah foto KTP";
        private static final String CLICK_RETAKE_KTP_SELFIE_FINAL_FORM_PAGE = "click on button coba lagi";

        private static final String CLICK_ON_BUTTON_EXIT = "click on button keluar";
        private static final String CLICK_ON_BUTTON_STAY = "click on button lanjut kirim";

        private static final String CLICK_ON_BUTTON_COBA_LAGI = "click on button coba lagi";
    }

    private static class Category {
        private static final String KYC_PAGE = "kyc page";
        private static final String KYC_KTP_PAGE = "kyc ktp page";
        private static final String KYC_LIVENESS_PAGE = "kyc liveness page";
        private static final String KYC_SUBMISSION_PAGE = "kyc submission page";
        private static final String KYC_PAGE_TRADEIN = "kyc trade in page";

        private static final String KYC_LIVENESS_FAILED_PAGE = "kyc liveness failed page";
    }

    private static class Label {
        private static final String labelOne = "1";
        private static final String labelTwo = "2";
        private static final String labelThree = "3";

        private static final String labelConnectionTimeout = "connection timeout";
    }

    private UserIdentificationCommonAnalytics(int projectID) {
        this.projectID = projectID;
    }

    public static UserIdentificationCommonAnalytics createInstance(int projectID) {
        return new UserIdentificationCommonAnalytics(projectID);
    }

    public void eventClickBackSelfiePage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelOne
        ));
    }

    public void eventViewSelfiePage() {
        if(projectID ==4){
            sendTradeInViewEvent(Action.VIEW_SELFIE_PAGE);
        } else {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    Event.VIEW_KYC,
                    Category.KYC_PAGE,
                    Action.VIEW_SELFIE_PAGE,
                    ""
            ));
        }
    }

    public void eventClickNextSelfiePage() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_NEXT_SELFIE_PAGE,"");
        } else {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    Event.CLICK_ACCOUNT,
                    Category.KYC_LIVENESS_PAGE,
                    Action.CLICK_ON_BUTTON_VERIFIKASI_WAJAH_PAGE,
                    ""
            ));
        }
    }

    public void eventViewOpenCameraKtp() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_OPEN_CAMERA_KTP,
                ""
        ));
    }

    public void eventViewOpenCameraSelfie() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_OPEN_CAMERA_SELFIE,
                ""
        ));
    }

    public void eventClickBackCameraKtp() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_KTP_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelTwo
        ));
    }

    public void eventClickBackCameraSelfie() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_BACK_CAMERA_SELFIE,
                ""
        ));
    }

    public void eventClickFlipCameraKtp() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_FLIP_CAMERA_KTP,"");
        } else {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    Event.CLICK_KYC,
                    Category.KYC_PAGE,
                    Action.CLICK_FLIP_CAMERA_KTP,
                    ""
            ));
        }
    }

    public void eventClickShutterCameraKtp() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_SHUTTER_CAMERA_KTP,"");
        } else {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    Event.CLICK_ACCOUNT,
                    Category.KYC_KTP_PAGE,
                    Action.CLICK_ON_BUTTON_CAPTURE_CAMERA_KTP,
                    ""
            ));
        }
    }

    public void eventClickShutterCameraSelfie() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_SHUTTER_CAMERA_SELFIE,"");
        } else {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    Event.CLICK_KYC,
                    Category.KYC_PAGE,
                    Action.CLICK_SHUTTER_CAMERA_SELFIE,
                    ""
            ));
        }
    }

    public void eventViewErrorImageTooLargeKtpPage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.VIEW_ACCOUNT_IRIS,
                Category.KYC_KTP_PAGE,
                Action.VIEW_ERROR_IMAGE_TOO_LARGE_KTP,
                ""
        ));
    }

    public void eventViewErrorImageTooLargeSelfiePage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_ERROR_IMAGE_TOO_LARGE_SELFIE,
                ""
        ));
    }

    public void eventClickFlipCameraSelfie() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_FLIP_CAMERA_SELFIE,"");
        } else {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    Event.CLICK_KYC,
                    Category.KYC_PAGE,
                    Action.CLICK_FLIP_CAMERA_SELFIE,
                    ""
            ));
        }
    }

    public void eventViewImagePreviewKtp() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_IMAGE_PREVIEW_KTP,
                ""
        ));
    }

    public void eventViewImagePreviewSelfie() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_IMAGE_PREVIEW_SELFIE,
                ""
        ));
    }

    public void eventClickCloseImagePreviewKtp() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_KTP_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelThree
        ));
    }

    public void eventClickCloseImagePreviewSelfie() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_CLOSE_IMAGE_PREVIEW_SELFIE,
                ""
        ));
    }

    public void eventClickRecaptureKtp() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_RECAPTURE_KTP,"");
        } else {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    Event.CLICK_ACCOUNT,
                    Category.KYC_KTP_PAGE,
                    Action.CLICK_BUTTON_RECAPTURE_KTP,
                    ""
            ));
        }
    }

    public void eventClickRecaptureSelfie() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_RECAPTURE_SELFIE,"");
        } else {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    Event.CLICK_KYC,
                    Category.KYC_PAGE,
                    Action.CLICK_RECAPTURE_SELFIE,
                    ""
            ));
        }
    }

    public void eventClickNextImagePreviewKtp() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_NEXT_IMAGE_PREVIEW_KTP,"");
        } else {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    Event.CLICK_ACCOUNT,
                    Category.KYC_KTP_PAGE,
                    Action.CLICK_ON_BUTTON_LANJUT_PREVIEW_KTP,
                    ""
            ));
        }
    }

    public void eventClickNextImagePreviewSelfie() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_NEXT_IMAGE_PREVIEW_SELFIE,"");
        } else {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    Event.CLICK_KYC,
                    Category.KYC_PAGE,
                    Action.CLICK_NEXT_IMAGE_PREVIEW_SELFIE,
                    ""
            ));
        }
    }

    public void eventViewFinalForm() {
        if(projectID ==4){
            sendTradeInViewEvent(Action.VIEW_FINAL_FORM_PAGE);
        } else {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    Event.VIEW_KYC,
                    Category.KYC_PAGE,
                    Action.VIEW_FINAL_FORM_PAGE,
                    ""
            ));
        }
    }

    public void eventClickChangeKtpFinalFormPage() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_CHANGE_KTP_FINAL_FORM_PAGE,"");
        } else {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    Event.CLICK_ACCOUNT,
                    Category.KYC_SUBMISSION_PAGE,
                    Action.CLICK_RETAKE_KTP_FINAL_FORM_PAGE,
                    ""
            ));
        }
    }

    public void eventClickBackChangeKtpFinalFormPage(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelOne
        ));
    }

    public void eventClickChangeSelfieFinalFormPage() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_CHANGE_SELFIE_FINAL_FORM_PAGE,"");
        } else {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    Event.CLICK_ACCOUNT,
                    Category.KYC_SUBMISSION_PAGE,
                    Action.CLICK_RETAKE_SELFIE_FINAL_FORM_PAGE,
                    ""
            ));
        }
    }

    public void eventClickBackChangeSelfieFinalFormPage(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelTwo
        ));
    }

    public void eventClickChangeKtpSelfieFinalFormPage(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_RETAKE_KTP_SELFIE_FINAL_FORM_PAGE,
                ""
        ));
    }

    public void eventClickBackChangeKtpSelfieFinalFormPage(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelThree
        ));
    }

    public void eventClickUploadPhotos() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_UPLOAD_PHOTOS,
                ""
        ));
    }

    public void eventClickTermsFinalFormPage() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_TERMS_AND_CONDITION_FINAL_FORM_PAGE,"");
        } else {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    Event.CLICK_KYC,
                    Category.KYC_PAGE,
                    Action.CLICK_TERMS_AND_CONDITION_FINAL_FORM_PAGE,
                    ""
            ));
        }
    }

    public void eventClickUploadPhotosTradeIn(String label) {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_UPLOAD_PHOTOS,label);
        }
    }

    public void eventClickBackFinalForm() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_BACK_FINAL_FORM_PAGE,
                ""
        ));
    }

    public void eventViewKtpPage() {
        if(projectID ==4){
            sendTradeInViewEvent(Action.VIEW_KTP_PAGE);
        } else {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    Event.VIEW_KYC,
                    Category.KYC_PAGE,
                    Action.VIEW_KTP_PAGE,
                    ""
            ));
        }
    }

    public void eventClickNextKtpPage() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_NEXT_KTP_PAGE,"");
        } else {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    Event.CLICK_ACCOUNT,
                    Category.KYC_KTP_PAGE,
                    Action.CLICK_ON_BUTTON_AMBIL_KTP_PAGE,
                    ""
            ));
        }
    }

    public void eventClickBackKtpPage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_KTP_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelOne
        ));
    }

    public void eventClickDialogExit() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_ON_BUTTON_EXIT,
                ""
        ));
    }

    public void eventClickDialogStay() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_ON_BUTTON_STAY,
                ""
        ));
    }

    private void sendTradeInClickEvent(String Action,String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_TRADEIN,
                Category.KYC_PAGE_TRADEIN,
                Action,
                label
        ));
    }

    private void sendTradeInViewEvent(String Action) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.VIEW_TRADEIN,
                Category.KYC_PAGE_TRADEIN,
                Action,
                ""));
    }

    public void eventClickConnectionTimeout() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_FAILED_PAGE,
                Action.CLICK_ON_BUTTON_COBA_LAGI,
                Label.labelConnectionTimeout
        ));
    }

    public void eventClickBackConnectionTimeout() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_FAILED_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelConnectionTimeout
        ));
    }

}
