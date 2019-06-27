package com.tokopedia.product.detail.common.data.model.constant;

import android.support.annotation.StringDef;

import static com.tokopedia.product.detail.common.data.model.constant.WeightTypeDef.GRAM;
import static com.tokopedia.product.detail.common.data.model.constant.WeightTypeDef.KILO;
import static com.tokopedia.product.detail.common.data.model.constant.WeightTypeDef.UNKNOWN;

@StringDef({UNKNOWN, GRAM, KILO})
public @interface WeightTypeDef {
    String UNKNOWN = "UNKNOWN";
    String GRAM = "GRAM";
    String KILO = "KILOGRAM";
}
