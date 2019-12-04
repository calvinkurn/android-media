package com.tokopedia.tkpd.tkpdreputation.analytic;

import com.tokopedia.tkpd.tkpdreputation.ReputationRouter;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.interfaces.ContextAnalytics;

import java.util.HashMap;

/**
 * Created by zulfikarrahman on 3/13/18.
 */

public class ReputationTracking {

    private ReputationRouter reputationRouter;
    private ContextAnalytics tracker;

    public ReputationTracking(ReputationRouter reputationRouter) {
        this.reputationRouter = reputationRouter;
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



    private String getEditMarker(boolean isEditReview) {
        return isEditReview ? " - edit" : "";
    }
}
