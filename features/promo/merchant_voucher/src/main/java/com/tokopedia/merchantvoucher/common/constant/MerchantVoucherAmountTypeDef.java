package com.tokopedia.merchantvoucher.common.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.merchantvoucher.common.constant.MerchantVoucherAmountTypeDef.TYPE_FIXED;
import static com.tokopedia.merchantvoucher.common.constant.MerchantVoucherAmountTypeDef.TYPE_PERCENTAGE;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TYPE_FIXED, TYPE_PERCENTAGE})
public @interface MerchantVoucherAmountTypeDef {
    int TYPE_FIXED = 1;
    int TYPE_PERCENTAGE = 2;
}
