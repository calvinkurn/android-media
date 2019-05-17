package com.tokopedia.topupbills.telco.data.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by nabillasabbaha on 16/05/19.
 */
@IntDef({TelcoComponentType.CLIENT_NUMBER,
        TelcoComponentType.PRODUCT_PULSA,
        TelcoComponentType.PRODUCT_ROAMING,
        TelcoComponentType.PRODUCT_PAKET_DATA})
@Retention(RetentionPolicy.SOURCE)
public @interface TelcoComponentType {
    int CLIENT_NUMBER = 5;
    int PRODUCT_PULSA = 6;
    int PRODUCT_ROAMING = 7;
    int PRODUCT_PAKET_DATA = 8;
}
