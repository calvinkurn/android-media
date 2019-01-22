package com.tokopedia.common_digital.common.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by alvarisi on 3/8/18.
 */

@Retention(RetentionPolicy.SOURCE)
@StringDef({
        DigitalCategoryConstant.PULSA,
        DigitalCategoryConstant.PAKET_DATA,
        DigitalCategoryConstant.PLN,
        DigitalCategoryConstant.BPJS,
        DigitalCategoryConstant.PDAM,
        DigitalCategoryConstant.GAME,
        DigitalCategoryConstant.CREDIT,
        DigitalCategoryConstant.TV,
        DigitalCategoryConstant.POSTPAID,
        DigitalCategoryConstant.TELKOM,
        DigitalCategoryConstant.STREAMING,
        DigitalCategoryConstant.PGN,
        DigitalCategoryConstant.ROAMING,
        DigitalCategoryConstant.TAX,
        DigitalCategoryConstant.GIFT_CARD,
        DigitalCategoryConstant.RETRIBUTION,
        DigitalCategoryConstant.MTIX,
        DigitalCategoryConstant.CREDIT_CARD,
        DigitalCategoryConstant.ETOLL
})
public @interface DigitalCategoryConstant {
    String PULSA = "1";
    String PAKET_DATA = "2";
    String PLN = "3";
    String BPJS = "4";
    String PDAM = "5";
    String GAME = "6";
    String CREDIT = "7";
    String TV = "8";
    String POSTPAID = "9";
    String TELKOM = "10";
    String STREAMING = "13";
    String PGN = "14";
    String ROAMING = "20";
    String TAX = "22";
    String GIFT_CARD = "24";
    String RETRIBUTION = "25";
    String MTIX = "31";
    String CREDIT_CARD = "26";
    String ETOLL = "34";
}


