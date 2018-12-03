package com.tokopedia.digital.product.additionalfeature.etoll;

/**
 * Created by Rizky on 23/05/18.
 */
public interface ETollEventTracking {

    interface Event {
        String CLICK_NFC = "clickNFC";
        String USER_HOME_INTERACTION_PAGE = "userInteractionHomepage";
    }

    interface Category {
        String DIGITAL_NFC = "digital - nfc";
        String HOMEPAGE_DIGITAL = "homepage digital";
        String HOMEPAGE_DIGITAL_WIDGET = "homepage digital widget";
    }

    interface Action {
        String CLICK_ACTIVATE = "click aktifkan";
        String CLICK_ACTIVATE_PROMPT = "click aktifkan_prompt";
        String CLICK_CANCEL_PROMPT = "click batalkan prompt";
        String CHECK_STEP_1 = "check step - 1";
        String CHECK_STEP_2 = "check step - 2";
        String CLICK_TOPUP_EMONEY = "click top up emoney";
        String FAILED_UPDATE_BALANCE = "failed update saldo";
        String CARD_IS_NOT_SUPPORTED = "card not supported";
        String SUCCESS_CHECK_BALANCE = "success check saldo";
        String CLICK_SEE_ALL_PRODUCTS = "click lihat semua produk";
        String CLICK_RECOMMENDATION_WIDGET = "click recommendation widget";
    }

    interface Label {
        String EMONEY = "emoney";
    }

}
