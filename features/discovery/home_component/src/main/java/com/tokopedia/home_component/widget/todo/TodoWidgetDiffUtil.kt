package com.tokopedia.home_component.widget.todo

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by frenzel
 */
internal open class TodoWidgetDiffUtil: DiffUtil.ItemCallback<TodoWidgetVisitable>() {

    override fun areItemsTheSame(
        oldItem: TodoWidgetVisitable,
        newItem: TodoWidgetVisitable
    ): Boolean = oldItem.getId() == newItem.getId()

    override fun areContentsTheSame(
        oldItem: TodoWidgetVisitable,
        newItem: TodoWidgetVisitable
    ): Boolean = oldItem.equalsWith(newItem)

    override fun getChangePayload(
        oldItem: TodoWidgetVisitable,
        newItem: TodoWidgetVisitable
    ): Any? {
        return oldItem.getChangePayloadFrom(newItem)
    }
}
