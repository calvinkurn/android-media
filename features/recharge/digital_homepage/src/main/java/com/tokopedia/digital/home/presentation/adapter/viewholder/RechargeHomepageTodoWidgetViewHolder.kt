package com.tokopedia.digital.home.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeListTodoWidgetBinding
import com.tokopedia.digital.home.model.RechargeHomepageTodoWidgetModel
import com.tokopedia.digital.home.presentation.adapter.RechargeHomepageTodoWidgetAdapter
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageTodoWidgetListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class RechargeHomepageTodoWidgetViewHolder(
    val binding: ViewRechargeHomeListTodoWidgetBinding,
    val listener: RechargeHomepageItemListener,
    private val todoWidgetListener: RechargeHomepageTodoWidgetListener,
) : AbstractViewHolder<RechargeHomepageTodoWidgetModel>(binding.root) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_recharge_home_list_todo_widget
    }

    override fun bind(element: RechargeHomepageTodoWidgetModel) {
        if (element.section.items.isNotEmpty()) {
            with(binding.rvTodoWidgetParent) {
                show()
                adapter = RechargeHomepageTodoWidgetAdapter(
                    element.section.items,
                    todoWidgetListener,
                    element.section.id,
                    element.section.name
                )
                layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
                binding.root.addOnImpressionListener(element.section) {
                    listener.onRechargeSectionItemImpression(element.section)
                }
            }
        } else {
            with(binding.rvTodoWidgetParent) {
                hide()
            }
            listener.loadRechargeSectionData(element.section.id, element.section.name)
        }
    }
}
