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

    private HashMap<String, Object> createEventMap(String event, String category, String action, String label) {
        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put(ReputationTrackingConstant.EVENT, event);
        eventMap.put(ReputationTrackingConstant.EVENT_CATEGORY, category);
        eventMap.put(ReputationTrackingConstant.EVENT_ACTION, action);
        eventMap.put(ReputationTrackingConstant.EVENT_LABEL, label);
        return eventMap;
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
}
