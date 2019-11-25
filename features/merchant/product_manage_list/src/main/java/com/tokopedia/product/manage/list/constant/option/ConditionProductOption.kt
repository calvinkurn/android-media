package com.tokopedia.product.manage.list.constant.option

import androidx.annotation.StringDef
import com.tokopedia.product.manage.list.constant.option.ConditionProductOption.Companion.ALL_CONDITION
import com.tokopedia.product.manage.list.constant.option.ConditionProductOption.Companion.NEW
import com.tokopedia.product.manage.list.constant.option.ConditionProductOption.Companion.USED

@Retention(AnnotationRetention.SOURCE)
@StringDef(NEW, USED, ALL_CONDITION)
annotation class ConditionProductOption {
    companion object {
        const val NEW = "1"
        const val USED = "2"
        const val ALL_CONDITION = "-1"
    }
}