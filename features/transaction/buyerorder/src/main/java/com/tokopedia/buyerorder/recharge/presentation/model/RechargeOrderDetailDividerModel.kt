package com.tokopedia.buyerorder.recharge.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorder.recharge.presentation.adapter.RechargeOrderDetailTypeFactory

/**
 * @author by furqan on 01/11/2021
 */
class RechargeOrderDetailDividerModel : Visitable<RechargeOrderDetailTypeFactory> {
    override fun type(typeFactory: RechargeOrderDetailTypeFactory?): Int =
            typeFactory?.type(this).orZero()
}