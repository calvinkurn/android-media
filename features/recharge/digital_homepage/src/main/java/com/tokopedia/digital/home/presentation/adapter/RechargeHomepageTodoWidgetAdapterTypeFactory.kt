package com.tokopedia.digital.home.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.databinding.ViewRechargeHomeTodoWidgetAutopayBinding
import com.tokopedia.digital.home.model.RechargeHomepageTodoWidgetModel
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageTodoWidgetAutoPayViewHolder
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageTodoWidgetCloseProcessListener
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageTodoWidgetListener
import com.tokopedia.digital.home.presentation.listener.TodoWidgetItemListener

class RechargeHomepageTodoWidgetAdapterTypeFactory(
    private val rechargeHomepageTodoWidgetListener: RechargeHomepageTodoWidgetListener,
    private val closeItemListener: RechargeHomepageTodoWidgetCloseProcessListener,
    private val todoWidgetItemListener: TodoWidgetItemListener
) : BaseAdapterTypeFactory() {

    fun type(model: RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayPostReminderItemModel): Int {
        return RechargeHomepageTodoWidgetAutoPayViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            RechargeHomepageTodoWidgetAutoPayViewHolder.LAYOUT -> {
                val binding = ViewRechargeHomeTodoWidgetAutopayBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                RechargeHomepageTodoWidgetAutoPayViewHolder(binding, rechargeHomepageTodoWidgetListener,
                    closeItemListener, todoWidgetItemListener)
            }

            else -> super.createViewHolder(parent, type)
        }
    }
}
