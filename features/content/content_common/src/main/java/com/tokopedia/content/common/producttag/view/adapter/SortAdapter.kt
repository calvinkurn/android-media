package com.tokopedia.content.common.producttag.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.content.common.R
import com.tokopedia.content.common.databinding.ItemContentCreationSortListBinding
import com.tokopedia.content.common.producttag.view.uimodel.SortUiModel

/**
 * Created By : Jonathan Darwin on May 20, 2022
 */
internal class SortAdapter(
    onSelected: (SortUiModel) -> Unit,
) : BaseDiffUtilAdapter<SortUiModel>() {

    init {
        delegatesManager.addDelegate(Delegate(onSelected))
    }

    override fun areItemsTheSame(oldItem: SortUiModel, newItem: SortUiModel): Boolean {
        return oldItem.text == newItem.text
    }

    override fun areContentsTheSame(oldItem: SortUiModel, newItem: SortUiModel): Boolean {
        return oldItem == newItem
    }

    private class Delegate(
        private val onSelected: (SortUiModel) -> Unit,
    ) : TypedAdapterDelegate<SortUiModel, SortUiModel, ViewHolder>(R.layout.view_cc_empty) {

        override fun onBindViewHolder(item: SortUiModel, holder: ViewHolder) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ViewHolder {
            return ViewHolder(
                ItemContentCreationSortListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                onSelected,
            )
        }
    }

    class ViewHolder(
        internal val binding: ItemContentCreationSortListBinding,
        private val onSelected: (SortUiModel) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                binding.rbSort.isChecked = true
            }
        }

        fun bind(item: SortUiModel) {
            binding.tvSortName.text = item.text

            binding.rbSort.setOnCheckedChangeListener(null)
            binding.rbSort.isChecked = item.isSelected
            binding.rbSort.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) onSelected(item)
            }
        }
    }
}