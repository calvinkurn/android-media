package com.tokopedia.common.topupbills.data.constant;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by nabillasabbaha on 16/05/19.
 */
@StringDef({TelcoComponentName.PRODUCT_PULSA,
        TelcoComponentName.PRODUCT_ROAMING,
        TelcoComponentName.PRODUCT_PAKET_DATA,
        TelcoComponentName.PRODUCT_PASCABAYAR,
        TelcoComponentName.PROMO,
        TelcoComponentName.SPECIAL_PROMO_MCCM,
        TelcoComponentName.RECENTS})
@Retention(RetentionPolicy.SOURCE)
public @interface TelcoComponentName {
    String PRODUCT_PULSA = "Pulsa";
    String PRODUCT_ROAMING = "Roaming";
    String PRODUCT_PAKET_DATA = "Paket Data";
    String PRODUCT_PASCABAYAR = "Pascabayar";
    String PROMO = "Promo";
    String SPECIAL_PROMO_MCCM = "special promo";
    String RECENTS = "Transaksi Terakhir";
}
