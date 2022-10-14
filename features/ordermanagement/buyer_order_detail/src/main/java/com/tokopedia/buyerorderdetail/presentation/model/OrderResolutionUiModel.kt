package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OrderResolutionViewHolder
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager

data class OrderResolutionUiModel(
    val title: String = "",
    val status: String = "",
    val description: String = "",
    val picture: String = "",
    val showDeadline: Boolean = false,
    val deadlineDateTime: String = "",
    val backgroundColor: String = "",
    val redirectPath: String = "",
    val resolutionStatusFontColor: String = ""
) : BaseVisitableUiModel {

    override fun shouldShow(context: Context?): Boolean {
        val hasValidResolutionDeadline = showDeadline
                && deadlineDateTime.isNotBlank()
                && backgroundColor.isNotBlank()
        return title.isNotBlank()
                && status.isNotBlank()
                && description.isNotBlank()
                && picture.isNotBlank()
                && redirectPath.isNotBlank()
                && resolutionStatusFontColor.isNotBlank()
                && (!showDeadline || hasValidResolutionDeadline)
    }

    override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
        return null
    }

    override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
        return OrderResolutionViewHolder.LAYOUT
    }
}
