package com.tokopedia.shop.review.analytic

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import java.util.*

/**
 * Created by zulfikarrahman on 3/13/18.
 */
class ReputationTracking {
    private val tracker: ContextAnalytics
    private fun eventShopPageOfficialStore(action: String, label: String?, shopId: String?, myShop: Boolean) {
        val eventMap = createEventMap(ReputationTrackingConstant.CLICK_OFFICIAL_STORE, getEventCategory(myShop),
                action, label)
        eventMap[ReputationTrackingConstant.SHOP_ID] = shopId
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    private fun getEventCategory(myShop: Boolean): String? {
        return if (myShop) {
            ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER
        } else {
            ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BRAND
        }
    }

    private fun eventShopPageOfficialStoreProductId(action: String, label: String, productId: String?, myShop: Boolean) {
        val eventMap = createEventMap(ReputationTrackingConstant.CLICK_OFFICIAL_STORE, getEventCategory(myShop),
                action, label)
        eventMap[ReputationTrackingConstant.PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    private fun createEventMap(event: String?, category: String?, action: String?, label: String?): HashMap<String?, Any?> {
        val eventMap = HashMap<String?, Any?>()
        eventMap[ReputationTrackingConstant.EVENT] = event
        eventMap[ReputationTrackingConstant.EVENT_CATEGORY] = category
        eventMap[ReputationTrackingConstant.EVENT_ACTION] = action
        eventMap[ReputationTrackingConstant.EVENT_LABEL] = label
        return eventMap
    }

    fun eventClickLikeDislikeReview(titlePage: String, status: Boolean, position: Int, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(
                titlePage + ReputationTrackingConstant.TOP_CONTENT_CLICK,
                ReputationTrackingConstant.CLICK_REVIEW + (if (status) ReputationTrackingConstant.NEUTRAL else ReputationTrackingConstant.HELPING) + "-" + (position + 1).toString(),
                shopId, myShop)
    }

    fun eventClickProductPictureOrName(titlePage: String, position: Int, productId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(
                titlePage + ReputationTrackingConstant.TOP_CONTENT_CLICK,
                ReputationTrackingConstant.CLICK_PRODUCT_PICTURE_OR_NAME + (position + 1).toString(),
                productId, myShop)
    }

    fun eventClickUserAccount(titlePage: String, position: Int, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(
                titlePage + ReputationTrackingConstant.TOP_CONTENT_CLICK,
                ReputationTrackingConstant.CLICK_USER_ACCOUNT + (position + 1).toString(),
                shopId, myShop)
    }

    fun eventClickUserAccountPage(titlePage: String, position: Int, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(
                titlePage + ReputationTrackingConstant.TOP_CONTENT_REVIEW_PAGE_CLICK,
                ReputationTrackingConstant.CLICK_USER_ACCOUNT + (position + 1).toString(),
                shopId, myShop)
    }

    fun eventCLickThreeDotMenu(titlePage: String, position: Int, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(
                titlePage + ReputationTrackingConstant.TOP_CONTENT_CLICK,
                ReputationTrackingConstant.CLICK_THREE_DOTTED + (position + 1).toString(),
                shopId, myShop)
    }

    fun eventClickChooseThreeDotMenu(titlePage: String, position: Int, typeAction: String?, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(
                titlePage + ReputationTrackingConstant.TOP_CONTENT_THREE_DOTTED_CLICK,
                ReputationTrackingConstant.CLICK_THREE_DOTTED + (position + 1).toString(),
                shopId, myShop)
    }

    fun eventClickSeeReplies(titlePage: String, position: Int, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(
                titlePage + ReputationTrackingConstant.TOP_CONTENT_CLICK,
                ReputationTrackingConstant.CLICK_SEE_REPLIES + (position + 1).toString(),
                shopId, myShop)
    }

    fun eventClickLikeDislikeReviewPage(titlePage: String?, status: Boolean, position: Int, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_REVIEW + (if (status) ReputationTrackingConstant.NEUTRAL else ReputationTrackingConstant.HELPING) + "-" + (position + 1).toString(),
                shopId, myShop)
    }

    fun eventClickProductPictureOrNamePage(titlePage: String?, position: Int, productId: String?, myShop: Boolean) {
        eventShopPageOfficialStoreProductId(String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_PRODUCT_PICTURE_OR_NAME + (position + 1).toString(),
                productId, myShop)
    }

    fun eventCLickThreeDotMenuPage(titlePage: String?, position: Int, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_THREE_DOTTED + (position + 1).toString(),
                shopId, myShop)
    }

    fun eventClickChooseThreeDotMenuPage(titlePage: String?, position: Int, report: String?, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_DOTTED_MENU_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_THREE_DOTTED + (position + 1).toString(),
                shopId, myShop)
    }

    fun eventClickSeeRepliesPage(titlePage: String?, position: Int, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_DOTTED_MENU_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_SEE_REPLIES + (position + 1).toString(),
                shopId, myShop)
    }

    fun eventClickSeeMoreReview(titlePage: String, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(
                titlePage + ReputationTrackingConstant.BOTTOM_NAVIGATION_CLICK,
                ReputationTrackingConstant.CLICK_SEE_MORE,
                shopId, myShop)
    }

    fun reviewOnViewTracker(orderId: String, productId: String) {
        tracker.sendGeneralEvent(createEventMap(
                "viewReviewIris",
                "product review detail page",
                "view - product review detail page",
                "$orderId - $productId"
        ))
    }

    fun reviewOnCloseTracker(orderId: String, productId: String) {
        tracker.sendGeneralEvent(createEventMap(
                "clickReview",
                "product review detail page",
                "click - back button on product review detail page",
                "$orderId - $productId"
        ))
    }

    fun reviewOnRatingChangedTracker(
            orderId: String,
            productId: String,
            ratingValue: String,
            isSuccessful: Boolean,
            isEditReview: Boolean
    ) {
        val successState = if (isSuccessful) "success" else "unsuccessful"
        tracker.sendGeneralEvent(createEventMap(
                "clickReview",
                "product review detail page" + getEditMarker(isEditReview),
                "click - product star rating - $ratingValue",
                "$orderId - $productId - $successState"
        ))
    }

    fun reviewOnMessageChangedTracker(
            orderId: String,
            productId: String,
            isMessageEmpty: Boolean,
            isEditReview: Boolean
    ) {
        val messageState = if (isMessageEmpty) "blank" else "filled"
        tracker.sendGeneralEvent(createEventMap(
                "clickReview",
                "product review detail page" + getEditMarker(isEditReview),
                "click - ulasan product description",
                "$orderId - $productId - $messageState"
        ))
    }

    fun reviewOnImageUploadTracker(
            orderId: String,
            productId: String,
            isSuccessful: Boolean,
            imageNum: String,
            isEditReview: Boolean
    ) {
        val successState = if (isSuccessful) "success" else "unsuccessful"
        tracker.sendGeneralEvent(createEventMap(
                "clickReview",
                "product review detail page" + getEditMarker(isEditReview),
                "click - upload gambar produk",
                "$orderId - $productId - $successState - $imageNum"
        ))
    }

    fun reviewOnAnonymousClickTracker(
            orderId: String,
            productId: String,
            isEditReview: Boolean
    ) {
        tracker.sendGeneralEvent(createEventMap(
                "clickReview",
                "product review detail page" + getEditMarker(isEditReview),
                "click - anonim on ulasan produk",
                "$orderId - $productId"
        ))
    }

    fun reviewOnSubmitTracker(
            orderId: String,
            productId: String,
            ratingValue: String,
            isMessageEmpty: Boolean,
            imageNum: String,
            isAnonymous: Boolean,
            isEditReview: Boolean
    ) {
        val messageState = if (isMessageEmpty) "blank" else "filled"
        val anonymousState = if (isAnonymous) "true" else "false"
        tracker.sendGeneralEvent(createEventMap(
                "clickReview",
                "product review detail page" + getEditMarker(isEditReview),
                "click - kirim ulasan produk",
                "order_id : " + orderId +
                        " - product_id : " + productId +
                        " - star : " + ratingValue +
                        " - ulasan : " + messageState +
                        " - gambar : " + imageNum +
                        " - anonim : " + anonymousState
        ))
    }

    fun eventImageClickOnReview(productId: String?,
                                reviewId: String?) {
        val KEY_PRODUCT_ID = "productId"
        val mapEvent = TrackAppUtils.gtmData(
                ReputationTrackingConstant.CLICK_PDP,
                ReputationTrackingConstant.PRODUCT_DETAIL_PAGE,
                "click - review gallery on rating list", String.format(
                "product_id: %s - review_id : %s",
                productId,
                reviewId
        ))
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventClickFilterReview(filterName: String,
                               productId: String) {
        val KEY_PRODUCT_ID = "productId"
        val mapEvent = TrackAppUtils.gtmData(
                ReputationTrackingConstant.CLICK_PDP,
                ReputationTrackingConstant.PRODUCT_DETAIL_PAGE, String.format(
                "click - filter review by %s",
                filterName.toLowerCase()
        ),
                productId
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun reviewItemOnClickTracker(invoice: String, position: Int, isFromWhitespace: Boolean, tab: Int) {
        val clickSource = if (isFromWhitespace) ReputationTrackingConstant.CLICK_ON_WHITESPACE else ReputationTrackingConstant.CLICK_ON_TEXT_REVIEW
        val tabSource = if (tab == 1) "menunggu diulas tab" else "ulasan saya tab"
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                ReputationTrackingConstant.CLICK_GIVE_REVIEW_FROM + tabSource,
                "$invoice - $position - $clickSource"
        ))
    }

    fun seeAllReviewItemOnClickTracker(invoice: String, position: Int, isFromWhitespace: Boolean, tab: Int) {
        val clickSource = if (isFromWhitespace) ReputationTrackingConstant.CLICK_ON_WHITESPACE else ReputationTrackingConstant.CLICK_ON_TEXT_REVIEW
        val tabSource = if (tab == 1) "menunggu diulas tab" else "ulasan saya tab"
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                ReputationTrackingConstant.CLICK_SEE_ALL_REVIEW_FROM + tabSource,
                "$invoice - $position - $clickSource"
        ))
    }

    fun onBackPressedInboxReviewClickTracker(tab: Int) {
        val tabSource = if (tab + 1 == 1) "menunggu diulas tab" else "ulasan saya tab"
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                ReputationTrackingConstant.BACK_PRESSED_REVIEW + tabSource,
                ""
        ))
    }

    fun onTabReviewSelectedTracker(tab: Int) {
        val tabSource = if (tab + 1 == 1) ReputationTrackingConstant.CLICK_WAITING_REVIEW_TAB else ReputationTrackingConstant.CLICK_MY_REVIEW_TAB
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                tabSource,
                ""
        ))
        val tabSourceView = if (tab + 1 == 1) ReputationTrackingConstant.VIEW_WAITING_REVIEW_TAB else ReputationTrackingConstant.VIEW_MY_REVIEW_TAB
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.VIEW_REVIEW,
                ReputationTrackingConstant.REVIEW_PAGE,
                tabSourceView,
                ""
        ))
    }

    fun onClickSearchViewTracker(tab: Int) {
        val tabSource = if (tab == 1) "menunggu diulas tab" else "ulasan saya tab"
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                ReputationTrackingConstant.CLICK_REVIEW_SEARCH + tabSource,
                ""
        ))
    }

    fun onSuccessFilteredReputationTracker(query: String, tab: Int) {
        val tabSource = if (tab == 1) "menunggu diulas tab" else "ulasan saya tab"
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                ReputationTrackingConstant.CLICK_REVIEW_SEARCH + tabSource,
                "success - $query"
        ))
    }

    fun onEmptyFilteredReputationTracker(query: String, tab: Int) {
        val tabSource = if (tab == 1) "menunggu diulas tab" else "ulasan saya tab"
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                ReputationTrackingConstant.CLICK_REVIEW_SEARCH + tabSource,
                "no result - $query"
        ))
    }

    fun onErrorFilteredReputationTracker(query: String, tab: Int) {
        val tabSource = if (tab == 1) "menunggu diulas tab" else "ulasan saya tab"
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                ReputationTrackingConstant.CLICK_REVIEW_SEARCH + tabSource,
                "abandon - $query"
        ))
    }

    fun onClickButtonFilterReputationTracker(tab: Int) {
        val tabSource = if (tab == 1) "menunggu diulas tab" else "ulasan saya tab"
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                ReputationTrackingConstant.CLICK_FILTER_REVIEW + tabSource,
                ""
        ))
    }

    fun onScrollReviewTracker(tab: Int) {
        val tabSource = if (tab == 1) "menunggu diulas tab" else "ulasan saya tab"
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.REVIEW_PAGE,
                ReputationTrackingConstant.SCROLL_REVIEW + tabSource,
                ""
        ))
    }

    fun onClickFilterItemTracker(timeFilter: String?, tab: Int) {
        val tabSource = if (tab == 1) "menunggu diulas tab" else "ulasan saya tab"
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.FILTER_INVOICE_PAGE,
                ReputationTrackingConstant.CLICK_SELECT_FILTER_REVIEW + tabSource,
                timeFilter
        ))
    }

    fun onSaveFilterReviewTracker(timeFilter: String?, tab: Int) {
        val tabSource = if (tab == 1) "menunggu diulas tab" else "ulasan saya tab"
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.FILTER_INVOICE_PAGE,
                ReputationTrackingConstant.CLICK_APPLY_FILTER_REVIEW + tabSource,
                timeFilter
        ))
    }

    fun onClickBackButtonFromFilterTracker(tab: Int) {
        val tabSource = if (tab == 1) "menunggu diulas tab" else "ulasan saya tab"
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.FILTER_INVOICE_PAGE,
                ReputationTrackingConstant.CLICK_BACK_BUTTON_FILTER_REVIEW + tabSource,
                ""
        ))
    }

    fun onClickResetButtonFilterTracker(tab: Int) {
        val tabSource = if (tab == 1) "menunggu diulas tab" else "ulasan saya tab"
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.FILTER_INVOICE_PAGE,
                ReputationTrackingConstant.CLICK_RESET_BUTTON_FILTER_REVIEW + tabSource,
                ""
        ))
    }

    fun onSeeFilterPageTracker(tab: Int) {
        val tabSource = if (tab == 1) "menunggu diulas tab" else "ulasan saya tab"
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.VIEW_REVIEW,
                ReputationTrackingConstant.FILTER_INVOICE_PAGE,
                ReputationTrackingConstant.VIEW_FILTER_PAGE + tabSource,
                ""
        ))
    }

    fun onSeeSellerFeedbackPage(orderId: String?) {
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.VIEW_REVIEW,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.VIEW_SELLER_FEEDBACK,
                orderId
        ))
    }

    fun onClickSmileyShopReviewTracker(smileyName: String, orderId: String?) {
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_SMILEY + smileyName,
                orderId
        ))
    }

    fun onClickFollowShopButton(shopId: Int, orderId: String?) {
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_FOLLOW_BUTTON + shopId,
                orderId
        ))
    }

    fun onClickGiveReviewTracker(productId: String, orderId: String, adapterPosition: Int) {
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_GIVE_REVIEW,
                "$orderId - $productId - $adapterPosition"
        ))
    }

    fun onClickBackButtonReputationDetailTracker(orderId: String?) {
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_BACK_BUTTON_SELLER_FEEDBACK,
                orderId
        ))
    }

    fun onClickReviewOverflowMenuTracker(orderId: String, productId: String, adapterPosition: Int) {
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_OVERFLOW_MENU,
                "$orderId - $productId - $adapterPosition"
        ))
    }

    fun onClickEditReviewMenuTracker(orderId: String, productId: String, adapterPosition: Int) {
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_OVERFLOW_MENU_EDIT,
                "$orderId - $productId - $adapterPosition"
        ))
    }

    fun onClickShareMenuReviewTracker(orderId: String, productId: String, adapterPosition: Int) {
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_OVERFLOW_MENU_SHARE,
                "$orderId - $productId - $adapterPosition"
        ))
    }

    fun onClickToggleReplyReviewTracker(orderId: String, productId: String, adapterPosition: Int) {
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_SEE_REPLY_TEXT,
                "$orderId - $productId - $adapterPosition"
        ))
    }

    fun onSuccessGetIncentiveOvoTracker(message: String, category: String) {
        if (category.isEmpty()) {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.VIEW_REVIEW,
                    ReputationTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReputationTrackingConstant.VIEW_OVO_INCENTIVES_TICKER,
                    ReputationTrackingConstant.MESSAGE + message + ";"
            ))
        } else {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.VIEW_REVIEW,
                    ReputationTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReputationTrackingConstant.VIEW_OVO_INCENTIVES_TICKER,
                    ReputationTrackingConstant.MESSAGE + message + ";"
            ))
        }
    }

    fun onClickReadSkIncentiveOvoTracker(message: String, category: String) {
        if (category.isEmpty()) {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.CLICK_REVIEW_OLD,
                    ReputationTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReputationTrackingConstant.CLICK_READ_SK_OVO_INCENTIVES_TICKER,
                    ReputationTrackingConstant.MESSAGE + message + ";"
            ))
        } else {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.CLICK_REVIEW_OLD,
                    ReputationTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReputationTrackingConstant.CLICK_READ_SK_OVO_INCENTIVES_TICKER,
                    ReputationTrackingConstant.MESSAGE + message + ";"
            ))
        }
    }

    fun onClickDismissIncentiveOvoTracker(message: String, category: String) {
        if (category.isEmpty()) {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.CLICK_REVIEW_OLD,
                    ReputationTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReputationTrackingConstant.CLICK_DISMISS_OVO_INCENTIVES_TICKER,
                    ReputationTrackingConstant.MESSAGE + message + ";"
            ))
        } else {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.CLICK_REVIEW_OLD,
                    ReputationTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReputationTrackingConstant.CLICK_DISMISS_OVO_INCENTIVES_TICKER,
                    ReputationTrackingConstant.MESSAGE + message + ";"
            ))
        }
    }

    fun onClickDismissIncentiveOvoBottomSheetTracker(category: String) {
        if (category.isEmpty()) {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.CLICK_REVIEW_OLD,
                    ReputationTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReputationTrackingConstant.CLICK_DISMISS_OVO_INCENTIVES_BOTTOMSHEET,
                    ""
            ))
        } else {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.CLICK_REVIEW_OLD,
                    ReputationTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReputationTrackingConstant.CLICK_DISMISS_OVO_INCENTIVES_BOTTOMSHEET,
                    ""
            ))
        }
    }

    fun onClickContinueIncentiveOvoBottomSheetTracker(category: String) {
        if (category.isEmpty()) {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.CLICK_REVIEW_OLD,
                    ReputationTrackingConstant.REVIEW_DETAIL_PAGE,
                    ReputationTrackingConstant.CLICK_CONTINUE_SEND_REVIEW_0N_OVO_INCENTIVES,
                    ""
            ))
        } else {
            tracker.sendGeneralEvent(createEventMap(
                    ReputationTrackingConstant.CLICK_REVIEW_OLD,
                    ReputationTrackingConstant.REVIEW_PAGE + " - " + category,
                    ReputationTrackingConstant.CLICK_CONTINUE_SEND_REVIEW_0N_OVO_INCENTIVES,
                    ""
            ))
        }
    }

    fun onClickRadioButtonReportAbuse(reasonSelected: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                ReputationTrackingConstant.EVENT, ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.EVENT_CATEGORY, ReputationTrackingConstant.REVIEW_DETAIL_PAGE,
                ReputationTrackingConstant.EVENT_ACTION, "click - select reason on report abuse",
                ReputationTrackingConstant.EVENT_LABEL, "reason:$reasonSelected",
                ReputationTrackingConstant.SCREEN_NAME, ReputationTrackingConstant.REVIEW_DETAIL_PAGE_SCREEN
        ))
    }

    fun onSubmitReportAbuse(feedbackId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                ReputationTrackingConstant.EVENT, ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.EVENT_CATEGORY, ReputationTrackingConstant.REVIEW_DETAIL_PAGE,
                ReputationTrackingConstant.EVENT_ACTION, "click - kirim button reasons",
                ReputationTrackingConstant.EVENT_LABEL, "feedbackId$feedbackId",
                ReputationTrackingConstant.SCREEN_NAME, ReputationTrackingConstant.REVIEW_DETAIL_PAGE_SCREEN
        ))
    }

    private fun getEditMarker(isEditReview: Boolean): String {
        return if (isEditReview) " - edit" else ""
    }

    init {
        tracker = TrackApp.getInstance().gtm
    }
}