package com.tokopedia.scp_rewards_widgets.task_progress

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.scp_rewards_common.R
import com.tokopedia.scp_rewards_widgets.databinding.WidgetMedalTaskProgressBinding
import com.tokopedia.unifycomponents.ProgressBarUnify

class WidgetMedalTaskProgress(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val binding = WidgetMedalTaskProgressBinding.inflate(LayoutInflater.from(context), this)

    init {
        background = ContextCompat.getDrawable(context, R.drawable.card_border)
        binding.progressBar.progressBarHeight = ProgressBarUnify.SIZE_MEDIUM

        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TasksAdapter()
        }
    }


    fun bindData(taskProgress: TaskProgress) {
        binding.tvProgress.text = taskProgress.title
        taskProgress.progress?.let {
            binding.progressBar.apply {
                setValue(it)
                rightIndicatorText = "$it%"
            }
        } ?: run { binding.progressBar.gone() }

        taskProgress.tasks?.let {
            (binding.rvTasks.adapter as TasksAdapter).submitList(it)
        }
    }

}
