package com.tokopedia.merchantvoucher.common.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.merchantvoucher.common.constant.MerchantVoucherTypeDef.TYPE_CASHBACK;
import static com.tokopedia.merchantvoucher.common.constant.MerchantVoucherTypeDef.TYPE_DISCOUNT;
import static com.tokopedia.merchantvoucher.common.constant.MerchantVoucherTypeDef.TYPE_FREE_ONGKIR;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TYPE_FREE_ONGKIR, TYPE_DISCOUNT, TYPE_CASHBACK})
public @interface MerchantVoucherTypeDef {
    int TYPE_FREE_ONGKIR = 1;
    int TYPE_DISCOUNT = 2;
    int TYPE_CASHBACK = 3;
}
