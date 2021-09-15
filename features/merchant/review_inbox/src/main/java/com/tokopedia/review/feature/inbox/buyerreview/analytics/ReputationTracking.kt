package com.tokopedia.review.feature.inbox.buyerreview.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
import java.util.*
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 3/13/18.
 */
class ReputationTracking @Inject constructor() {

    fun onSeeSellerFeedbackPage(orderId: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReputationTrackingConstant.VIEW_REVIEW,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.VIEW_SELLER_FEEDBACK,
                orderId
            )
        )
    }

    fun onClickSmileyShopReviewTracker(smileyName: String?, orderId: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_SMILEY + smileyName,
                orderId
            )
        )
    }

    fun onClickBackButtonReputationDetailTracker(orderId: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_BACK_BUTTON_SELLER_FEEDBACK,
                orderId
            )
        )
    }

    fun onClickReviewOverflowMenuTracker(
        orderId: String?,
        productId: String?,
        adapterPosition: Int
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_OVERFLOW_MENU,
                "$orderId - $productId - $adapterPosition"
            )
        )
    }

    fun onClickShareMenuReviewTracker(orderId: String?, productId: String?, adapterPosition: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_OVERFLOW_MENU_SHARE,
                "$orderId - $productId - $adapterPosition"
            )
        )
    }

    fun onClickToggleReplyReviewTracker(
        orderId: String?,
        productId: String?,
        adapterPosition: Int
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_SEE_REPLY_TEXT,
                "$orderId - $productId - $adapterPosition"
            )
        )
    }

    fun onClickRadioButtonReportAbuse(reasonSelected: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                ReputationTrackingConstant.EVENT,
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.EVENT_CATEGORY,
                ReputationTrackingConstant.REVIEW_DETAIL_PAGE,
                ReputationTrackingConstant.EVENT_ACTION,
                "click - select reason on report abuse",
                ReputationTrackingConstant.EVENT_LABEL,
                "reason:$reasonSelected",
                ReputationTrackingConstant.SCREEN_NAME,
                ReputationTrackingConstant.REVIEW_DETAIL_PAGE_SCREEN
            )
        )
    }

    fun onSubmitReportAbuse(feedbackId: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                ReputationTrackingConstant.EVENT,
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.EVENT_CATEGORY,
                ReputationTrackingConstant.REVIEW_DETAIL_PAGE,
                ReputationTrackingConstant.EVENT_ACTION,
                "click - kirim button reasons",
                ReputationTrackingConstant.EVENT_LABEL,
                "feedbackId$feedbackId",
                ReputationTrackingConstant.SCREEN_NAME,
                ReputationTrackingConstant.REVIEW_DETAIL_PAGE_SCREEN
            )
        )
    }

    fun onSuccessGetIncentiveOvoTracker(message: String, category: String) {
        if (category.isEmpty()) {
            TrackApp.getInstance().gtm.sendGeneralEvent(
                createEventMap(
                    ReputationTrackingConstant.VIEW_REVIEW,
                    ReputationTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReputationTrackingConstant.VIEW_OVO_INCENTIVES_TICKER,
                    ReputationTrackingConstant.MESSAGE + message + ";"
                )
            )
        } else {
            TrackApp.getInstance().gtm.sendGeneralEvent(
                createEventMap(
                    ReputationTrackingConstant.VIEW_REVIEW,
                    ReputationTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReputationTrackingConstant.VIEW_OVO_INCENTIVES_TICKER,
                    ReputationTrackingConstant.MESSAGE + message + ";"
                )
            )
        }
    }

    private fun createEventMap(
        event: String?,
        category: String?,
        action: String?,
        label: String?
    ): HashMap<String?, Any?> {
        val eventMap: HashMap<String?, Any?> = HashMap()
        eventMap[ReputationTrackingConstant.EVENT] = event
        eventMap[ReputationTrackingConstant.EVENT_CATEGORY] = category
        eventMap[ReputationTrackingConstant.EVENT_ACTION] = action
        eventMap[ReputationTrackingConstant.EVENT_LABEL] = label
        return eventMap
    }
}