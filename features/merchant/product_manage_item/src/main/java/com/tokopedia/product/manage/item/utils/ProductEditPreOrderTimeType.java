package com.tokopedia.product.manage.item.utils;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.product.manage.item.utils.ProductEditPreOrderTimeType.DAY;
import static com.tokopedia.product.manage.item.utils.ProductEditPreOrderTimeType.WEEK;


@Retention(RetentionPolicy.SOURCE)
@IntDef({DAY, WEEK})
public @interface ProductEditPreOrderTimeType {
    int DAY = 1;
    int WEEK = 2;
    int MONTH = 3;
}
