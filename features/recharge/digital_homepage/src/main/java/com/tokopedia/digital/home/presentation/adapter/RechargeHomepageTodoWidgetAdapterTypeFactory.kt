package com.tokopedia.digital.home.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.databinding.ViewRechargeHomeTodoWidgetBayarSekaligusBinding
import com.tokopedia.digital.home.databinding.ViewRechargeHomeTodoWidgetBinding
import com.tokopedia.digital.home.model.RechargeHomepageTodoWidgetModel
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageTodoWidgetViewHolder

class RechargeHomepageTodoWidgetAdapterTypeFactory : BaseAdapterTypeFactory() {

    fun type(model : RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetPostReminderItemModel): Int {
        return RechargeHomepageTodoWidgetViewHolder.RechargeHomepageTodoWidgetPostReminderViewHolder.LAYOUT
    }

    fun type(model: RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayItemModel): Int {
        return RechargeHomepageTodoWidgetViewHolder.RechargeHomepageTodoWidgetAutoPayViewHolder.LAYOUT
    }

    fun type(model: RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetBayarSekaligusItemModel): Int {
        return RechargeHomepageTodoWidgetViewHolder.RechargeHomepageTodoWidgetBayarSekaligusViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            RechargeHomepageTodoWidgetViewHolder.RechargeHomepageTodoWidgetPostReminderViewHolder.LAYOUT -> {
                val binding = ViewRechargeHomeTodoWidgetBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                RechargeHomepageTodoWidgetViewHolder.RechargeHomepageTodoWidgetPostReminderViewHolder(binding)
            }

            RechargeHomepageTodoWidgetViewHolder.RechargeHomepageTodoWidgetAutoPayViewHolder.LAYOUT -> {
                val binding = ViewRechargeHomeTodoWidgetBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                RechargeHomepageTodoWidgetViewHolder.RechargeHomepageTodoWidgetAutoPayViewHolder(binding)
            }

            RechargeHomepageTodoWidgetViewHolder.RechargeHomepageTodoWidgetBayarSekaligusViewHolder.LAYOUT -> {
                val binding = ViewRechargeHomeTodoWidgetBayarSekaligusBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )

                RechargeHomepageTodoWidgetViewHolder.RechargeHomepageTodoWidgetBayarSekaligusViewHolder(binding)
            }

            else -> super.createViewHolder(parent, type)
        }
    }
}
