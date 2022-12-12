package com.tokopedia.affiliatecommon.analytics;

/**
 * @author by yfsx on 05/11/18.
 */

public interface AffiliateEventTracking {

    interface Screen {
        String BYME_CLAIM_TOKOPEDIA = "claim tokopedia by.me";
        String BYME_ADD_RECOMMENDATION = "add recommendation tokopedia by.me";
        String BYME_EXPLORE = "/explore-byme-socialcommerce";
        String BYME_CREATE_POST = "/create-post-socialcommerce";
        String BYME_CONTENT_DETAIL = "/user-profile-socialcommerce-content-detail";
        String BYME_USER_PROFILE = "user profile page";
        String BYME_MY_PROFILE = "my profile page";
    }

    interface Event {
        String AFFILIATE_CLICK = "clickAffiliate";
        String PROFILE_CLICK = "clickProfile";
        String PRODUCT_VIEW = "productView";
        String PRODUCT_CLICK = "productClick";
    }

    interface Category {
        String BYME_ONBOARD = "onboard tokopedia by.me";
        String BYME_CLAIM = "claim tokopedia by.me";
        String BYME_DIRECT_RECOMM = "direct recomm tokopedia by.me";
        String BYME_EXPLORE = "explore by.me socialcommerce";
        String BYME_CREATE_POST = "create post by.me socialcommerce";
        String BYME_AFFILIATE_TRAFFIC = "affiliate traffic";
        String BYME_MY_PROFILE = "my profile page";
        String BYME_AFFILIATE_CDP_EXTERNAL = "content detail page traffic - external";
    }

    interface Action {
        String SEARCH_NOT_FOUND = "search not found";
        String CLICK_SIMPAN = "click simpan";
        String CLICK_SYARAT_KETENTUAN = "click syarat dan ketentuan by.me";
        String CLICK_REKOMENDASIKAN = "click rekomendasikan";
        String CLICK_LIHAT_PRODUK_LAINNYA = "click lihat produk lainnya";
        String CLICK_LIHAT_PILIHAN_PRODUK = "click lihat pilihan produk";
        String IMPRESSION_ONBOARD = "impression onboard";
        String IMPRESSION_PRODUCT = "impression product affiliate";
        String CLICK_PRODUCT = "click product affiliate";
        String CLICK_LIHAT_CONTOH = "click lihat contoh";
        String CLICK_TOKOPEDIA_SALDO = "cta tokopedia saldo";
        String CLICK_SEARCH_SUGGESTION = "click search suggestion";
        String OTHERS = "others";
        String CLICK_AFFILIATE_CDP_EXTERNAL = "external";
        String IMPRESSION_SUGGESTION_MERCHANT = "impression product suggestion as merchant";
        String IMPRESSION_SUGGESTION_AFFILIATE = "impression product suggestion as affiliate";
        String CLICK_SUGGESTION_MERCHANT = "click product suggestion as merchant";
        String CLICK_SUGGESTION_AFFILIATE = "click product suggestion as affiliate";
    }

    interface EventLabel {
        String SEARCH_RESULT = "search result";
    }
}
