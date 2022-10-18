package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager
import com.tokopedia.kotlin.extensions.view.orZero
import java.io.Serializable

data class OrderInsuranceUiModel(
    val logoUrl: String = "",
    val title: String = "",
    val subtitle: String = "",
    val appLink: String = ""
) : BaseVisitableUiModel, Serializable {
    override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    override fun shouldShow(context: Context?): Boolean {
        return logoUrl.isNotBlank() &&
            title.isNotBlank() &&
            subtitle.isNotBlank() &&
            appLink.isNotBlank()
    }

    override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
        return null
    }
}
