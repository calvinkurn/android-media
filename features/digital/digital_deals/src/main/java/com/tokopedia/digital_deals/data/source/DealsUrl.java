package com.tokopedia.digital_deals.data.source;

public class DealsUrl {
    //Base Url
    public static String DEALS_DOMAIN = "https://booking.tokopedia.com/";

    public interface HelperUrl {
        String DEALS_LIST = "v1/api/h/deal";
        String DEALS_LIST_SEARCH = "v1/api/s/deal";
        String DEALS_LOCATIONS = "v1/api/location/deal";
        String DEALS_PRODUCT = "v1/api/p";
        String DEALS_LIKES = "v1/api/deal/rating";
        String DEALS_LIKES_PRODUCT = "v1/api/deal/rating/product";
    }
}
