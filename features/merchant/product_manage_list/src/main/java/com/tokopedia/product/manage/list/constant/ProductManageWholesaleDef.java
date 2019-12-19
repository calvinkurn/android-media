package com.tokopedia.product.manage.list.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.product.manage.list.constant.ProductManageWholesaleDef.NOT_WHOLESALE;
import static com.tokopedia.product.manage.list.constant.ProductManageWholesaleDef.WHOLESALE;

/**
 * Created by zulfikarrahman on 9/22/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({NOT_WHOLESALE, WHOLESALE})
public @interface ProductManageWholesaleDef {
    int NOT_WHOLESALE = 0;
    int WHOLESALE = 1;
}
