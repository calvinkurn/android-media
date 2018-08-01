package com.tokopedia.instantloan.common.analytics;

public class InstantLoanEventConstants {

    interface Event {
        String EVENT_CLICK_FINTECH_MICROSITE = "clickFintechMicrosite";
    }

    interface Category {
        String FINTECH_HOMEPAGE = "fin - android homepage";
    }

    interface Action {
        String PL_BANNER_IMPRESSION = "pl - banner impressions - {partner name}";
        String PL_BANNER_CLICK = "pl - banner impressions click - {partner name}";
        String PL_CARI_PINJAMAN_CLICK = "pl - cari pinjaman click";
        String PL_POP_UP_CLICK = "pl - dana instan popup click";
    }

    public interface EventLabel {
        String PL_POPUP_LEARN_MORE = "pelajari selengkapnya";
        String PL_POPUP_TNC = "third - syarat dan ketentuan";
        String PL_POPUP_CONNECT_DEVICE = "third - hubungkan perangkat";
        String PL_INTRO_SLIDER_FIRST_NEXT = "first - next";
        String PL_INTRO_SLIDER_SECOND_NEXT = "second - next";
        String PL_INTRO_SLIDER_SECOND_PREVIOUS = "second - previous";
        String PL_INTRO_SLIDER_THIRD_PREVIOUS = "third - previous";
        String PL_PERMISSION_DENIED = "forth - permission denied - ";
    }

}
