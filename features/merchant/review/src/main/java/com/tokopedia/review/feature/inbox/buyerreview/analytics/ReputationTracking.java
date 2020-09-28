package com.tokopedia.review.feature.inbox.buyerreview.analytics;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.interfaces.ContextAnalytics;

import java.util.HashMap;

/**
 * Created by zulfikarrahman on 3/13/18.
 */

public class ReputationTracking {

    private ContextAnalytics tracker;

    public ReputationTracking() {
        this.tracker = TrackApp.getInstance().getGTM();
    }

    private String getEventCategory(boolean myShop) {
        if(myShop){
            return ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER;
        }else{
            return ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BRAND;
        }
    }

    private HashMap<String, Object> createEventMap(String event, String category, String action, String label) {
        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put(ReputationTrackingConstant.EVENT, event);
        eventMap.put(ReputationTrackingConstant.EVENT_CATEGORY, category);
        eventMap.put(ReputationTrackingConstant.EVENT_ACTION, action);
        eventMap.put(ReputationTrackingConstant.EVENT_LABEL, label);
        return eventMap;
    }

    public void reviewItemOnClickTracker(String invoice, int position, boolean isFromWhitespace, int tab) {
        String clickSource = isFromWhitespace ? ReputationTrackingConstant.CLICK_ON_WHITESPACE : ReputationTrackingConstant.CLICK_ON_TEXT_REVIEW;
        String tabSource = (tab == 1) ? "menunggu diulas tab" : "ulasan saya tab";
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                ReputationTrackingConstant.CLICK_GIVE_REVIEW_FROM + tabSource,
                invoice+" - "+position+" - "+clickSource
        ));
    }

    public void seeAllReviewItemOnClickTracker(String invoice, int position, boolean isFromWhitespace, int tab) {
        String clickSource = isFromWhitespace ? ReputationTrackingConstant.CLICK_ON_WHITESPACE : ReputationTrackingConstant.CLICK_ON_TEXT_REVIEW;
        String tabSource = (tab == 1) ? "menunggu diulas tab" : "ulasan saya tab";
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                ReputationTrackingConstant.CLICK_SEE_ALL_REVIEW_FROM + tabSource,
                invoice+" - "+position+" - "+clickSource
        ));
    }

    public void onBackPressedInboxReviewClickTracker(int tab) {
        String tabSource = ((tab+1) == 1) ? "menunggu diulas tab" : "ulasan saya tab";
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                ReputationTrackingConstant.BACK_PRESSED_REVIEW + tabSource,
                ""
        ));
    }

    public void onTabReviewSelectedTracker(int tab) {
        String tabSource = ((tab+1) == 1) ? ReputationTrackingConstant.CLICK_WAITING_REVIEW_TAB : ReputationTrackingConstant.CLICK_MY_REVIEW_TAB;
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                tabSource,
                ""
        ));
        String tabSourceView = ((tab+1) == 1) ? ReputationTrackingConstant.VIEW_WAITING_REVIEW_TAB : ReputationTrackingConstant.VIEW_MY_REVIEW_TAB;
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.VIEW_REVIEW,
                ReputationTrackingConstant.REVIEW_PAGE,
                tabSourceView,
                ""
        ));
    }

    public void onClickSearchViewTracker(int tab) {
        String tabSource = (tab == 1) ? "menunggu diulas tab" : "ulasan saya tab";
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                ReputationTrackingConstant.CLICK_REVIEW_SEARCH+tabSource,
                ""
        ));
    }

    public void onSuccessFilteredReputationTracker(String query, int tab) {
        String tabSource = (tab == 1) ? "menunggu diulas tab" : "ulasan saya tab";
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                ReputationTrackingConstant.CLICK_REVIEW_SEARCH+tabSource,
                "success - "+query
        ));
    }

    public void onEmptyFilteredReputationTracker(String query, int tab) {
        String tabSource = (tab == 1) ? "menunggu diulas tab" : "ulasan saya tab";
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                ReputationTrackingConstant.CLICK_REVIEW_SEARCH+tabSource,
                "no result - "+query
        ));
    }

    public void onErrorFilteredReputationTracker(String query, int tab) {
        String tabSource = (tab == 1) ? "menunggu diulas tab" : "ulasan saya tab";
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                ReputationTrackingConstant.CLICK_REVIEW_SEARCH+tabSource,
                "abandon - "+query
        ));
    }

    public void onClickButtonFilterReputationTracker(int tab) {
        String tabSource = (tab == 1) ? "menunggu diulas tab" : "ulasan saya tab";
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                ReputationTrackingConstant.CLICK_FILTER_REVIEW+tabSource,
                ""
        ));
    }

    public void onScrollReviewTracker(int tab) {
        String tabSource = (tab == 1) ? "menunggu diulas tab" : "ulasan saya tab";
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                ReputationTrackingConstant.SCROLL_REVIEW+tabSource,
                ""
        ));
    }

    public void onClickFilterItemTracker(String timeFilter, int tab) {
        String tabSource = (tab == 1) ? "menunggu diulas tab" : "ulasan saya tab";
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.FILTER_INVOICE_PAGE,
                ReputationTrackingConstant.CLICK_SELECT_FILTER_REVIEW + tabSource,
                timeFilter
        ));
    }

    public void onSaveFilterReviewTracker(String timeFilter, int tab) {
        String tabSource = (tab == 1) ? "menunggu diulas tab" : "ulasan saya tab";
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.FILTER_INVOICE_PAGE,
                ReputationTrackingConstant.CLICK_APPLY_FILTER_REVIEW + tabSource,
                timeFilter
        ));
    }

    public void onClickBackButtonFromFilterTracker(int tab) {
        String tabSource = (tab == 1) ? "menunggu diulas tab" : "ulasan saya tab";
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.FILTER_INVOICE_PAGE,
                ReputationTrackingConstant.CLICK_BACK_BUTTON_FILTER_REVIEW + tabSource,
                ""
        ));
    }

    public void onClickResetButtonFilterTracker(int tab) {
        String tabSource = (tab == 1) ? "menunggu diulas tab" : "ulasan saya tab";
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.FILTER_INVOICE_PAGE,
                ReputationTrackingConstant.CLICK_RESET_BUTTON_FILTER_REVIEW + tabSource,
                ""
        ));
    }

    public void onSeeFilterPageTracker(int tab) {
        String tabSource = (tab == 1) ? "menunggu diulas tab" : "ulasan saya tab";
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.VIEW_REVIEW,
                ReputationTrackingConstant.FILTER_INVOICE_PAGE,
                ReputationTrackingConstant.VIEW_FILTER_PAGE+tabSource,
                ""
        ));
    }

    public void onSeeSellerFeedbackPage(String orderId) {
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.VIEW_REVIEW,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.VIEW_SELLER_FEEDBACK,
                orderId
        ));
    }

    public void onClickSmileyShopReviewTracker(String smileyName, String orderId) {
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_SMILEY + smileyName,
                orderId
        ));
    }

    public void onClickBackButtonReputationDetailTracker(String orderId) {
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_BACK_BUTTON_SELLER_FEEDBACK,
                orderId
        ));
    }

    public void onClickReviewOverflowMenuTracker(String orderId, String productId, int adapterPosition) {
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_OVERFLOW_MENU,
                orderId+" - "+productId+" - "+adapterPosition
        ));
    }

    public void onClickShareMenuReviewTracker(String orderId, String productId, int adapterPosition) {
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_OVERFLOW_MENU_SHARE,
                orderId+" - "+productId+" - "+adapterPosition
        ));
    }

    public void onClickToggleReplyReviewTracker(String orderId, String productId, int adapterPosition) {
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_SEE_REPLY_TEXT,
                orderId+" - "+productId+" - "+adapterPosition
        ));
    }

    public void onClickRadioButtonReportAbuse(String reasonSelected) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                ReputationTrackingConstant.EVENT, ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.EVENT_CATEGORY, ReputationTrackingConstant.REVIEW_DETAIL_PAGE,
                ReputationTrackingConstant.EVENT_ACTION, "click - select reason on report abuse",
                ReputationTrackingConstant.EVENT_LABEL, "reason:"+reasonSelected,
                ReputationTrackingConstant.SCREEN_NAME, ReputationTrackingConstant.REVIEW_DETAIL_PAGE_SCREEN
        ));
    }

    public void onSubmitReportAbuse(String feedbackId) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                ReputationTrackingConstant.EVENT, ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.EVENT_CATEGORY, ReputationTrackingConstant.REVIEW_DETAIL_PAGE,
                ReputationTrackingConstant.EVENT_ACTION, "click - kirim button reasons",
                ReputationTrackingConstant.EVENT_LABEL, "feedbackId"+feedbackId,
                ReputationTrackingConstant.SCREEN_NAME, ReputationTrackingConstant.REVIEW_DETAIL_PAGE_SCREEN
        ));
    }

    public void onSuccessGetIncentiveOvoTracker(String message, String category) {
        if(category.isEmpty()) {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.VIEW_REVIEW,
                    ReputationTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReputationTrackingConstant.VIEW_OVO_INCENTIVES_TICKER,
                    ReputationTrackingConstant.MESSAGE + message + ";"
            ));
        } else {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.VIEW_REVIEW,
                    ReputationTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReputationTrackingConstant.VIEW_OVO_INCENTIVES_TICKER,
                    ReputationTrackingConstant.MESSAGE + message + ";"
            ));
        }
    }

    public void onClickReadSkIncentiveOvoTracker(String message, String category) {
        if(category.isEmpty()) {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.CLICK_REVIEW_OLD,
                    ReputationTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReputationTrackingConstant.CLICK_READ_SK_OVO_INCENTIVES_TICKER,
                    ReputationTrackingConstant.MESSAGE + message + ";"
            ));
        } else {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.CLICK_REVIEW_OLD,
                    ReputationTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReputationTrackingConstant.CLICK_READ_SK_OVO_INCENTIVES_TICKER,
                    ReputationTrackingConstant.MESSAGE + message + ";"
            ));
        }
    }

    public void onClickDismissIncentiveOvoTracker(String message, String category) {
        if(category.isEmpty()) {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.CLICK_REVIEW_OLD,
                    ReputationTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReputationTrackingConstant.CLICK_DISMISS_OVO_INCENTIVES_TICKER,
                    ReputationTrackingConstant.MESSAGE + message + ";"
            ));
        } else  {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.CLICK_REVIEW_OLD,
                    ReputationTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReputationTrackingConstant.CLICK_DISMISS_OVO_INCENTIVES_TICKER,
                    ReputationTrackingConstant.MESSAGE + message + ";"
            ));
        }
    }

    public void onClickDismissIncentiveOvoBottomSheetTracker(String category) {
        if(category.isEmpty()) {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.CLICK_REVIEW_OLD,
                    ReputationTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReputationTrackingConstant.CLICK_DISMISS_OVO_INCENTIVES_BOTTOMSHEET,
                    ""
            ));
        } else {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.CLICK_REVIEW_OLD,
                    ReputationTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReputationTrackingConstant.CLICK_DISMISS_OVO_INCENTIVES_BOTTOMSHEET,
                    ""
            ));
        }
    }

    public void onClickContinueIncentiveOvoBottomSheetTracker(String category) {
        if(category.isEmpty()) {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.CLICK_REVIEW_OLD,
                    ReputationTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReputationTrackingConstant.CLICK_CONTINUE_SEND_REVIEW_0N_OVO_INCENTIVES,
                    ""
            ));
        } else {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.CLICK_REVIEW_OLD,
                    ReputationTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReputationTrackingConstant.CLICK_CONTINUE_SEND_REVIEW_0N_OVO_INCENTIVES,
                    ""
            ));
        }
    }

    private String getEditMarker(boolean isEditReview) {
        return isEditReview ? " - edit" : "";
    }
}
