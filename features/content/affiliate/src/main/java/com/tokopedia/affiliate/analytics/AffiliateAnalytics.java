package com.tokopedia.affiliate.analytics;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.user.session.UserSession;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * @author by yfsx on 05/11/18.
 */
public class AffiliateAnalytics {

    public static final String PARAM_SCREEN_NAME = "screenName";
    public static final String PARAM_EVENT_NAME = "event";
    public static final String PARAM_EVENT_CATEGORY = "eventCategory";
    public static final String PARAM_EVENT_ACTION = "eventAction";
    public static final String PARAM_EVENT_LABEL = "eventLabel";
    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_PRODUCT_ID = "product_id";
    public static final String PARAM_SHOP_ID = "shop_id";




    private AbstractionRouter abstractionRouter;
    private UserSession userSession;

    @Inject
    public AffiliateAnalytics(AbstractionRouter abstractionRouter, UserSession userSession) {
        this.abstractionRouter = abstractionRouter;
        this.userSession = userSession;
    }

    private HashMap<String, Object> setDefaultData(String screenName,
                                                  String event,
                                                  String category,
                                                  String action,
                                                  String label) {
        HashMap<String, Object> mapEvent = new HashMap<>();
        mapEvent.put(PARAM_SCREEN_NAME, screenName);
        mapEvent.put(PARAM_EVENT_NAME, event);
        mapEvent.put(PARAM_EVENT_CATEGORY, category);
        mapEvent.put(PARAM_EVENT_ACTION, action);
        mapEvent.put(PARAM_EVENT_LABEL, label);
        return mapEvent;
    }

    private HashMap<String, Object> setDefaultDataWithUserId(String screenName,
                                                            String event,
                                                            String category,
                                                            String action,
                                                            String label) {
        HashMap<String, Object> mapEvent = setDefaultData(screenName, event, category, action, label);
        mapEvent.put(PARAM_USER_ID, userSession.getUserId());
        return mapEvent;
    }

    public void onByMeButtonClicked(String productId) {
        abstractionRouter.getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_DISCOVERY_PAGE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_DISCOVERY_PAGE,
                        AffiliateEventTracking.Action.CLICK_BYME,
                        productId
                )
        );
    }

    public void onProductImpression(String productId) {
        abstractionRouter.getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_DISCOVERY_PAGE,
                        AffiliateEventTracking.Event.AFFILIATE_VIEW,
                        AffiliateEventTracking.Category.BYME_DISCOVERY_PAGE,
                        AffiliateEventTracking.Action.IMPRESSION_PRODUCTS_AFFILIATE,
                        productId
                )
        );
    }

    public void onProductClicked(String productId) {
        abstractionRouter.getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_DISCOVERY_PAGE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_DISCOVERY_PAGE,
                        AffiliateEventTracking.Action.CLICK_PRODUCTS_AFFILIATE,
                        productId
                )
        );
    }

    public void onSearchSubmitted(String keyword) {
        abstractionRouter.getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_DISCOVERY_PAGE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_DISCOVERY_PAGE,
                        AffiliateEventTracking.Action.SEARCH,
                        keyword
                )
        );
    }

    public void onSearchNotFound(String keyword) {
        abstractionRouter.getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_DISCOVERY_PAGE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_DISCOVERY_PAGE,
                        AffiliateEventTracking.Action.SEARCH_NOT_FOUND,
                        keyword
                )
        );
    }

    public void onCobaSekarangButtonClicked() {
        abstractionRouter.getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CLAIM_TOKOPEDIA,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_ONBOARD,
                        AffiliateEventTracking.Action.CLICK_COBA_SEKARANG,
                        ""
                )
        );
    }

    public void onTentangKomisiButtonClicked() {
        abstractionRouter.getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CLAIM_TOKOPEDIA,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_ONBOARD,
                        AffiliateEventTracking.Action.CLICK_TENTANG_KOMISI,
                        ""
                )
        );
    }

    public void onSimpanButtonClicked() {
        abstractionRouter.getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CLAIM_TOKOPEDIA,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_ONBOARD,
                        AffiliateEventTracking.Action.CLICK_SIMPAN,
                        ""
                )
        );
    }

    public void onSKButtonClicked() {
        abstractionRouter.getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CLAIM_TOKOPEDIA,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_ONBOARD,
                        AffiliateEventTracking.Action.CLICK_SYARAT_KETENTUAN,
                        ""
                )
        );
    }

    public void onLihatContohButtonClicked() {
        abstractionRouter.getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        AffiliateEventTracking.Action.CLICK_LIHAT_CONTOH,
                        ""
                )
        );
    }

    public void onTambahGambarButtonClicked() {
        abstractionRouter.getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        AffiliateEventTracking.Action.CLICK_TAMBAH_GAMBAR,
                        ""
                )
        );
    }
    public void onSelesaiCreateButtonClicked() {
        abstractionRouter.getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        AffiliateEventTracking.Action.CLICK_SELESAI,
                        ""
                )
        );
    }

}
