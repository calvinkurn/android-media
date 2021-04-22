package com.tokopedia.buyerorderdetail.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.ActionButtonClickListener
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.*
import com.tokopedia.buyerorderdetail.presentation.model.*

class BuyerOrderDetailTypeFactory(
        private val actionButtonClickListener: ActionButtonClickListener,
        private val buyProtectionListener: BuyProtectionViewHolder.BuyProtectionListener) : BaseAdapterTypeFactory() {
    fun type(actionButtonsUiModel: ActionButtonsUiModel): Int {
        return ActionButtonsViewHolder.LAYOUT
    }

    fun type(buyProtectionUiModel: BuyProtectionUiModel): Int {
        return BuyProtectionViewHolder.LAYOUT
    }

    fun type(courierDriverInfoUiModel: CourierDriverInfoUiModel): Int {
        return CourierDriverInfoViewHolder.LAYOUT
    }

    fun type(thickDividerUiModel: ThickDividerUiModel): Int {
        return ThickDividerViewHolder.LAYOUT
    }

    fun type(thinDashedDividerUiModel: ThinDashedDividerUiModel): Int {
        return ThinDashedDividerViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ActionButtonsViewHolder.LAYOUT -> ActionButtonsViewHolder(parent, actionButtonClickListener)
            CourierDriverInfoViewHolder.LAYOUT -> CourierDriverInfoViewHolder(parent)
            BuyProtectionViewHolder.LAYOUT -> BuyProtectionViewHolder(parent, buyProtectionListener)
            ThickDividerViewHolder.LAYOUT -> ThickDividerViewHolder(parent)
            ThinDashedDividerViewHolder.LAYOUT -> ThinDashedDividerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}