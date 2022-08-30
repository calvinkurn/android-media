package com.tokopedia.digital_deals.data.source;

import com.tokopedia.url.TokopediaUrl;

public class DealsUrl {
    //Base Url
    public static String DEALS_DOMAIN = TokopediaUrl.Companion.getInstance().getBOOKING();

    public interface HelperUrl {
        String DEALS_LOCATIONS = "v1/api/s/location";
        String DEALS_CITIES = "v1/api/location/deal";
        String DEALS_PRODUCT = "v1/api/p";
        String DEALS_LIKES = "v1/api/deal/rating";
        String DEALS_LIKES_PRODUCT = "v1/api/deal/rating/product";
        String DEALS_BRAND = "v1/api/b/";
        String DEALS_NSQ_EVENT = "/v1/api/tracking";
        String DEALS_NEAREST_LOCATION = "/v1/api/s/nearestlocation";
    }

    public interface WebUrl{
       String REDEEM_URL = "https://www.tokopedia.com/help/article/st-1283-tokopedia-food-voucher";
    }

    public interface AppLink{
        String DIGITAL_DEALS = "tokopedia://deals";
    }
}
