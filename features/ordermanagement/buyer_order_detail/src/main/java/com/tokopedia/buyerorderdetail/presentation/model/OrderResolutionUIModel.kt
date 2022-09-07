package com.tokopedia.buyerorderdetail.presentation.model;

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OrderResolutionViewHolder
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager

data class OrderResolutionUIModel(
        val title: String? = "",
        val status: String? = "",
        val description: String? = "",
        val picture: String? = "",
        val showDeadline: Boolean? = false,
        val deadlineDateTime: String? = "",
        val backgroundColorUnify: String? = "",
        val backgroundColor: String? = "",
        val redirectPath: String? = ""
) : BaseVisitableUiModel {

        override fun shouldShow(context: Context?): Boolean {
                return !title.isNullOrEmpty() && !status.isNullOrEmpty() && !description.isNullOrEmpty()
        }

        override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
                return null
        }

        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
                return OrderResolutionViewHolder.LAYOUT
        }
}
