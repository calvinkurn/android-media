package com.tokopedia.digital_deals.data.source;

public class DealsUrl {
    //Base Url
    public static String DEALS_DOMAIN = "https://booking.tokopedia.com/";

    public interface HelperUrl {
        String DEALS_LIST = "v1/api/h/deal";
        String DEALS_LIST_SEARCH = "v1/api/s/deal";
        String DEALS_LOCATIONS = "v1/api/location/deal";
        String DEALS_PRODUCT = "v1/api/p";
        String DEALS_CATEGORY = "v1/api/h/deal/c/";
        String DEALS_LIKES = "v1/api/deal/rating";
        String DEALS_LIKES_PRODUCT = "v1/api/deal/rating/product";
        String DEALS_BRAND = "v1/api/b/";
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
    }
}
