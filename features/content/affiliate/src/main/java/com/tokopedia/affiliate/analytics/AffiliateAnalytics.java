package com.tokopedia.affiliate.analytics;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

/**
 * @author by yfsx on 05/11/18.
 */
public class AffiliateAnalytics {

    private static final String PARAM_SCREEN_NAME = "screenName";
    private static final String PARAM_EVENT_NAME = "event";
    private static final String PARAM_EVENT_CATEGORY = "eventCategory";
    private static final String PARAM_EVENT_ACTION = "eventAction";
    private static final String PARAM_EVENT_LABEL = "eventLabel";
    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_PRODUCT_ID = "product_id";
    private static final String PARAM_SHOP_ID = "shop_id";

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

    private HashMap<String, Object> getEnhancedEcommerce(String ecommerceType, String productName,
                                                         String productId, int productComission,
                                                         String sectionName, int position) {
        HashMap<String, Object> ecommerceItem = new HashMap<>();
        ecommerceItem.put("name", productName);
        ecommerceItem.put("id", productId);
        ecommerceItem.put("price", productComission);
        ecommerceItem.put("list", String.format("/explore page byme - %s", sectionName));
        ecommerceItem.put("position", position);

        ArrayList<Object> listEcommerce = new ArrayList<>();
        listEcommerce.add(ecommerceItem);

        HashMap<String, Object> ecommerce = new HashMap<>();
        ecommerce.put("currencyCode", "IDR");
        ecommerce.put(ecommerceType, listEcommerce);
        return ecommerce;
    }

    public AnalyticTracker getAnalyticTracker() {
        return abstractionRouter.getAnalyticTracker();
    }

//    3
    public void onSearchSubmitted(String keyword) {
        getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        "search",
                        keyword
                )
        );
    }

//    4
    public void onInfoClicked() {
        getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        "click info",
                        ""
                )
        );
    }

//    5
    public void onProfileClicked(String userId) {
        getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        "click profile page",
                        userId
                )
        );
    }

//    6
    public void onBannerClicked(String activityId, String imageUrl) {
        getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        "click banner global announcement",
                        String.format("%s-%s", activityId, imageUrl)
                )
        );
    }

//    7
    public void onQuickFilterClicked(String category) {
        getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        "click quick filter",
                        category
                )
        );
    }

//    9
    public void onProductImpression(String productName, String productId, int productComission,
                                    String sectionName, int position) {
        HashMap<String, Object> data = setDefaultDataWithUserId(
                AffiliateEventTracking.Screen.BYME_EXPLORE,
                AffiliateEventTracking.Event.PRODUCT_VIEW,
                AffiliateEventTracking.Category.BYME_EXPLORE,
                "impression product affiliate",
                String.format("%s-%s", sectionName, productName)
        );
        data.put(
                "ecommerce",
                getEnhancedEcommerce("impressions", productName, productId,
                        productComission, sectionName, position)
        );
        getAnalyticTracker().sendEnhancedEcommerce(data);
    }

//    10
    public void onProductClicked(String productName, String productId, int productComission,
                                 String sectionName, int position) {
        HashMap<String, Object> data = setDefaultDataWithUserId(
                AffiliateEventTracking.Screen.BYME_EXPLORE,
                AffiliateEventTracking.Event.PRODUCT_CLICK,
                AffiliateEventTracking.Category.BYME_EXPLORE,
                "click product affiliate",
                String.format("%s-%s", sectionName, productName)
        );
        data.put(
                "ecommerce",
                getEnhancedEcommerce("click", productName, productId,
                        productComission, sectionName, position)
        );
        getAnalyticTracker().sendEventTracking(data);
    }

//    11
    public void onPopularClicked(String profileId) {
        getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        "click to other profile - most popular curation",
                        profileId
                )
        );
    }

//    12
    public void onSortClicked(String profileId) {
        getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        "click to sort",
                        profileId
                )
        );
    }

//    13
    public void onFilterClicked(String profileId) {
        getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        "click to filter",
                        profileId
                )
        );
    }

    public void onSearchNotFound(String keyword) {
        getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        AffiliateEventTracking.Action.SEARCH_NOT_FOUND,
                        keyword
                )
        );
    }

    public void onJatahRekomendasiHabisDialogShow() {
        getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_VIEW,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        AffiliateEventTracking.Action.IMPRESSION_JATAH_HABIS,
                        ""
                )
        );
    }

    public void onJatahRekomendasiHabisPdp() {
        getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_PDP,
                        AffiliateEventTracking.Event.AFFILIATE_VIEW,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        AffiliateEventTracking.Action.IMPRESSION_JATAH_HABIS,
                        ""
                )
        );
    }

    public void onCobaSekarangButtonClicked() {
        getAnalyticTracker().sendEventTracking(
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
        getAnalyticTracker().sendEventTracking(
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
        getAnalyticTracker().sendEventTracking(
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
        getAnalyticTracker().sendEventTracking(
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
        getAnalyticTracker().sendEventTracking(
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
        getAnalyticTracker().sendEventTracking(
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
        getAnalyticTracker().sendEventTracking(
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
        getAnalyticTracker().sendEventTracking(
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
        getAnalyticTracker().sendEventTracking(
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
        getAnalyticTracker().sendEventTracking(
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
        getAnalyticTracker().sendEventTracking(
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
        getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_MY_PROFILE,
                        AffiliateEventTracking.Event.PROFILE_CLICK,
                        AffiliateEventTracking.Category.BYME_MY_PROFILE,
                        AffiliateEventTracking.Action.CLICK_TOKOPEDIA_SALDO,
                        ""
                )
        );
    }

    public void onImpressionOnboard() {
        getAnalyticTracker().sendEventTracking(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CLAIM_TOKOPEDIA,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_ONBOARD,
                        AffiliateEventTracking.Action.IMPRESSION_ONBOARD,
                        ""
                )
        );
    }
}
