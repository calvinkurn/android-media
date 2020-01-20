package com.tokopedia.digital_deals.data.source;

import com.tokopedia.url.TokopediaUrl;

public class DealsUrl {
    //Base Url
    public static String DEALS_DOMAIN = TokopediaUrl.Companion.getInstance().getBOOKING();

    public interface HelperUrl {
        String DEALS_LIST = "v1/api/h/deal";
        String DEALS_LIST_V2 = "v2/api/h/deal";
        String DEALS_LIST_SEARCH = "v1/api/s/deal";
        String DEALS_LOCATIONS = "v1/api/s/location";
        String DEALS_CITIES = "v1/api/location/deal";
        String DEALS_PRODUCT = "v1/api/p";
        String DEALS_CATEGORY = "v1/api/h/deal/c/";
        String DEALS_LIKES = "v1/api/deal/rating";
        String DEALS_LIKES_PRODUCT = "v1/api/deal/rating/product";
        String DEALS_BRAND = "v1/api/b/";
        String DEALS_CATEGORIES = "v1/api/c/deal/children";
        String DEALS_NSQ_EVENT = "/v1/api/tracking";
        String DEALS_NEAREST_LOCATION = "/v1/api/s/nearestlocation";
    }

    public interface WebUrl{
        String PROMOURL = "https://www.tokopedia.com/promo/produk-digital/entertainment";
        String FAQURL = "https://www.tokopedia.com/contact-us#step2";
        String TRANSATIONSURL = "https://pulsa.tokopedia.com/order-list/";
        String REDEEM_URL = "https://www.tokopedia.com/bantuan/produk-digital/tokopedia_e_voucher/seputar-tokopedia-e-voucher/#cara-menggunakan-e-voucher";
    }

    public interface AppLink{
        String DEALS = "deals";
        String DIGITAL_DEALS = "tokopedia://deals";
        String DIGITAL_DEALS_DETAILS = "tokopedia://deals/{slug}";
        String DIGITAL_DEALS_CATEGORY="tokopedia://deals/category/page";
        String DIGITAL_DEALS_BRAND = "tokopedia://deals/brand/{slug}";
        String DIGITAL_DEALS_ALL_BRAND = "tokopedia://deals/allbrands/{isVoucher}";
    }
}
