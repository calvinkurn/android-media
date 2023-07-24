package com.tokopedia.scp_rewards_widgets.task_progress

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class TasksViewTypeFactory : BaseAdapterTypeFactory() {

    fun type(model: Task) = TaskViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            TaskViewHolder.LAYOUT -> TaskViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
