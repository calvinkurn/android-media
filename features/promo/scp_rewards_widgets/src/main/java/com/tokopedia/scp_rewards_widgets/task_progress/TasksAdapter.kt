package com.tokopedia.scp_rewards_widgets.task_progress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.scp_rewards_widgets.R
import com.tokopedia.scp_rewards_widgets.databinding.ItemTaskLayoutBinding

class TasksAdapter : ListAdapter<Task, TasksAdapter.TaskViewHolder>(TasksDiffUtil) {

    object TasksDiffUtil : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem == newItem
    }


    class TaskViewHolder(private val binding: ItemTaskLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(task: Task) {
            binding.tvTask.text = task.title
            if (task.isCompleted == true) {
                binding.ivCheck.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_radio_check))
            } else {
                binding.ivCheck.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_radio_uncheck))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(ItemTaskLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.setData(getItem(holder.bindingAdapterPosition))
    }
}
