package com.tokopedia.product.manage.list.constant.option;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.product.manage.list.constant.option.ConditionProductOption.ALL_CONDITION;
import static com.tokopedia.product.manage.list.constant.option.ConditionProductOption.USED;
import static com.tokopedia.product.manage.list.constant.option.ConditionProductOption.NEW;

/**
 * Created by zulfikarrahman on 9/22/17.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({NEW, USED, ALL_CONDITION})
public @interface StatusProductOption {
    String EMPTY = "3";
    String UNDER_SUPERVISION = "-1";
}
