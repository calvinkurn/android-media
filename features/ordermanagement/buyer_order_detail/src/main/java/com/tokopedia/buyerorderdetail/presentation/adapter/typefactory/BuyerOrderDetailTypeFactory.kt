package com.tokopedia.buyerorderdetail.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.ActionButtonClickListener
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ActionButtonsViewHolder
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel

class BuyerOrderDetailTypeFactory(private val actionButtonClickListener: ActionButtonClickListener) : BaseAdapterTypeFactory() {
    fun type(actionButtonsUiModel: ActionButtonsUiModel): Int {
        return ActionButtonsViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ActionButtonsViewHolder.LAYOUT -> ActionButtonsViewHolder(parent, actionButtonClickListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}