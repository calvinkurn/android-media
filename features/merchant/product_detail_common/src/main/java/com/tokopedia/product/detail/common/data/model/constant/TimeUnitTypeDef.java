package com.tokopedia.product.detail.common.data.model.constant;

import android.support.annotation.StringDef;

import static com.tokopedia.product.detail.common.data.model.constant.TimeUnitTypeDef.DAY;
import static com.tokopedia.product.detail.common.data.model.constant.TimeUnitTypeDef.MONTH;
import static com.tokopedia.product.detail.common.data.model.constant.TimeUnitTypeDef.UNKNOWN;
import static com.tokopedia.product.detail.common.data.model.constant.TimeUnitTypeDef.WEEK;

@StringDef({UNKNOWN, DAY, WEEK, MONTH})
public @interface TimeUnitTypeDef {
    String UNKNOWN = "UNKNOWN";
    String DAY = "DAY";
    String WEEK = "WEEK";
    String MONTH = "MONTH";
}
