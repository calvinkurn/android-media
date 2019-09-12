package com.tokopedia.merchantvoucher.common.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.merchantvoucher.common.constant.MerchantVoucherOwnerTypeDef.TYPE_MERCHANT;
import static com.tokopedia.merchantvoucher.common.constant.MerchantVoucherOwnerTypeDef.TYPE_TOKOPEDIA;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TYPE_MERCHANT, TYPE_TOKOPEDIA})
public @interface MerchantVoucherOwnerTypeDef {
    int TYPE_MERCHANT = 1;
    int TYPE_TOKOPEDIA = 2;
}
