package com.tokopedia.play.broadcaster.setup.product.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ItemSortListBinding
import com.tokopedia.play.broadcaster.setup.product.view.model.SortListModel

/**
 * Created by kenny.hadisaputra on 02/02/22
 */
internal class SortListAdapter(
    onSelected: (SortListModel) -> Unit,
) : BaseDiffUtilAdapter<SortListModel>() {

    init {
        delegatesManager.addDelegate(Delegate(onSelected))
    }

    override fun areItemsTheSame(oldItem: SortListModel, newItem: SortListModel): Boolean {
        return oldItem.sort == newItem.sort
    }

    override fun areContentsTheSame(oldItem: SortListModel, newItem: SortListModel): Boolean {
        return oldItem == newItem
    }

    private class Delegate(
        private val onSelected: (SortListModel) -> Unit,
    ) : TypedAdapterDelegate<SortListModel, SortListModel, ViewHolder>(R.layout.view_empty) {

        override fun onBindViewHolder(item: SortListModel, holder: ViewHolder) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ViewHolder {
            return ViewHolder(
                ItemSortListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                onSelected,
            )
        }
    }

    private class ViewHolder(
        private val binding: ItemSortListBinding,
        private val onSelected: (SortListModel) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                binding.rbSort.isChecked = true
            }
        }

        fun bind(item: SortListModel) {
            binding.tvSortName.text = item.sort.text

            binding.rbSort.setOnCheckedChangeListener(null)
            binding.rbSort.isChecked = item.isSelected
            binding.rbSort.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) onSelected(item)
            }
        }
    }
}