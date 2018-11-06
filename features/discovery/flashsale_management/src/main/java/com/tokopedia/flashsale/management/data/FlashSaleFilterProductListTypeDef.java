package com.tokopedia.flashsale.management.data;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.flashsale.management.data.FlashSaleFilterProductListTypeDef.TYPE_ALL;
import static com.tokopedia.flashsale.management.data.FlashSaleFilterProductListTypeDef.TYPE_NON_SUBMITTED;
import static com.tokopedia.flashsale.management.data.FlashSaleFilterProductListTypeDef.TYPE_SUBMITTED;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TYPE_ALL, TYPE_SUBMITTED, TYPE_NON_SUBMITTED})
public @interface FlashSaleFilterProductListTypeDef {
    int TYPE_ALL = 0;
    int TYPE_SUBMITTED = 1;
    int TYPE_NON_SUBMITTED = 3;
}
