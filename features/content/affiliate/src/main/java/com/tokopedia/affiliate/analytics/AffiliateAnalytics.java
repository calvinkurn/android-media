package com.tokopedia.affiliate.analytics;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.interfaces.ContextAnalytics;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        HashMap<String, Object> mapEvent = setDefaultData(screenName, event, category, action,
                label);
        mapEvent.put(PARAM_USER_ID, userSession.getUserId());
        return mapEvent;
    }

    public ContextAnalytics getAnalyticTracker() {
        return TrackApp.getInstance().getGTM();
    }

    private HashMap<String, Object> getEnhancedEcommerceImpressions(
            String productName, String productId, int productComission, String sectionName,
            int position) {
        HashMap<String, Object> ecommerceItem = new HashMap<>();
        ecommerceItem.put("name", productName);
        ecommerceItem.put("id", productId);
        ecommerceItem.put("price", productComission);
        ecommerceItem.put("list", String.format("/affiliate explore - %s", sectionName));
        ecommerceItem.put("position", position);

        ArrayList<Object> listEcommerce = new ArrayList<>();
        listEcommerce.add(ecommerceItem);

        HashMap<String, Object> ecommerce = new HashMap<>();
        ecommerce.put("currencyCode", "IDR");
        ecommerce.put("impressions", listEcommerce);
        return ecommerce;
    }

    private HashMap<String, Object> getEnhancedEcommerceClick(
            String productName, String productId, int productComission, String sectionName,
            int position) {
        String list = String.format("/affiliate explore - %s", sectionName);

        HashMap<String, Object> productItem = new HashMap<>();
        productItem.put("name", productName);
        productItem.put("id", productId);
        productItem.put("price", productComission);
        productItem.put("list", list);
        productItem.put("position", position);

        ArrayList<Object> products = new ArrayList<>();
        products.add(productItem);

        HashMap<String, Object> actionField = new HashMap<>();
        actionField.put("list", list);

        HashMap<String, Object> click = new HashMap<>();
        click.put("actionField", actionField);
        click.put("products", products);

        HashMap<String, Object> ecommerce = new HashMap<>();
        ecommerce.put("click", click);
        return ecommerce;
    }

    //    3
    public void onSearchSubmitted(String keyword) {
        getAnalyticTracker().sendEnhanceEcommerceEvent(
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
        getAnalyticTracker().sendEnhanceEcommerceEvent(
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
        getAnalyticTracker().sendEnhanceEcommerceEvent(
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
        getAnalyticTracker().sendEnhanceEcommerceEvent(
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
        getAnalyticTracker().sendEnhanceEcommerceEvent(
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
                String.format("%s-%s", sectionName, productId)
        );
        data.put(
                "ecommerce",
                getEnhancedEcommerceImpressions(productName,
                        productId,
                        productComission,
                        sectionName,
                        position)
        );
        getAnalyticTracker().sendEnhanceEcommerceEvent(data);
    }

    //    10
    public void onProductClicked(String productName, String productId, int productComission,
                                 String sectionName, int position) {
        HashMap<String, Object> data = setDefaultDataWithUserId(
                AffiliateEventTracking.Screen.BYME_EXPLORE,
                AffiliateEventTracking.Event.PRODUCT_CLICK,
                AffiliateEventTracking.Category.BYME_EXPLORE,
                "click product affiliate",
                String.format("%s-%s", sectionName, productId)
        );
        data.put(
                "ecommerce",
                getEnhancedEcommerceClick(productName,
                        productId,
                        productComission,
                        sectionName,
                        position)
        );
        getAnalyticTracker().sendEnhanceEcommerceEvent(data);
    }

    //    11
    public void onPopularClicked(String profileId) {
        getAnalyticTracker().sendEnhanceEcommerceEvent(
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
        getAnalyticTracker().sendEnhanceEcommerceEvent(
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
        getAnalyticTracker().sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        "click to filter",
                        profileId
                )
        );
    }

    //    19
    public void onJatahRekomendasiHabisDialogShow() {
        getAnalyticTracker().sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        "popup message jatah rekomendasi habis",
                        userSession.getUserId()
                )
        );
    }

    //    20
    public void onTambahGambarButtonClicked() {
        getAnalyticTracker().sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        "click tambah foto",
                        userSession.getUserId()
                )
        );
    }

    //    21
    public void onTambahVideoButtonClicked() {
        getAnalyticTracker().sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        "click tambah video",
                        userSession.getUserId()
                )
        );
    }

    //    22
    public void onSelesaiCreateButtonClicked(List<String> productIds) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < productIds.size(); i++) {
            stringBuilder.append(productIds.get(i));
            if (i != productIds.size() - 1) {
                stringBuilder.append(",");
            }
        }
        getAnalyticTracker().sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        "click post sekarang",
                        String.format("%s,%s", userSession.getUserId(), stringBuilder.toString())
                )
        );
    }

    //    23
    public void onTambahTagButtonClicked() {
        getAnalyticTracker().sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        "click tambah tag",
                        userSession.getUserId()
                )
        );
    }

    public void onSearchNotFound(String keyword) {
        getAnalyticTracker().sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_EXPLORE,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_EXPLORE,
                        AffiliateEventTracking.Action.SEARCH_NOT_FOUND,
                        keyword
                )
        );
    }

    public void onSimpanButtonClicked() {
        getAnalyticTracker().sendEnhanceEcommerceEvent(
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
        getAnalyticTracker().sendEnhanceEcommerceEvent(
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
        getAnalyticTracker().sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CREATE_POST,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_CREATE_POST,
                        AffiliateEventTracking.Action.CLICK_LIHAT_CONTOH,
                        productId
                )
        );
    }

    public void onDirectRecommRekomendasikanButtonClicked() {
        getAnalyticTracker().sendEnhanceEcommerceEvent(
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
        getAnalyticTracker().sendEnhanceEcommerceEvent(
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
        getAnalyticTracker().sendEnhanceEcommerceEvent(
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
        getAnalyticTracker().sendEnhanceEcommerceEvent(
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
        getAnalyticTracker().sendEnhanceEcommerceEvent(
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
        getAnalyticTracker().sendEnhanceEcommerceEvent(
                setDefaultDataWithUserId(
                        AffiliateEventTracking.Screen.BYME_CLAIM_TOKOPEDIA,
                        AffiliateEventTracking.Event.AFFILIATE_CLICK,
                        AffiliateEventTracking.Category.BYME_ONBOARD,
                        AffiliateEventTracking.Action.IMPRESSION_ONBOARD,
                        ""
                )
        );
    }

    public void onAutoCompleteClicked(String keyword) {
        getAnalyticTracker().sendGeneralEvent(AffiliateEventTracking.Event.AFFILIATE_CLICK,
                AffiliateEventTracking.Category.BYME_EXPLORE,
                AffiliateEventTracking.Action.CLICK_SEARCH_SUGGESTION,
                keyword);
    }

    public void trackProductImpressionNonEE(String productId) {
        getAnalyticTracker().sendGeneralEvent(AffiliateEventTracking.Event.AFFILIATE_CLICK,
                AffiliateEventTracking.Category.BYME_EXPLORE,
                AffiliateEventTracking.Action.IMPRESSION_PRODUCT,
                AffiliateEventTracking.EventLabel.SEARCH_RESULT_PRODUCT_ID+productId);
    }

    public void onProductSearchClicked(String productId) {
        getAnalyticTracker().sendGeneralEvent(AffiliateEventTracking.Event.AFFILIATE_CLICK,
                AffiliateEventTracking.Category.BYME_EXPLORE,
                AffiliateEventTracking.Action.CLICK_PRODUCT,
                AffiliateEventTracking.EventLabel.SEARCH_RESULT_PRODUCT_ID+productId);
    }
}
