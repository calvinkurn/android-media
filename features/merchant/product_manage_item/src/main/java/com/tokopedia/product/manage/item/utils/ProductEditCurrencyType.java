package com.tokopedia.product.manage.item.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.product.manage.item.util.ProductEditCurrencyType.RUPIAH;
import static com.tokopedia.product.manage.item.util.ProductEditCurrencyType.USD;

@Retention(RetentionPolicy.SOURCE)
@IntDef({RUPIAH, USD})
public @interface ProductEditCurrencyType {
    int RUPIAH = 1;
    int USD = 2;
}
