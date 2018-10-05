package com.tokopedia.merchantvoucher.common.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef.TYPE_AVAILABLE;
import static com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef.TYPE_IN_USE;
import static com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef.TYPE_OUT_OF_STOCK;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TYPE_AVAILABLE, TYPE_IN_USE, TYPE_OUT_OF_STOCK})
public @interface MerchantVoucherStatusTypeDef {
    int TYPE_AVAILABLE = 1;
    int TYPE_IN_USE = 2;
    int TYPE_OUT_OF_STOCK = 3;
}
