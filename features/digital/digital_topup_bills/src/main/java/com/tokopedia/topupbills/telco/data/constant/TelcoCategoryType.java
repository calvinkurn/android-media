package com.tokopedia.topupbills.telco.data.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by nabillasabbaha on 16/05/19.
 */
@IntDef({TelcoCategoryType.CATEGORY_PULSA,
        TelcoCategoryType.CATEGORY_PAKET_DATA,
        TelcoCategoryType.CATEGORY_ROAMING,
        TelcoCategoryType.CATEGORY_PASCABAYAR})
@Retention(RetentionPolicy.SOURCE)
public @interface TelcoCategoryType {
    int CATEGORY_PULSA = 1;
    int CATEGORY_PAKET_DATA = 2;
    int CATEGORY_ROAMING = 20;
    int CATEGORY_PASCABAYAR = 9;

}
