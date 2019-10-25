package com.tokopedia.product.manage.item.utils;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.product.manage.item.utils.ProductEditWeightType.GRAM;
import static com.tokopedia.product.manage.item.utils.ProductEditWeightType.KILOGRAM;

@Retention(RetentionPolicy.SOURCE)
@IntDef({GRAM, KILOGRAM})
public @interface ProductEditWeightType {
    int GRAM = 1;
    int KILOGRAM = 2;
}
