package com.tokopedia.people.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.people.databinding.UpItemOptionBinding

/**
 * Created by kenny.hadisaputra on 28/11/22
 */
class UserProfileOptionAdapter : ListAdapter<Option, OptionViewHolder>(
    object : DiffUtil.ItemCallback<Option>() {
        override fun areItemsTheSame(oldItem: Option, newItem: Option): Boolean {
            return oldItem.text == newItem.text
        }

        override fun areContentsTheSame(oldItem: Option, newItem: Option): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        return OptionViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class OptionViewHolder(
    private val binding: UpItemOptionBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Option) {
        binding.root.text = item.text
        binding.root.setTextColor(item.color)
        binding.root.setOnClickListener {
            item.onClicked()
        }
    }

    companion object {
        fun create(parent: ViewGroup): OptionViewHolder {
            return OptionViewHolder(
                UpItemOptionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}

data class Option(
    val text: String,
    @ColorInt val color: Int,
    val onClicked: () -> Unit,
)
