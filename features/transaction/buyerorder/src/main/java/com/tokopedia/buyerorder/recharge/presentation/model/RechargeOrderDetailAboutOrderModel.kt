package com.tokopedia.buyerorder.recharge.presentation.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorder.recharge.presentation.adapter.RechargeOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 02/11/2021
 */
@Parcelize
data class RechargeOrderDetailAboutOrderModel(
        val helpUrl: String
) : Parcelable, Visitable<RechargeOrderDetailTypeFactory> {
    override fun type(typeFactory: RechargeOrderDetailTypeFactory?): Int =
            typeFactory?.type(this).orZero()
}