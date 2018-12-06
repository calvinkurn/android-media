package com.tokopedia.useridentification.analytics;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

/**
 * @author by alvinatin on 26/11/18.
 */

public class UserIdentificationAnalytics {

    private final AnalyticTracker analyticTracker;

    private static class Event {
        private static final String VIEW_KYC = "viewKYC";
        private static final String CLICK_KYC = "clickKYC";
    }

    private static class Action {
        private static final String VIEW_KYC_ONBOARDING = "view on KYC onboarding";
        private static final String CLICK_BACK_ONBOARDING = "click on back KYC onboarding";
        private static final String CLICK_NEXT_ONBOARDING = "click on lanjut kyc onboarding";

        private static final String VIEW_KTP_PAGE = "view on panduan KTP KYC";
        private static final String CLICK_BACK_KTP_PAGE = "click on back panduan KTP KYC";
        private static final String CLICK_NEXT_KTP_PAGE = "click on ambil foto KTP";
        private static final String VIEW_ERROR_IMAGE_TOO_LARGE_KTP = "view on error maximum foto size KTP";

        private static final String VIEW_OPEN_CAMERA_KTP = "view on open camera KTP";
        private static final String CLICK_BACK_CAMERA_KTP = "click on close ambil foto KTP";
        private static final String CLICK_SHUTTER_CAMERA_KTP = "click on foto KTP";
        private static final String CLICK_FLIP_CAMERA_KTP = "click on change camera foto KTP";
        private static final String VIEW_IMAGE_PREVIEW_KTP = "view on take foto KTP";
        private static final String CLICK_CLOSE_IMAGE_PREVIEW_KTP = "click on close take foto KTP";
        private static final String CLICK_RECAPTURE_KTP = "click on foto ulang KTP";
        private static final String CLICK_NEXT_IMAGE_PREVIEW_KTP = "click on lanjut take foto KTP";

        private static final String VIEW_SELFIE_PAGE = "view on panduan selfie KTP";
        private static final String CLICK_NEXT_SELFIE_PAGE = "click on ambil foto diri bersama KTP";
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
        private static final String CLICK_CHANGE_SELFIE_FINAL_FORM_PAGE = "click on ubah foto selfie KTP";
        private static final String CLICK_TERMS_AND_CONDITION_FINAL_FORM_PAGE = "click on syarat dan ketentuan";
        private static final String CLICK_UPLOAD_PHOTOS = "click on unggah foto KTP";

        private static final String VIEW_PENDING_PAGE = "view on menunggu verifikasi";
        private static final String CLICK_BACK_PENDING_PAGE = "click on back menunggu verifikasi";
        private static final String VIEW_SUCCESS_SNACKBAR_PENDING_PAGE = "view on success message verifikasi";

        private static final String VIEW_REJECTED_PAGE = "view on gagal verifikasi";
        private static final String CLICK_BACK_REJECTED_PAGE = "click on back gagal verifikasi";
        private static final String CLICK_NEXT_REJECTED_PAGE = "click on unggah ulang gagal verifikasi";

        private static final String VIEW_SUCCES_PAGE = "view on success terverifikasi";
        private static final String CLICK_BACK_SUCCESS_PAGE = "click on back success terverfikasi";
        private static final String CLICK_TERMS_AND_CONDITION_SUCCESS_PAGE = "click on syarat dan ketentuan sukses terverfikasi";
    }

    private static class Category {
        private static final String KYC_PAGE = "kyc page";
    }

    public UserIdentificationAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public static UserIdentificationAnalytics createInstance(Context context) {
        if (context != null && context instanceof AbstractionRouter) {
            return new UserIdentificationAnalytics(((AbstractionRouter) context).getAnalyticTracker());

        } else return null;
    }

    public void eventViewOnKYCOnBoarding() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_KYC_ONBOARDING,
                ""
        );
    }

    public void eventClickOnBackOnBoarding() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_BACK_ONBOARDING,
                ""
        );
    }

    public void eventClickOnNextOnBoarding() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_NEXT_ONBOARDING,
                ""
        );
    }

    public void eventViewKtpPage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_KTP_PAGE,
                ""
        );
    }

    public void eventClickBackKtpPage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_BACK_KTP_PAGE,
                ""
        );
    }

    public void eventClickNextKtpPage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_NEXT_KTP_PAGE,
                ""
        );
    }

    public void eventViewErrorImageTooLargeKtpPage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_ERROR_IMAGE_TOO_LARGE_KTP,
                ""
        );
    }

    public void eventViewOpenCameraKtp() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_OPEN_CAMERA_KTP,
                ""
        );
    }

    public void eventClickBackCameraKtp() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_BACK_CAMERA_KTP,
                ""
        );
    }

    public void eventClickShutterCameraKtp() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_SHUTTER_CAMERA_KTP,
                ""
        );
    }

    public void eventClickFlipCameraKtp() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_FLIP_CAMERA_KTP,
                ""
        );
    }

    public void eventViewImagePreviewKtp() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_IMAGE_PREVIEW_KTP,
                ""
        );
    }

    public void eventClickCloseImagePreviewKtp() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_CLOSE_IMAGE_PREVIEW_KTP,
                ""
        );
    }

    public void eventClickRecaptureKtp() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_RECAPTURE_KTP,
                ""
        );
    }

    public void eventClickNextImagePreviewKtp() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_NEXT_IMAGE_PREVIEW_KTP,
                ""
        );
    }

    public void eventViewSelfiePage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_SELFIE_PAGE,
                ""
        );
    }

    public void eventClickBackSelfiePage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_BACK_SELFIE_PAGE,
                ""
        );
    }

    public void eventClickNextSelfiePage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_NEXT_SELFIE_PAGE,
                ""
        );
    }

    public void eventViewErrorImageTooLargeSelfiePage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_ERROR_IMAGE_TOO_LARGE_SELFIE,
                ""
        );
    }

    public void eventViewOpenCameraSelfie() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_OPEN_CAMERA_SELFIE,
                ""
        );
    }

    public void eventClickBackCameraSelfie() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_BACK_CAMERA_SELFIE,
                ""
        );
    }

    public void eventClickShutterCameraSelfie() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_SHUTTER_CAMERA_SELFIE,
                ""
        );
    }

    public void eventClickFlipCameraSelfie() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_FLIP_CAMERA_SELFIE,
                ""
        );
    }

    public void eventViewImagePreviewSelfie() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_IMAGE_PREVIEW_SELFIE,
                ""
        );
    }

    public void eventClickCloseImagePreviewSelfie() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_CLOSE_IMAGE_PREVIEW_SELFIE,
                ""
        );
    }

    public void eventClickRecaptureSelfie() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_RECAPTURE_SELFIE,
                ""
        );
    }

    public void eventClickNextImagePreviewSelfie() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_NEXT_IMAGE_PREVIEW_SELFIE,
                ""
        );
    }

    public void eventViewFinalForm() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_FINAL_FORM_PAGE,
                ""
        );
    }

    public void eventClickBackFinalForm() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_BACK_FINAL_FORM_PAGE,
                ""
        );
    }

    public void eventClickChangeKtpFinalFormPage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_CHANGE_KTP_FINAL_FORM_PAGE,
                ""
        );
    }

    public void eventClickChangeSelfieFinalFormPage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_CHANGE_SELFIE_FINAL_FORM_PAGE,
                ""
        );
    }

    public void eventClickTermsFinalFormPage() {
            if (analyticTracker == null)
                return;

            analyticTracker.sendEventTracking(
                    Event.CLICK_KYC,
                    Category.KYC_PAGE,
                    Action.CLICK_TERMS_AND_CONDITION_FINAL_FORM_PAGE,
                    ""
            );
    }

    public void eventClickUploadPhotos() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_UPLOAD_PHOTOS,
                ""
        );
    }

    public void eventViewPendingPage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_PENDING_PAGE,
                ""
        );
    }

    public void eventClickBackPendingPage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_BACK_PENDING_PAGE,
                ""
        );
    }

    public void eventViewSuccessSnackbarPendingPage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_SUCCESS_SNACKBAR_PENDING_PAGE,
                ""
        );
    }

    public void eventViewRejectedPage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_REJECTED_PAGE,
                ""
        );
    }

    public void eventClickBackRejectedPage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_BACK_REJECTED_PAGE,
                ""
        );
    }

    public void eventClickNextRejectedPage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_NEXT_REJECTED_PAGE,
                ""
        );
    }

    public void eventViewSuccessPage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_SUCCES_PAGE,
                ""
        );
    }

    public void eventClickBackSuccessPage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_BACK_SUCCESS_PAGE,
                ""
        );
    }

    public void eventClickTermsSuccessPage() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_TERMS_AND_CONDITION_SUCCESS_PAGE,
                ""
        );
    }
}
