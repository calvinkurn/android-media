package com.tokopedia.topupbills.telco.data.constant;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by nabillasabbaha on 16/05/19.
 */
@StringDef({TelcoComponentName.PRODUCT_PULSA,
        TelcoComponentName.PRODUCT_ROAMING,
        TelcoComponentName.PRODUCT_PAKET_DATA,
        TelcoComponentName.TELCO_PREPAID,
        TelcoComponentName.TELCO_POSTPAID})
@Retention(RetentionPolicy.SOURCE)
public @interface TelcoComponentName {
    String PRODUCT_PULSA = "Pulsa";
    String PRODUCT_ROAMING = "Roaming";
    String PRODUCT_PAKET_DATA = "Paket Data";
    String PROMO = "Promo";
    String RECENTS = "Recents";
}
