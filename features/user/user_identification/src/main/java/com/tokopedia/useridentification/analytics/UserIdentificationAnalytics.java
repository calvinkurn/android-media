package com.tokopedia.useridentification.analytics;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;


/**
 * @author by alvinatin on 26/11/18.
 */

public class UserIdentificationAnalytics {

    private final int projectID;

    private static class Event {
        private static final String VIEW_KYC = "viewKYC";
        private static final String CLICK_KYC = "clickKYC";
        private static final String VIEW_TRADEIN = "viewTradeIn";
        private static final String CLICK_TRADEIN = "clickTradeIn";
    }

    private static class Action {
        private static final String VIEW_KYC_ONBOARDING = "view on KYC onboarding";
        private static final String CLICK_BACK_ONBOARDING = "click on back KYC onboarding";
        private static final String CLICK_NEXT_ONBOARDING = "click on lanjut kyc onboarding";

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
        private static final String KYC_PAGE_TRADEIN = "kyc trade in page";
    }

    private UserIdentificationAnalytics(int projectID) {
        this.projectID = projectID;
    }

    public static UserIdentificationAnalytics createInstance(int projectID) {
        return new UserIdentificationAnalytics(projectID);
    }

    public void eventViewOnKYCOnBoarding() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_KYC_ONBOARDING,
                ""
        ));
    }

    public void eventClickOnBackOnBoarding() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_BACK_ONBOARDING,
                ""
        ));
    }

    public void eventClickOnNextOnBoarding() {
        if(projectID ==4){
            sendTradeInClickEvent(Action.CLICK_NEXT_ONBOARDING,"");
        } else {
            TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                    Event.CLICK_KYC,
                    Category.KYC_PAGE,
                    Action.CLICK_NEXT_ONBOARDING,
                    ""
            ));
        }
    }

    public void eventViewPendingPage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_PENDING_PAGE,
                ""
        ));
    }

    public void eventClickBackPendingPage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_BACK_PENDING_PAGE,
                ""
        ));
    }

    public void eventViewSuccessSnackbarPendingPage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_SUCCESS_SNACKBAR_PENDING_PAGE,
                ""
        ));
    }

    public void eventViewRejectedPage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_REJECTED_PAGE,
                ""
        ));
    }

    public void eventClickBackRejectedPage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_BACK_REJECTED_PAGE,
                ""
        ));
    }

    public void eventClickNextRejectedPage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_NEXT_REJECTED_PAGE,
                ""
        ));
    }

    public void eventViewSuccessPage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_SUCCES_PAGE,
                ""
        ));
    }

    public void eventClickBackSuccessPage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_BACK_SUCCESS_PAGE,
                ""
        ));
    }

    public void eventClickTermsSuccessPage() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_TERMS_AND_CONDITION_SUCCESS_PAGE,
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
}
