package com.tokopedia.topupbills.telco.data.constant;


import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
        TelcoFavNumberType.TYPE_INPUT_TEL,
        TelcoFavNumberType.TYPE_INPUT_NUMERIC,
        TelcoFavNumberType.TYPE_INPUT_ALPHANUMERIC,
        TelcoFavNumberType.DEFAULT_TYPE_CONTRACT
})
public @interface TelcoFavNumberType {

    String TYPE_INPUT_TEL = "tel";
    String TYPE_INPUT_NUMERIC = "numeric";
    String TYPE_INPUT_ALPHANUMERIC = "tel";
    String DEFAULT_TYPE_CONTRACT = "client_number";

}
