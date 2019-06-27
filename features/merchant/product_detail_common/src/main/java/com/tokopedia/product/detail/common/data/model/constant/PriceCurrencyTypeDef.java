package com.tokopedia.product.detail.common.data.model.constant;

import android.support.annotation.StringDef;

import static com.tokopedia.product.detail.common.data.model.constant.PriceCurrencyTypeDef.IDR;
import static com.tokopedia.product.detail.common.data.model.constant.PriceCurrencyTypeDef.UNKNOWN;
import static com.tokopedia.product.detail.common.data.model.constant.PriceCurrencyTypeDef.USD;

@StringDef({UNKNOWN, IDR, USD})
public @interface PriceCurrencyTypeDef {
    String UNKNOWN = "UNKNOWN";
    String IDR = "IDR";
    String USD = "USD";
}
