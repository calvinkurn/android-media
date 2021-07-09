package com.tokopedia.topupbills.telco.data.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by nabillasabbaha on 16/05/19.
 */
@IntDef({TelcoProductType.PRODUCT_GRID, TelcoProductType.PRODUCT_LIST})
@Retention(RetentionPolicy.SOURCE)
public @interface TelcoProductType {
    int PRODUCT_GRID = 0;
    int PRODUCT_LIST = 1;
    int PRODUCT_MCCM = 2;
}
