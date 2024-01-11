package com.tokopedia.mvcwidget.views.benefit

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.mvcwidget.databinding.LayoutItemUsablePromoBinding
import kotlinx.parcelize.Parcelize

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
            binding.ivIcon.urlSrc = item.icon
            binding.tvName.text = item.text
            binding.tvValue.text = item.value
        }
    }

    private class DiffUtilCallback : DiffUtil.ItemCallback<UsablePromoModel>() {
        override fun areItemsTheSame(
            oldItem: UsablePromoModel,
            newItem: UsablePromoModel
        ): Boolean {
            return oldItem.text == newItem.text
        }

        override fun areContentsTheSame(
            oldItem: UsablePromoModel,
            newItem: UsablePromoModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}

@Parcelize
data class UsablePromoModel(
    val icon: String,
    val text: String,
    val value: String
) : Parcelable
