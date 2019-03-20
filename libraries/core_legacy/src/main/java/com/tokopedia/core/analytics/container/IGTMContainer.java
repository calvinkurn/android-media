package com.tokopedia.core.analytics.container;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;
import com.tokopedia.core.analytics.model.Hotlist;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.analytics.nishikino.model.Checkout;
import com.tokopedia.core.analytics.nishikino.model.GTMCart;
import com.tokopedia.core.analytics.nishikino.model.Product;
import com.tokopedia.core.analytics.nishikino.model.ProductDetail;
import com.tokopedia.core.analytics.nishikino.model.Purchase;

import java.util.List;
import java.util.Map;

/**
 * @author by alvarisi on 10/26/16.
 */

@Deprecated
public interface IGTMContainer {

    /**
     * Method for send open screen event
     *
     * @param screenName screen name of opened screen
     * @return GTM Container Instance
     */
    GTMContainer sendScreen(String screenName);

    GTMContainer sendScreen(String screenName, Map<String, String> customDimension);

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
    GTMContainer eventCheckout(Checkout checkout, String paymentId);

    GTMContainer eventCheckout(Checkout checkout);

    void clearCheckoutDataLayer();

    GTMContainer sendScreenAuthenticated(String screenName);

    GTMContainer sendScreenAuthenticated(String screenName, Map<String, String> customDimension);

    GTMContainer sendScreenAuthenticated(String screenName, String shopID, String shopType, String pageType, String productId);

    GTMContainer eventAuthenticate();

    GTMContainer eventAuthenticate(Map<String, String> customDimension);

    String getClientIDString();

    GTMContainer eventDetail(ProductDetail detail);

    GTMContainer sendEvent(Map<String, Object> events);

    TagManager getTagManager();

    void event(String name, Map<String, Object> data);

    void eventTrackingEnhancedEcommerce(Map<String, Object> trackingData);

    void clearEnhanceEcommerce();

    void eventPurchaseDigital(Purchase purchase);
}
