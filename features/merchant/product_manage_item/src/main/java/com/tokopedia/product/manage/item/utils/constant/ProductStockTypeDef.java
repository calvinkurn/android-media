package com.tokopedia.product.manage.item.utils.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.product.manage.item.utils.constant.ProductStockTypeDef.TYPE_ACTIVE;
import static com.tokopedia.product.manage.item.utils.constant.ProductStockTypeDef.TYPE_NOT_ACTIVE;


/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TYPE_ACTIVE, TYPE_NOT_ACTIVE})
public @interface ProductStockTypeDef {
    int TYPE_ACTIVE = 1;
    int TYPE_NOT_ACTIVE = 0;
}