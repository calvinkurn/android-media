package com.tokopedia.digital.product.additionalfeature.etoll;

/**
 * Created by Rizky on 23/05/18.
 */
public interface ETollEventTracking {

    interface Event {
        String CLICK_NFC = "clickNFC";
    }

    interface Category {
        String DIGITAL_NFC = "digital - nfc";
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
    }

    interface Label {
        String EMONEY = "emoney";
    }

}
