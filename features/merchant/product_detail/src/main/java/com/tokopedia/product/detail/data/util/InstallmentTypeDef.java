package com.tokopedia.product.detail.data.util;


import android.support.annotation.IntDef;

import static com.tokopedia.product.detail.data.util.InstallmentTypeDef.MONTH_12;
import static com.tokopedia.product.detail.data.util.InstallmentTypeDef.MONTH_3;
import static com.tokopedia.product.detail.data.util.InstallmentTypeDef.MONTH_6;

@IntDef({MONTH_3, MONTH_6, MONTH_12})
public @interface InstallmentTypeDef {
    int MONTH_3 = 3;
    int MONTH_6 = 6;
    int MONTH_12 = 12;
}
