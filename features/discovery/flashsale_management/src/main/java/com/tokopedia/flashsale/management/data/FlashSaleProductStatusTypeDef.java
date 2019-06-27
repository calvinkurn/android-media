package com.tokopedia.flashsale.management.data;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.flashsale.management.data.FlashSaleProductStatusTypeDef.NOTHING;
import static com.tokopedia.flashsale.management.data.FlashSaleProductStatusTypeDef.REJECTED;
import static com.tokopedia.flashsale.management.data.FlashSaleProductStatusTypeDef.RESERVE;
import static com.tokopedia.flashsale.management.data.FlashSaleProductStatusTypeDef.SUBMITTED;
import static com.tokopedia.flashsale.management.data.FlashSaleProductStatusTypeDef.SUBMIT_CANCEL;
import static com.tokopedia.flashsale.management.data.FlashSaleProductStatusTypeDef.SUBMIT_CANCEL_SUBMIT;

@Retention(RetentionPolicy.SOURCE)
@IntDef({NOTHING, SUBMITTED, REJECTED, RESERVE, SUBMIT_CANCEL, SUBMIT_CANCEL_SUBMIT})
public @interface FlashSaleProductStatusTypeDef {
    int NOTHING = 0;
    int SUBMITTED = 1;
    int REJECTED = 2;
    int RESERVE = 3;
    int SUBMIT_CANCEL = 4;
    int SUBMIT_CANCEL_SUBMIT = 5;
}
