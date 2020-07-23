package com.tokopedia.tkpd.tkpdreputation.analytic;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.ContextAnalytics;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zulfikarrahman on 3/13/18.
 */

public class ReputationTracking {

    private ContextAnalytics tracker;

    public ReputationTracking() {
        this.tracker = TrackApp.getInstance().getGTM();
    }

    private void eventShopPageOfficialStore(String action, String label, String shopId, boolean myShop){
        HashMap<String, Object> eventMap = createEventMap(ReputationTrackingConstant.CLICK_OFFICIAL_STORE, getEventCategory(myShop),
                action, label);
        eventMap.put(ReputationTrackingConstant.SHOP_ID, shopId);
        TrackApp.getInstance().getGTM().sendGeneralEvent(eventMap);
    }

    private String getEventCategory(boolean myShop) {
        if(myShop){
            return ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER;
        }else{
            return ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BRAND;
        }
    }

    private void eventShopPageOfficialStoreProductId( String action, String label, String productId, boolean myShop){
        HashMap<String, Object> eventMap = createEventMap(ReputationTrackingConstant.CLICK_OFFICIAL_STORE, getEventCategory(myShop),
                action, label);
        eventMap.put(ReputationTrackingConstant.PRODUCT_ID, productId);
        TrackApp.getInstance().getGTM().sendGeneralEvent(eventMap);
    }

    private HashMap<String, Object> createEventMap(String event, String category, String action, String label) {
        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put(ReputationTrackingConstant.EVENT, event);
        eventMap.put(ReputationTrackingConstant.EVENT_CATEGORY, category);
        eventMap.put(ReputationTrackingConstant.EVENT_ACTION, action);
        eventMap.put(ReputationTrackingConstant.EVENT_LABEL, label);
        return eventMap;
    }

    public void eventClickLikeDislikeReview(String titlePage, boolean status, int position, String shopId, boolean myShop) {
        eventShopPageOfficialStore(
                titlePage + ReputationTrackingConstant.TOP_CONTENT_CLICK,
                ReputationTrackingConstant.CLICK_REVIEW + (status ? ReputationTrackingConstant.NEUTRAL : ReputationTrackingConstant.HELPING) + "-" + String.valueOf(position +1),
                shopId, myShop);
    }

    public void eventClickProductPictureOrName(String titlePage, int position, String productId, boolean myShop) {
        eventShopPageOfficialStore(
                titlePage + ReputationTrackingConstant.TOP_CONTENT_CLICK,
                ReputationTrackingConstant.CLICK_PRODUCT_PICTURE_OR_NAME +  String.valueOf(position+1),
                productId, myShop);
    }

    public void eventClickUserAccount(String titlePage, int position, String shopId, boolean myShop) {
        eventShopPageOfficialStore(
                titlePage + ReputationTrackingConstant.TOP_CONTENT_CLICK,
                ReputationTrackingConstant.CLICK_USER_ACCOUNT +  String.valueOf(position+1),
                shopId, myShop);
    }

    public void eventClickUserAccountPage(String titlePage, int position, String shopId, boolean myShop) {
        eventShopPageOfficialStore(
                titlePage + ReputationTrackingConstant.TOP_CONTENT_REVIEW_PAGE_CLICK,
                ReputationTrackingConstant.CLICK_USER_ACCOUNT +  String.valueOf(position+1),
                shopId, myShop);
    }

    public void eventCLickThreeDotMenu(String titlePage, int position, String shopId, boolean myShop) {
        eventShopPageOfficialStore(
                titlePage + ReputationTrackingConstant.TOP_CONTENT_CLICK,
                ReputationTrackingConstant.CLICK_THREE_DOTTED +  String.valueOf(position+1),
                shopId, myShop);
    }

    public void eventClickChooseThreeDotMenu(String titlePage, int position, String typeAction, String shopId, boolean myShop) {
        eventShopPageOfficialStore(
                titlePage + ReputationTrackingConstant.TOP_CONTENT_THREE_DOTTED_CLICK,
                ReputationTrackingConstant.CLICK_THREE_DOTTED +  String.valueOf(position+1),
                shopId, myShop);
    }

    public void eventClickSeeReplies(String titlePage, int position, String shopId, boolean myShop) {
        eventShopPageOfficialStore(
                titlePage + ReputationTrackingConstant.TOP_CONTENT_CLICK,
                ReputationTrackingConstant.CLICK_SEE_REPLIES +  String.valueOf(position+1),
                shopId, myShop);
    }

    public void eventClickLikeDislikeReviewPage(String titlePage, boolean status, int position, String shopId, boolean myShop) {
        eventShopPageOfficialStore(
                String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_REVIEW + (status ? ReputationTrackingConstant.NEUTRAL : ReputationTrackingConstant.HELPING) + "-" + String.valueOf(position+1),
                shopId, myShop);
    }

    public void eventClickProductPictureOrNamePage(String titlePage, int position, String productId, boolean myShop) {
        eventShopPageOfficialStoreProductId(
                String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_PRODUCT_PICTURE_OR_NAME +  String.valueOf(position+1),
                productId, myShop);
    }

    public void eventCLickThreeDotMenuPage(String titlePage, int position, String shopId, boolean myShop) {
        eventShopPageOfficialStore(
                String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_THREE_DOTTED +  String.valueOf(position+1),
                shopId, myShop);
    }

    public void eventClickChooseThreeDotMenuPage(String titlePage, int position, String report, String shopId, boolean myShop) {
        eventShopPageOfficialStore(
                String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_DOTTED_MENU_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_THREE_DOTTED +  String.valueOf(position+1),
                shopId, myShop);
    }

    public void eventClickSeeRepliesPage(String titlePage, int position, String shopId, boolean myShop) {
        eventShopPageOfficialStore(
                String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_DOTTED_MENU_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_SEE_REPLIES +  String.valueOf(position+1),
                shopId, myShop);
    }

    public void eventClickSeeMoreReview(String titlePage, String shopId, boolean myShop) {
        eventShopPageOfficialStore(
                titlePage + ReputationTrackingConstant.BOTTOM_NAVIGATION_CLICK,
                ReputationTrackingConstant.CLICK_SEE_MORE,
                shopId, myShop);
    }

    public void reviewOnViewTracker(String orderId, String productId) {
        tracker.sendGeneralEvent(createEventMap(
                "viewReviewIris",
                "product review detail page",
                "view - product review detail page",
                orderId + " - " + productId
        ));
    }

    public void reviewOnCloseTracker(String orderId, String productId) {
        tracker.sendGeneralEvent(createEventMap(
                "clickReview",
                "product review detail page",
                "click - back button on product review detail page",
                orderId + " - " + productId
        ));
    }

    public void reviewOnRatingChangedTracker(
            String orderId,
            String productId,
            String ratingValue,
            boolean isSuccessful,
            boolean isEditReview
    ) {
        String successState = isSuccessful ? "success" : "unsuccessful";

        tracker.sendGeneralEvent(createEventMap(
                "clickReview",
                "product review detail page" + getEditMarker(isEditReview),
                "click - product star rating - " + ratingValue,
                orderId + " - " + productId + " - " + successState
        ));
    }

    public void reviewOnMessageChangedTracker(
            String orderId,
            String productId,
            boolean isMessageEmpty,
            boolean isEditReview
    ) {
        String messageState = isMessageEmpty ? "blank" : "filled";

        tracker.sendGeneralEvent(createEventMap(
                "clickReview",
                "product review detail page" + getEditMarker(isEditReview),
                "click - ulasan product description",
                orderId + " - " + productId + " - " + messageState
        ));
    }

    public void reviewOnImageUploadTracker(
            String orderId,
            String productId,
            boolean isSuccessful,
            String imageNum,
            boolean isEditReview
    ) {
        String successState = isSuccessful ? "success" : "unsuccessful";

        tracker.sendGeneralEvent(createEventMap(
                "clickReview",
                "product review detail page" + getEditMarker(isEditReview),
                "click - upload gambar produk",
                orderId + " - " + productId + " - " + successState + " - " + imageNum
        ));
    }

    public void reviewOnAnonymousClickTracker(
            String orderId,
            String productId,
            boolean isEditReview
    ) {
        tracker.sendGeneralEvent(createEventMap(
                "clickReview",
                "product review detail page" + getEditMarker(isEditReview),
                "click - anonim on ulasan produk",
                orderId + " - " + productId
        ));
    }

    public void reviewOnSubmitTracker(
            String orderId,
            String productId,
            String ratingValue,
            boolean isMessageEmpty,
            String imageNum,
            boolean isAnonymous,
            boolean isEditReview
    ) {
        String messageState = isMessageEmpty ? "blank" : "filled";
        String anonymousState = isAnonymous ? "true" : "false";

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
        ));
    }

    public void eventImageClickOnReview(String productId,
                                        String reviewId) {

        String KEY_PRODUCT_ID = "productId";
        Map<String, Object> mapEvent = TrackAppUtils.gtmData(
                ReputationTrackingConstant.CLICK_PDP,
                ReputationTrackingConstant.PRODUCT_DETAIL_PAGE,
                "click - review gallery on rating list",
                String.format(
                        "product_id: %s - review_id : %s",
                        productId,
                        reviewId
                )
        );
        mapEvent.put(KEY_PRODUCT_ID, productId);
        TrackApp.getInstance().getGTM().sendGeneralEvent(mapEvent);
    }

    public void eventClickFilterReview(String filterName,
                                       String productId) {
        String KEY_PRODUCT_ID = "productId";
        Map<String, Object> mapEvent = TrackAppUtils.gtmData(
                ReputationTrackingConstant.CLICK_PDP,
                ReputationTrackingConstant.PRODUCT_DETAIL_PAGE,
                String.format(
                        "click - filter review by %s",
                        filterName.toLowerCase()
                ),
                productId
        );
        mapEvent.put(KEY_PRODUCT_ID, productId);
        TrackApp.getInstance().getGTM().sendGeneralEvent(mapEvent);
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

    public void onClickFollowShopButton(int shopId, String orderId) {
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_FOLLOW_BUTTON + shopId,
                orderId
        ));
    }

    public void onClickGiveReviewTracker(String productId, String orderId, int adapterPosition) {
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_GIVE_REVIEW,
                orderId+" - "+productId+" - "+adapterPosition
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

    public void onClickEditReviewMenuTracker(String orderId, String productId, int adapterPosition) {
        tracker.sendGeneralEvent(createEventMap(
                ReputationTrackingConstant.CLICK_REVIEW_OLD,
                ReputationTrackingConstant.SELLER_FEEDBACK_PAGE,
                ReputationTrackingConstant.CLICK_OVERFLOW_MENU_EDIT,
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

    private String getEditMarker(boolean isEditReview) {
        return isEditReview ? " - edit" : "";
    }
}
