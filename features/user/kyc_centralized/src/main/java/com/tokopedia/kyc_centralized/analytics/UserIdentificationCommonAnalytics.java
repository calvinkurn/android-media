package com.tokopedia.kyc_centralized.analytics;

import static com.tokopedia.kyc_centralized.common.KYCConstant.CO_BRAND_PROJECT_ID;
import static com.tokopedia.kyc_centralized.common.KYCConstant.GO_CICIL_PROJECT_ID;
import static com.tokopedia.kyc_centralized.common.KYCConstant.HOME_CREDIT_PROJECT_ID;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.util.Map;

public class UserIdentificationCommonAnalytics {

    private final int projectID;

    private static final String BUSINESS_UNIT = "businessUnit";
    private static final String USER_PLATFORM = "user platform";
    private static final String TOKOPEDIA_MARKETPLACE = "tokopediamarketplace";
    private static final String CURRENT_SITE = "currentSite";
    private static final String TRACKER_ID = "trackerId";

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
        private static final String CLICK_ON_BUTTON_CAPTURE_CAMERA = "click on button capture";
        private static final String CLICK_FLIP_CAMERA_KTP = "click on change camera foto KTP";
        private static final String VIEW_IMAGE_PREVIEW_KTP = "view on take foto KTP";
        private static final String CLICK_CLOSE_IMAGE_PREVIEW_KTP = "click on close take foto KTP";
        private static final String CLICK_RECAPTURE_KTP = "click on foto ulang KTP";
        private static final String CLICK_BUTTON_RECAPTURE = "click on button foto ulang";
        private static final String CLICK_NEXT_IMAGE_PREVIEW_KTP = "click on lanjut take foto KTP";
        private static final String CLICK_ON_BUTTON_LANJUT_PREVIEW = "click on button lanjut";

        private static final String VIEW_SELFIE_PAGE = "view on panduan selfie KTP";
        private static final String CLICK_NEXT_SELFIE_PAGE = "click on ambil foto diri bersama KTP";
        private static final String CLICK_ON_BUTTON_VERIFIKASI_WAJAH_PAGE = "click on button mulai verifikasi wajah";
        private static final String CLICK_BACK_SELFIE_PAGE = "click on back panduan selfie KTP";
        private static final String VIEW_ERROR_IMAGE_TOO_LARGE_SELFIE = "view on error maximum foto size selfie KTP";

        private static final String VIEW_OPEN_CAMERA_SELFIE = "view on open camera selfie bersama KTP";
        private static final String CLICK_BACK_CAMERA_SELFIE = "click on back selfie bersama KTP";
        private static final String CLICK_SHUTTER_CAMERA_SELFIE = "click on foto selfie bersama KTP";
        private static final String CLICK_ON_BUTTON_SHUTTER_CAMERA_SELFIE = "click on button ambil foto selfie";
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
        private static final String CLICK_TERMS_AND_CONDITION_FINAL_FORM_PAGE = "click on syarat dan ketentuan sukses terverifikasi";
        private static final String CLICK_UPLOAD_PHOTOS = "click on unggah foto KTP";
        private static final String CLICK_RETAKE_KTP_SELFIE_FINAL_FORM_PAGE = "click on button coba lagi";

        private static final String CLICK_ON_BUTTON_EXIT = "click on button keluar";
        private static final String CLICK_ON_BUTTON_STAY = "click on button lanjut kirim";

        private static final String CLICK_ON_BUTTON_COBA_LAGI = "click on button coba lagi";
    }

    private static class Category {
        private static final String KYC_PAGE = "kyc page";
        private static final String KYC_KTP_PAGE = "kyc ktp page";
        private static final String KYC_SELFIE_PAGE = "kyc ktp page";
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

    public void eventClickBackSelfiePage(Boolean isLiveness) {
        if (isLiveness) {
            track(TrackAppUtils.gtmData(
                    Event.CLICK_ACCOUNT,
                    Category.KYC_LIVENESS_PAGE,
                    Action.CLICK_ON_BUTTON_BACK,
                    Label.labelOne + " - " + projectID + " - " + getKycType(String.valueOf(projectID))
            ), "2629");
        } else  {
            track(TrackAppUtils.gtmData(
                    Event.CLICK_ACCOUNT,
                    Category.KYC_SELFIE_PAGE,
                    Action.CLICK_ON_BUTTON_BACK,
                    Label.labelOne + " - click - " + projectID + " - " + getKycType(String.valueOf(projectID))
            ), "2621");
        }
    }

    public void eventViewSelfiePage(Boolean isSelfie) {
        if(projectID ==4){
            sendTradeInViewEvent(Action.VIEW_SELFIE_PAGE);
        }

        if (isSelfie) {
            track(TrackAppUtils.gtmData(
                    Event.VIEW_ACCOUNT_IRIS,
                    Category.KYC_PAGE,
                    Action.VIEW_SELFIE_PAGE,
                    "success - " + projectID + " - " + getKycType(String.valueOf(projectID))
            ), "35141");
        }
    }

    public void eventClickNextSelfiePage(Boolean isLiveness) {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_NEXT_SELFIE_PAGE,"");
        }

        if (isLiveness) {
            track(TrackAppUtils.gtmData(
                    Event.CLICK_ACCOUNT,
                    Category.KYC_LIVENESS_PAGE,
                    Action.CLICK_ON_BUTTON_VERIFIKASI_WAJAH_PAGE,
                    "click - " + projectID + " - " + getKycType(String.valueOf(projectID))
            ), "2628");
        } else  {
            track(TrackAppUtils.gtmData(
                    Event.CLICK_ACCOUNT,
                    Category.KYC_SELFIE_PAGE,
                    Action.CLICK_NEXT_SELFIE_PAGE,
                    "click - " + projectID + " - " + getKycType(String.valueOf(projectID))
            ), "");
        }
    }

    public void eventViewOpenCameraKtp() {
        track(TrackAppUtils.gtmData(
                Event.VIEW_ACCOUNT_IRIS,
                Category.KYC_PAGE,
                Action.VIEW_OPEN_CAMERA_KTP,
                "success - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "35138");
    }

    public void eventViewOpenCameraSelfie() {
        track(TrackAppUtils.gtmData(
                Event.VIEW_ACCOUNT_IRIS,
                Category.KYC_PAGE,
                Action.VIEW_OPEN_CAMERA_SELFIE,
                "success - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "35139");
    }

    public void eventClickBackCameraKtp() {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_KTP_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelTwo + " - click - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "2621");
    }

    public void eventClickBackCameraSelfie() {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_BACK_CAMERA_SELFIE,
                "click - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "35240");
    }

    public void eventClickFlipCameraKtp() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_FLIP_CAMERA_KTP,"");
        }

        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_FLIP_CAMERA_KTP,
                "success - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "35131");
    }

    public void eventClickShutterCameraKtp() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_SHUTTER_CAMERA_KTP,"");
        }

        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_KTP_PAGE,
                Action.CLICK_ON_BUTTON_CAPTURE_CAMERA,
                "click - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "2622");
    }

    public void eventClickShutterCameraSelfie() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_SHUTTER_CAMERA_SELFIE,"");
        }

        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_SHUTTER_CAMERA_SELFIE,
                "success - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "35133");
    }

    public void eventViewErrorImageTooLargeKtpPage() {
        track(TrackAppUtils.gtmData(
                Event.VIEW_ACCOUNT_IRIS,
                Category.KYC_KTP_PAGE,
                Action.VIEW_ERROR_IMAGE_TOO_LARGE_KTP,
                "failed  - " + Action.VIEW_ERROR_IMAGE_TOO_LARGE_KTP + " - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "2627");
    }

    public void eventClickFlipCameraSelfie() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_FLIP_CAMERA_SELFIE,"");
        }

        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_FLIP_CAMERA_SELFIE,
                "success - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "35132");
    }

    public void eventViewImagePreviewKtp() {
        track(TrackAppUtils.gtmData(
                Event.VIEW_ACCOUNT_IRIS,
                Category.KYC_PAGE,
                Action.VIEW_IMAGE_PREVIEW_KTP,
                "success - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "35215");
    }

    public void eventViewImagePreviewSelfie() {
        track(TrackAppUtils.gtmData(
                Event.VIEW_ACCOUNT_IRIS,
                Category.KYC_PAGE,
                Action.VIEW_IMAGE_PREVIEW_SELFIE,
                "success - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "35216");
    }

    public void eventClickCloseImagePreviewKtp() {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_KTP_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelThree + " - click - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "2646");
    }

    public void eventClickCloseImagePreviewSelfie() {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_CLOSE_IMAGE_PREVIEW_SELFIE,
                "click - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "35241");
    }

    public void eventClickRecaptureKtp() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_RECAPTURE_KTP,"");
        }

        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_KTP_PAGE,
                Action.CLICK_BUTTON_RECAPTURE,
                "click - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "2625");
    }

    public void eventClickRecaptureSelfie() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_RECAPTURE_SELFIE,"");
        }

        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_RECAPTURE_SELFIE,
                "success - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "35134");
    }

    public void eventClickNextImagePreviewKtp() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_NEXT_IMAGE_PREVIEW_KTP,"");
        }

        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_KTP_PAGE,
                Action.CLICK_ON_BUTTON_LANJUT_PREVIEW,
                "click - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "2626");
    }

    public void eventClickNextImagePreviewSelfie() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_NEXT_IMAGE_PREVIEW_SELFIE,"");
        }

        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_NEXT_IMAGE_PREVIEW_SELFIE,
                "success - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "35135");
    }

    public void eventViewFinalForm() {
        if(projectID ==4){
            sendTradeInViewEvent(Action.VIEW_FINAL_FORM_PAGE);
        }

        track(TrackAppUtils.gtmData(
                Event.VIEW_ACCOUNT_IRIS,
                Category.KYC_PAGE,
                Action.VIEW_FINAL_FORM_PAGE,
                "success - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "35142");
    }

    public void eventClickChangeKtpFinalFormPage() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_CHANGE_KTP_FINAL_FORM_PAGE,"");
        }

        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_RETAKE_KTP_FINAL_FORM_PAGE,
                "click - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "2645");
    }

    public void eventClickBackChangeKtpFinalFormPage(){
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelOne + " - click - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "2646");
    }

    public void eventClickBackChangeSelfieFinalFormPage(){
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelTwo + " - click - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "2646");
    }

    public void eventClickChangeSelfieFinalFormPage() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_CHANGE_SELFIE_FINAL_FORM_PAGE,"");
        }

        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_RETAKE_SELFIE_FINAL_FORM_PAGE,
                "click - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "2647");
    }

    public void eventClickChangeKtpSelfieFinalFormPage(){
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_RETAKE_KTP_SELFIE_FINAL_FORM_PAGE,
                "click - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "2649");
    }

    public void eventClickBackChangeKtpSelfieFinalFormPage(){
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelThree + " - click - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "2646");
    }

    public void eventClickTermsFinalFormPage() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_TERMS_AND_CONDITION_FINAL_FORM_PAGE,"");
        }

        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_TERMS_AND_CONDITION_FINAL_FORM_PAGE,
                "click - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "35136");
    }

    public void eventClickUploadPhotosTradeIn(String label) {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_UPLOAD_PHOTOS,label);
        }
    }

    public void eventClickBackFinalForm() {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_BACK_FINAL_FORM_PAGE,
                "success - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "35129");
    }

    public void eventViewKtpPage() {
        if(projectID ==4){
            sendTradeInViewEvent(Action.VIEW_KTP_PAGE);
        }

        track(TrackAppUtils.gtmData(
                Event.VIEW_ACCOUNT_IRIS,
                Category.KYC_PAGE,
                Action.VIEW_KTP_PAGE,
                "success - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "35140");
    }

    public void eventClickNextKtpPage() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_NEXT_KTP_PAGE,"");
        }

        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_KTP_PAGE,
                Action.CLICK_ON_BUTTON_AMBIL_KTP_PAGE,
                "click - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "2620");
    }

    public void eventClickBackKtpPage() {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_KTP_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelOne + " - click - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "2621");
    }

    public void eventClickDialogExit() {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_ON_BUTTON_EXIT,
                "click - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "2654");
    }

    public void eventClickDialogStay() {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_ON_BUTTON_STAY,
                "click - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "2655");
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
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_FAILED_PAGE,
                Action.CLICK_ON_BUTTON_COBA_LAGI,
                Label.labelConnectionTimeout + " - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "2656");
    }

    public void eventClickBackConnectionTimeout() {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_FAILED_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelConnectionTimeout + " - " + projectID + " - " + getKycType(String.valueOf(projectID))
        ), "2657");
    }

    private String getKycType(String projectID) {
        String TYPE_ALA_CARTE = "ala carte";
        String TYPE_CKYC = "ckyc";

        if (
            projectID.equals(HOME_CREDIT_PROJECT_ID) ||
            projectID.equals(CO_BRAND_PROJECT_ID) ||
            projectID.equals(GO_CICIL_PROJECT_ID)
        ) {
            return TYPE_ALA_CARTE;
        } else {
            return TYPE_CKYC;
        }
    }

    private void track(Map<String, Object> data, String trackerId) {
        data.put(BUSINESS_UNIT, USER_PLATFORM);
        data.put(CURRENT_SITE, TOKOPEDIA_MARKETPLACE);
        data.put(TRACKER_ID, trackerId);
        TrackApp.getInstance().getGTM().sendGeneralEvent(data);
    }
}
