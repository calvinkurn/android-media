package com.tokopedia.topupbills.telco.data.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by nabillasabbaha on 16/05/19.
 */
@IntDef({TelcoComponentType.CLIENT_NUMBER_PREPAID,
        TelcoComponentType.PRODUCT_PULSA,
        TelcoComponentType.PRODUCT_ROAMING,
        TelcoComponentType.PRODUCT_PAKET_DATA,
        TelcoComponentType.TELCO_PREPAID,
        TelcoComponentType.TELCO_POSTPAID})
@Retention(RetentionPolicy.SOURCE)
public @interface TelcoComponentType {
    int TELCO_PREPAID = 2;
    int TELCO_POSTPAID = 3;
    int CLIENT_NUMBER_PREPAID = 5;
    int CLIENT_NUMBER_PROSTPAID = 19;
    int PRODUCT_PULSA = 6;
    int PRODUCT_ROAMING = 7;
    int PRODUCT_PAKET_DATA = 8;
    int FAV_NUMBER_PREPAID = 1;
    int FAV_NUMBER_POSTPAID = 9;
}
