package com.tokopedia.product.manage.list.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.product.manage.list.constant.ProductManageStockDef.NOT_USING_STOCK;
import static com.tokopedia.product.manage.list.constant.ProductManageStockDef.USING_STOCK;


/**
 * Created by zulfikarrahman on 9/22/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({NOT_USING_STOCK, USING_STOCK})
public @interface ProductManageStockDef {
    int NOT_USING_STOCK = 0;
    int USING_STOCK = 1;
}
