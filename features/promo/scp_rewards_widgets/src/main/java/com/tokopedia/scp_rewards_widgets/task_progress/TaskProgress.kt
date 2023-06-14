package com.tokopedia.scp_rewards_widgets.task_progress

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class TaskProgress(
    val title: String? = null,
    val progress: Int? = 0,
    val tasks: List<Task> = emptyList(),
)

data class Task(
    val title: String? = null,
    val isCompleted: Boolean? = false,
    val progressInfo: String? = null,
) : Visitable<TasksViewTypeFactory> {
    override fun type(typeFactory: TasksViewTypeFactory): Int = typeFactory.type(this)
}
