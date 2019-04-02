package com.tokopedia.merchantvoucher.common.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef.TYPE_AVAILABLE;
import static com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef.TYPE_IN_USE;
import static com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef.TYPE_RUN_OUT;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TYPE_AVAILABLE, TYPE_IN_USE, TYPE_RUN_OUT})
public @interface MerchantVoucherStatusTypeDef {
    int TYPE_IN_USE = 1;
    int TYPE_AVAILABLE = 2;
    int TYPE_RUN_OUT = 3;
}
