package com.tokopedia.product.detail.common.data.model.constant;

import android.support.annotation.StringDef;

import static com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef.ACTIVE;
import static com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef.BANNED;
import static com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef.DELETED;
import static com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef.HIDDEN;
import static com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef.PENDING;
import static com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef.WAREHOUSE;

@StringDef({DELETED, ACTIVE, WAREHOUSE, HIDDEN, PENDING, BANNED})
public @interface ProductStatusTypeDef {
    String DELETED = "DELETED";
    String ACTIVE = "ACTIVE";
    String WAREHOUSE = "WAREHOUSE";
    String HIDDEN = "HIDDEN";
    String PENDING = "PENDING";
    String BANNED = "BANNED";
}
