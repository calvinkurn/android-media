package com.tokopedia.topads.common.data.internal

import androidx.annotation.StringDef
import com.tokopedia.topads.common.data.internal.NonDeliveryReason.Companion.NO_BALANCE
import com.tokopedia.topads.common.data.internal.NonDeliveryReason.Companion.OUT_OF_BUDGET
import com.tokopedia.topads.common.data.internal.NonDeliveryReason.Companion.OUT_OF_STOCK
import com.tokopedia.topads.common.data.internal.NonDeliveryReason.Companion.SHOP_INACTIVE

/**
 * Created by Pika on 18/9/20.
 */

@StringDef(SHOP_INACTIVE, NO_BALANCE, OUT_OF_BUDGET, OUT_OF_STOCK)
@Retention(AnnotationRetention.SOURCE)
annotation class NonDeliveryReason{
    companion object{
        const val SHOP_INACTIVE = "shop_inactive"
        const val NO_BALANCE = "no_balance"
        const val OUT_OF_BUDGET = "run_out_budget"
        const val OUT_OF_STOCK = "out_of_stock"
    }
}
