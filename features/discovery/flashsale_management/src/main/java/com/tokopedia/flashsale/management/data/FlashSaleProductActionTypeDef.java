package com.tokopedia.flashsale.management.data;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.flashsale.management.data.FlashSaleProductActionTypeDef.CANCEL;
import static com.tokopedia.flashsale.management.data.FlashSaleProductActionTypeDef.NO_ACTION;
import static com.tokopedia.flashsale.management.data.FlashSaleProductActionTypeDef.RE_RESERVE;
import static com.tokopedia.flashsale.management.data.FlashSaleProductActionTypeDef.UNDO_CANCEL;
import static com.tokopedia.flashsale.management.data.FlashSaleProductActionTypeDef.RESERVE;

@Retention(RetentionPolicy.SOURCE)
@IntDef({NO_ACTION, RESERVE, CANCEL, UNDO_CANCEL, RE_RESERVE})
public @interface FlashSaleProductActionTypeDef {
    int NO_ACTION = -1;
    int RESERVE = 0;
    int CANCEL = 1;
    int UNDO_CANCEL = 2;
    int RE_RESERVE = 3;
}
