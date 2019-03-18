package com.tokopedia.affiliate.analytics;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.interfaces.ContextAnalytics;
import com.tokopedia.user.session.UserSessionInterface;

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
    private UserSessionInterface userSession;

    @Inject
    public AffiliateAnalytics(AbstractionRouter abstractionRouter,
                              UserSessionInterface userSession) {
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

    public ContextAnalytics getAnalyticTracker() {
        return TrackApp.getInstance().getGTM();
    }

    public void onByMeButtonClicked(String productId) {
        getAnalyticTracker().sendEnhanceECommerceEvent(
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
        getAnalyticTracker().sendEnhanceECommerceEvent(
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
        getAnalyticTracker().sendEnhanceECommerceEvent(
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
        getAnalyticTracker().sendEnhanceECommerceEvent(
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
        getAnalyticTracker().sendEnhanceECommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_DISCOVERY_PAGE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_DISCOVERY_PAGE,
                        AffiliateEventTracking.Action.SEARCH_NOT_FOUND,
                        keyword
                )
        );
    }

    public void onJatahRekomendasiHabisDialogShow() {
        getAnalyticTracker().sendEnhanceECommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_DISCOVERY_PAGE,
                        AffiliateEventTracking.Event.AFFILIATE_VIEW,
                        AffiliateEventTracking.Category.BYME_DISCOVERY_PAGE,
                        AffiliateEventTracking.Action.IMPRESSION_JATAH_HABIS,
                        ""
                )
        );
    }

    public void onJatahRekomendasiHabisPdp() {
        getAnalyticTracker().sendEnhanceECommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_PDP,
                        AffiliateEventTracking.Event.AFFILIATE_VIEW,
                        AffiliateEventTracking.Category.BYME_DISCOVERY_PAGE,
                        AffiliateEventTracking.Action.IMPRESSION_JATAH_HABIS,
                        ""
                )
        );
    }

    public void onCobaSekarangButtonClicked() {
        getAnalyticTracker().sendEnhanceECommerceEvent(
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
        getAnalyticTracker().sendEnhanceECommerceEvent(
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
        getAnalyticTracker().sendEnhanceECommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CLAIM_TOKOPEDIA,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CLAIM,
                        AffiliateEventTracking.Action.CLICK_SIMPAN,
                        ""
                )
        );
    }

    public void onSKButtonClicked() {
        getAnalyticTracker().sendEnhanceECommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CLAIM_TOKOPEDIA,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CLAIM,
                        AffiliateEventTracking.Action.CLICK_SYARAT_KETENTUAN,
                        ""
                )
        );
    }

    public void onLihatContohButtonClicked(String productId) {
        getAnalyticTracker().sendEnhanceECommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        AffiliateEventTracking.Action.CLICK_LIHAT_CONTOH,
                        productId
                )
        );
    }

    public void onTambahGambarButtonClicked(String productId) {
        getAnalyticTracker().sendEnhanceECommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        AffiliateEventTracking.Action.CLICK_TAMBAH_GAMBAR,
                        productId
                )
        );
    }

    public void onSelesaiCreateButtonClicked(String productId) {
        getAnalyticTracker().sendEnhanceECommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        AffiliateEventTracking.Action.CLICK_SELESAI,
                        productId
                )
        );
    }

    public void onDirectRecommRekomendasikanButtonClicked() {
        getAnalyticTracker().sendEnhanceECommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_ADD_RECOMMENDATION,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_DIRECT_RECOMM,
                        AffiliateEventTracking.Action.CLICK_REKOMENDASIKAN,
                        ""
                )
        );
    }

    public void onDirectRecommProdukLainButtonClicked() {
        getAnalyticTracker().sendEnhanceECommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_ADD_RECOMMENDATION,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_DIRECT_RECOMM,
                        AffiliateEventTracking.Action.CLICK_LIHAT_PRODUK_LAINNYA,
                        ""
                )
        );
    }

    public void onDirectRecommPilihanProdukButtonClicked() {
        getAnalyticTracker().sendEnhanceECommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        AffiliateEventTracking.Action.CLICK_LIHAT_PILIHAN_PRODUK,
                        ""
                )
        );
    }

    public void onAfterClickTokopediMe(String originalLink) {
        getAnalyticTracker().sendEnhanceECommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_USER_PROFILE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_AFFILIATE_TRAFFIC,
                        AffiliateEventTracking.Action.OTHERS,
                        originalLink
                )
        );
    }

    public void onAfterClickSaldo() {
        getAnalyticTracker().sendEnhanceECommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_MY_PROFILE,
                        AffiliateEventTracking.Event.PROFILE_CLICK,
                        AffiliateEventTracking.Category.BYME_MY_PROFILE,
                        AffiliateEventTracking.Action.CLICK_TOKOPEDIA_SALDO,
                        ""
                )
        );
    }

    public void onClickProfileOnExplore() {
        getAnalyticTracker().sendEnhanceECommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_DISCOVERY_PAGE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_DISCOVERY_PAGE,
                        AffiliateEventTracking.Action.CLICK_PROFILE,
                        userSession.getUserId()
                )
        );

    }
}
