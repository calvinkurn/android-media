package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager
import com.tokopedia.kotlin.extensions.view.orZero

data class PlatformFeeInfoUiModel(
    val text: StringRes = StringRes(R.string.buyer_order_detail_platform_fee_info)
) : BaseVisitableUiModel {
    override fun shouldShow(context: Context?): Boolean {
        return true
    }

    override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
        return null
    }

    override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }
}
