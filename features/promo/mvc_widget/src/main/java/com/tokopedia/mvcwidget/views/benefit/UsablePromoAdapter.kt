package com.tokopedia.mvcwidget.views.benefit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.mvcwidget.databinding.LayoutItemUsablePromoBinding
import com.tokopedia.mvcwidget.utils.getUnifyColorFromHex
import com.tokopedia.mvcwidget.utils.setAttribute

class UsablePromoAdapter :
    ListAdapter<UsablePromoModel, UsablePromoAdapter.ItemViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = LayoutItemUsablePromoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewHolder(private val binding: LayoutItemUsablePromoBinding) :
        ViewHolder(binding.root) {
        fun bind(item: UsablePromoModel) {
            binding.ivIcon.run {
                urlSrc = item.icon
                cornerRadius = 0
            }
            binding.tvName.setAttribute(
                item.title,
                binding.root.context.getUnifyColorFromHex(item.titleColor),
                item.titleFormat
            )
            binding.tvValue.setAttribute(
                item.text,
                binding.root.context.getUnifyColorFromHex(item.textColor),
                item.textFormat
            )
        }
    }

    private class DiffUtilCallback : DiffUtil.ItemCallback<UsablePromoModel>() {
        override fun areItemsTheSame(
            oldItem: UsablePromoModel,
            newItem: UsablePromoModel
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: UsablePromoModel,
            newItem: UsablePromoModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}
