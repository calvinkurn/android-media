package com.tokopedia.affiliate.analytics;

/**
 * @author by yfsx on 05/11/18.
 */

public interface AffiliateEventTracking {

    interface Screen {
        String BYME_CLAIM_TOKOPEDIA = "claim tokopedia by.me";
        String BYME_ADD_RECOMMENDATION = "add recommendation tokopedia by.me";
        String BYME_EXPLORE = "/explore-byme-socialcommerce";
        String BYME_CREATE_POST = "/create-post-socialcommerce";
        String BYME_USER_PROFILE = "user profile page";
        String BYME_MY_PROFILE = "my profile page";
        String BYME_PDP = "product detail page";
    }

    interface Event {
        String AFFILIATE_CLICK = "clickAffiliate";
        String AFFILIATE_VIEW = "viewAffiliate";
        String PROFILE_CLICK = "clickProfile";
        String PRODUCT_VIEW = "productView";
        String PRODUCT_CLICK = "productClick";
    }

    interface Category {
        String BYME_ONBOARD = "onboard tokopedia by.me";
        String BYME_CLAIM = "claim tokopedia by.me";
        String BYME_DIRECT_RECOMM = "direct recomm tokopedia by.me";
        String BYME_COMPLETE_PROFILE = "complete profile tokopedia by.me";
        String BYME_EXPLORE = "explore by.me socialcommerce";
        String BYME_CREATE_POST = "create post tokopedia by.me";
        String BYME_AFFILIATE_TRAFFIC = "affiliate traffic";
        String BYME_MY_PROFILE = "my profile page";
    }

    interface Action {
        String CLICK_COBA_SEKARANG = "click coba sekarang";
        String CLICK_TENTANG_KOMISI = "click tentang komisi";
        String SEARCH_NOT_FOUND = "search not found";
        String CLICK_SIMPAN = "click simpan";
        String CLICK_SYARAT_KETENTUAN = "click syarat dan ketentuan by.me";
        String CLICK_REKOMENDASIKAN = "click rekomendasikan";
        String CLICK_LIHAT_PRODUK_LAINNYA = "click lihat produk lainnya";
        String CLICK_LIHAT_PILIHAN_PRODUK = "click lihat pilihan produk";
        String CLICK_BYME = "click by.me";
        String IMPRESSION_PRODUCTS_AFFILIATE = "impressions products affiliate";
        String IMPRESSION_ONBOARD = "impression onboard";
        String CLICK_PRODUCTS_AFFILIATE = "click products affiliate";
        String IMPRESSION_JATAH_HABIS = "popup message jatah rekomendasi habis";
        String CLICK_LIHAT_CONTOH = "click lihat contoh";
        String CLICK_SELESAI = "click selesai";
        String CLICK_TAMBAH_GAMBAR = "click tambah gambar";
        String CLICK_TOKOPEDIA_SALDO = "cta tokopedia saldo";
        String CLICK_PROFILE = "click profile";
        String OTHERS = "others";
    }

    interface EventLabel {
    }
}
