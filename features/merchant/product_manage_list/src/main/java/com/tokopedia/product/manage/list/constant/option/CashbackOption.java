package com.tokopedia.product.manage.list.constant.option;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.product.manage.list.constant.option.CashbackOption.CASHBACK_OPTION_3;
import static com.tokopedia.product.manage.list.constant.option.CashbackOption.CASHBACK_OPTION_4;
import static com.tokopedia.product.manage.list.constant.option.CashbackOption.CASHBACK_OPTION_5;
import static com.tokopedia.product.manage.list.constant.option.CashbackOption.CASHBACK_OPTION_NONE;

/**
 * Created by zulfikarrahman on 9/22/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({CASHBACK_OPTION_3, CASHBACK_OPTION_4, CASHBACK_OPTION_5, CASHBACK_OPTION_NONE})
public @interface CashbackOption {
    int CASHBACK_OPTION_3 = 3;
    int CASHBACK_OPTION_4 = 4;
    int CASHBACK_OPTION_5 = 5;
    int CASHBACK_OPTION_NONE = 0;
}
