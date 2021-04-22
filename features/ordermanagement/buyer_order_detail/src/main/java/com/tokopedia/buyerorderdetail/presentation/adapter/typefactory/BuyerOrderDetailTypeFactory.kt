package com.tokopedia.buyerorderdetail.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.ActionButtonClickListener
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ActionButtonsViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.BuyProtectionViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ThickDividerViewHolder
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BuyProtectionUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ThickDividerUiModel

class BuyerOrderDetailTypeFactory(
        private val actionButtonClickListener: ActionButtonClickListener,
        private val buyProtectionListener: BuyProtectionViewHolder.BuyProtectionListener) : BaseAdapterTypeFactory() {
    fun type(actionButtonsUiModel: ActionButtonsUiModel): Int {
        return ActionButtonsViewHolder.LAYOUT
    }

    fun type(buyProtectionUiModel: BuyProtectionUiModel): Int {
        return BuyProtectionViewHolder.LAYOUT
    }

    fun type(thickDividerUiModel: ThickDividerUiModel): Int {
        return ThickDividerViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ActionButtonsViewHolder.LAYOUT -> ActionButtonsViewHolder(parent, actionButtonClickListener)
            BuyProtectionViewHolder.LAYOUT -> BuyProtectionViewHolder(parent, buyProtectionListener)
            ThickDividerViewHolder.LAYOUT -> ThickDividerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}