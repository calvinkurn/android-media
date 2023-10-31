package com.tokopedia.digital.home.presentation.adapter.viewholder

import com.tokopedia.digital.home.R
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.databinding.ViewRechargeHomeListTodoWidgetBinding
import com.tokopedia.digital.home.databinding.ViewRechargeHomeTodoWidgetBayarSekaligusBinding
import com.tokopedia.digital.home.databinding.ViewRechargeHomeTodoWidgetBinding
import com.tokopedia.digital.home.model.RechargeHomepageTodoWidgetModel
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener

class RechargeHomepageTodoWidgetViewHolder (
    val binding: ViewRechargeHomeListTodoWidgetBinding,
    val listener: RechargeHomepageItemListener
): AbstractViewHolder<RechargeHomepageTodoWidgetModel>(binding.root) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_list_todo_widget
    }

    override fun bind(element: RechargeHomepageTodoWidgetModel) {
        if (element.section.items.isNotEmpty()) {
            with(binding) {
                titleTodoWidgetFirst.text = element.section.items.first().title
                titleTodoWidgetSecond.text = element.section.items.second().title
            }
        } else {
            listener.loadRechargeSectionData(element.section.id, element.section.name)
        }
    }

    class RechargeHomepageTodoWidgetPostReminderViewHolder(
        val binding: ViewRechargeHomeTodoWidgetBinding
    ): AbstractViewHolder<RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetPostReminderItemModel>(binding.root) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.view_recharge_home_todo_widget
        }

        override fun bind(element: RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetPostReminderItemModel?) {

        }
    }

    class RechargeHomepageTodoWidgetAutoPayViewHolder(
        val binding: ViewRechargeHomeTodoWidgetBinding
    ): AbstractViewHolder<RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayItemModel>(binding.root) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.view_recharge_home_todo_widget
        }

        override fun bind(element: RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetAutoPayItemModel?) {

        }
    }

    class RechargeHomepageTodoWidgetBayarSekaligusViewHolder(
        val binding: ViewRechargeHomeTodoWidgetBayarSekaligusBinding
    ): AbstractViewHolder<RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetBayarSekaligusItemModel>(binding.root) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.view_recharge_home_todo_widget_bayar_sekaligus
        }

        override fun bind(element: RechargeHomepageTodoWidgetModel.RechargeHomepageTodoWidgetBayarSekaligusItemModel?) {

        }
    }


    private fun <T> List<T>.second(): T {
        if (isEmpty())
            throw NoSuchElementException("List is empty.")
        return this[1]
    }
}
