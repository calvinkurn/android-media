package com.tokopedia.core.analytics.container;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;
import com.tokopedia.core.analytics.model.Hotlist;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.analytics.nishikino.model.Checkout;
import com.tokopedia.core.analytics.nishikino.model.GTMCart;
import com.tokopedia.core.analytics.nishikino.model.ProductDetail;
import com.tokopedia.core.analytics.nishikino.model.Promotion;
import com.tokopedia.core.analytics.nishikino.model.Purchase;

import java.util.List;
import java.util.Map;

/**
 * @author by alvarisi on 10/26/16.
 */

public interface IGTMContainer {
    /**
     * Load init container, use this when app start and sync/refresh container
     */
    void loadContainer();

    void loadContainer(String containerId, int defaultContainerResId);

    void loadContainer(ResultCallback<ContainerHolder> callback);

    /**
     * Method for send open screen event
     *
     * @param screenName screen name of opened screen
     * @return GTM Container Instance
     */
    GTMContainer sendScreen(String screenName);

    /**
     * Method for send campaign in deeplink
     *
     * @param campaign campaign values that user get from deeplink (utm)
     * @return GTM Container Instance
     */
    GTMContainer sendCampaign(Campaign campaign);

    GTMContainer clearCampaign(Campaign campaign);

    /**
     * Method for track checkout session when user checkout on cart step 2
     *
     * @param checkout detail checkout values
     * @return GTM Container Instance
     */
    GTMContainer eventCheckout(Checkout checkout);

    void clearCheckoutDataLayer();

    GTMContainer sendScreenAuthenticated(String screenName);

    GTMContainer sendScreenAuthenticatedOfficialStore(String screenName, String shopID, String shopType);

    GTMContainer eventAuthenticate(Authenticated authenticated);

    /**
     * Method for track add to cart session user, sent when user successfully add to cart
     *
     * @param cart detail product
     * @return GTM Container Instance
     */
    GTMContainer eventAddtoCart(GTMCart cart);

    GTMContainer clearAddtoCartDataLayer(String act);

    String eventHTTP();

    String getString(String key);

    String getClientIDString();

    boolean getBoolean(String key);

    long getLong(String key);

    double getDouble(String key);

    void eventError(String screenName, String errorDesc);

    void eventLogAnalytics(String screenName, String errorDesc);

    GTMContainer eventDetail(ProductDetail detail);

    void eventOnline(String uid);

    void eventNetworkError(String networkError);

    void eventTransaction(Purchase purchase);

    GTMContainer eventBannerImpression(Promotion promotion);

    GTMContainer eventBannerClick(Promotion promotion);

    void clearTransactionDataLayer(Purchase purchase);

    GTMContainer sendEvent(Map<String, Object> events);

    TagManager getTagManager();

    void pushUserId(String userId);

    void sendButtonClick(String loginError, String login, String loginError1, String label);

    void eventClickHotlistProductFeatured(Hotlist hotlist);

    void eventImpressionHotlistProductFeatured(Hotlist hotlist);

    void event(String name, Map<String, Object> data);

    void impressionHotlistTracking(String hotlistName, String promoName, String promoCode);

    void clickCopyButtonHotlistPromo(String hotlistName, String promoName, String promoCode);

    void clickTncButtonHotlistPromo(String hotlistName, String promoName, String promoCode);


    void eventTrackingEnhancedEcommerce(Map<String, Object> trackingData);

    void clearEnhanceEcommerce();

    void eventPurchaseMarketplace(Purchase purchase);

    void eventPurchaseDigital(Purchase purchase);

    void eventImpressionPromoList(List<Object> list, String promoName);

    void eventClickPromoListItem(List<Object> list, String promoName);

    void eventImpressionCategoryLifestyle(List<Object> list);

    void eventClickCategoryLifestyle(String categoryUrl, List<Object> list);

    void enhanceClickSearchResultProduct(Object object,
                                         String keyword,
                                         String actionField);

    void enhanceImpressionSearchResultProduct(List<Object> objects, String keyword);
}
