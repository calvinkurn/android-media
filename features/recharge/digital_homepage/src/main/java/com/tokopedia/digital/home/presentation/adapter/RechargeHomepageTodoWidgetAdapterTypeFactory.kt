package com.tokopedia.digital.home.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.databinding.ViewRechargeHomeTodoWidgetAutopayBinding
import com.tokopedia.digital.home.model.RechargeHomepageTodoWidgetModel
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageTodoWidgetViewHolder

class RechargeHomepageTodoWidgetAdapterTypeFactory(
    private val rechargeHomepageTodoWidgetListener: RechargeHomepageTodoWidgetViewHolder.RechargeHomepageTodoWidgetListener,
    private val closeItemListener: RechargeHomepageTodoWidgetViewHolder.RechargeHomepageTodoWidgetCloseProcess,
) : BaseAdapterTypeFactory() {

    fun type(model: RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayPostReminderItemModel): Int {
        return RechargeHomepageTodoWidgetViewHolder.RechargeHomepageTodoWidgetAutoPayViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            RechargeHomepageTodoWidgetViewHolder.RechargeHomepageTodoWidgetAutoPayViewHolder.LAYOUT -> {
                val binding = ViewRechargeHomeTodoWidgetAutopayBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                RechargeHomepageTodoWidgetViewHolder.RechargeHomepageTodoWidgetAutoPayViewHolder(binding, rechargeHomepageTodoWidgetListener, closeItemListener)
            }

            else -> super.createViewHolder(parent, type)
        }
    }
}
