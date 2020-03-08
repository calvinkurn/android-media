package com.tokopedia.product.addedit.common.util;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.product.addedit.common.util.CurrencyTypeDef.TYPE_IDR;
import static com.tokopedia.product.addedit.common.util.CurrencyTypeDef.TYPE_USD;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TYPE_IDR, TYPE_USD})
public @interface CurrencyTypeDef {
    int TYPE_IDR = 1;
    int TYPE_USD = 2;
}
