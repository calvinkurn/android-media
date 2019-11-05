package com.tokopedia.shop.common.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.shop.common.constant.ShopScheduleActionDef.CLOSED;
import static com.tokopedia.shop.common.constant.ShopScheduleActionDef.OPEN;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({CLOSED, OPEN})
public @interface ShopScheduleActionDef {
    int CLOSED = 0;
    int OPEN = 1;
}
