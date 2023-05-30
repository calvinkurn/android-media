package com.tokopedia.scp_rewards_widgets.task_progress

data class TaskProgress(
    val title: String? = null,
    val progress: Int? = 0,
    val tasks: List<Task>? = emptyList(),
)

data class Task(
    val title: String? = null,
    val isCompleted: Boolean? = false,
)
