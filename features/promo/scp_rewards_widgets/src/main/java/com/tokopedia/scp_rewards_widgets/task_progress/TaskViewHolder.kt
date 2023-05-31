package com.tokopedia.scp_rewards_widgets.task_progress

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.databinding.ItemTaskLayoutBinding

class TaskViewHolder(itemView: View) : AbstractViewHolder<Task>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_task_layout
    }

    private var binding: ItemTaskLayoutBinding? = null

    init {
        binding = ItemTaskLayoutBinding.bind(itemView)
    }

    private fun setData(task: Task) {
        binding?.let {
            with(it) {
                tvTask.text = task.title
                if (task.isCompleted == true) {
                    ivCheck.setImageDrawable(ContextCompat.getDrawable(root.context, R.drawable.ic_radio_check))
                } else {
                    ivCheck.setImageDrawable(ContextCompat.getDrawable(root.context, R.drawable.ic_radio_uncheck))
                }
            }

        }
    }

    override fun bind(task: Task?) {
        task?.let {
            setData(it)
        }
    }
}
