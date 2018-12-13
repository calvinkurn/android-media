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
        private static final String CLICK_NEXT_ONBOARDING = "click on lanjut kyc onboarding";
        private static final String VIEW_KTP_PAGE = "view on panduan KTP KYC";
        private static final String CLICK_NEXT_KTP_PAGE = "click on ambil foto KTP";
        private static final String VIEW_SELFIE_PAGE = "view on panduan selfie KTP";
        private static final String CLICK_NEXT_SELFIE_PAGE = "click on ambil foto diri bersama KTP";
        private static final String VIEW_FINAL_FORM_PAGE = "view on preview KTP and selfie";
        private static final String CLICK_UPLOAD_PHOTOS = "click on unggah foto KTP";
        private static final String VIEW_PENDING_PAGE = "view on menunggu verifikasi";
        private static final String VIEW_REJECTED_PAGE = "view on gagal verfikasi";
        private static final String CLICK_NEXT_REJECTED_PAGE = "click on unggah ulang gagal verifikasi";
        private static final String VIEW_SUCCES_PAGE = "view on success terverifikasi";
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
}
