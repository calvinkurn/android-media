package com.tokopedia.product.detail.common.data.model.constant;

import android.support.annotation.StringDef;

import static com.tokopedia.product.detail.common.data.model.constant.ProductConditionTypeDef.NEW;
import static com.tokopedia.product.detail.common.data.model.constant.ProductConditionTypeDef.UNKNOWN;
import static com.tokopedia.product.detail.common.data.model.constant.ProductConditionTypeDef.USED;

@StringDef({UNKNOWN, NEW, USED})
public @interface ProductConditionTypeDef {
    String UNKNOWN = "UNKNOWN";
    String NEW = "NEW";
    String USED = "USED";
}
