package com.tokopedia.scp_rewards_widgets.task_progress

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.scp_rewards_common.R
import com.tokopedia.scp_rewards_widgets.common.VerticalSpacing
import com.tokopedia.scp_rewards_widgets.databinding.WidgetMedalTaskProgressBinding
import com.tokopedia.unifycomponents.ProgressBarUnify
import java.util.*

class WidgetMedalTaskProgress(private val context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val binding = WidgetMedalTaskProgressBinding.inflate(LayoutInflater.from(context), this)


    private val taskAdapter: BaseAdapter<TasksViewTypeFactory> by lazy {
        BaseAdapter(TasksViewTypeFactory())
    }

    companion object {
        private const val ITEM_VERTICAL_SPACING = 8
    }

    init {
        background = ContextCompat.getDrawable(context, com.tokopedia.unifycomponents.R.drawable.card_border)
        binding.progressBar.progressBarHeight = ProgressBarUnify.SIZE_MEDIUM

        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
            addItemDecoration(VerticalSpacing(ITEM_VERTICAL_SPACING))
        }
    }


    fun bindData(taskProgress: TaskProgress) {
        binding.tvProgress.text = taskProgress.title
        taskProgress.progress?.let {
            binding.tvProgressPercent.text = String.format(Locale.getDefault(), context.getString(com.tokopedia.scp_rewards_widgets.R.string.progress_percent), it)
            binding.progressBar.setValue(it)
        } ?: run {
            binding.tvProgressPercent.gone()
            binding.progressBar.gone()
        }

        taskProgress.tasks.let {
            taskAdapter.setVisitables(it)
        }
    }

}
