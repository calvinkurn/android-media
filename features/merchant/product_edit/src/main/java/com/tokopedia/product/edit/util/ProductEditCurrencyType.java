package com.tokopedia.product.edit.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.product.edit.util.ProductEditCurrencyType.RUPIAH;
import static com.tokopedia.product.edit.util.ProductEditCurrencyType.USD;

@Retention(RetentionPolicy.SOURCE)
@IntDef({RUPIAH, USD})
public @interface ProductEditCurrencyType {
    int RUPIAH = 1;
    int USD = 2;
}
