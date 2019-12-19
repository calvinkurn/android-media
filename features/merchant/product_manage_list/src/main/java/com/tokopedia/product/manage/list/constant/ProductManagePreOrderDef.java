package com.tokopedia.product.manage.list.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.product.manage.list.constant.ProductManagePreOrderDef.NOTE_PRE_ORDER;
import static com.tokopedia.product.manage.list.constant.ProductManagePreOrderDef.PRE_ORDER;

/**
 * Created by zulfikarrahman on 9/22/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({NOTE_PRE_ORDER, PRE_ORDER})
public @interface ProductManagePreOrderDef {
    int NOTE_PRE_ORDER = 0;
    int PRE_ORDER = 1;
}
