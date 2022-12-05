package com.tokopedia.mvc.presentation.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.mvc.databinding.SmvcItemFilterVoucherBinding
import com.tokopedia.unifycomponents.ChipsUnify

class FilterVoucherAdapter: RecyclerView.Adapter<FilterVoucherAdapter.ViewHolder>() {

    private var data: List<Pair<String, Boolean>> = emptyList()
    private var onClickListener: (position: Int, isSelected: Boolean) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SmvcItemFilterVoucherBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(binding, onClickListener)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    fun setDataList(newData: List<Pair<String, Boolean>>) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }

    fun setSelectionAt(selectedIndex: Int) {
        data = data.mapIndexed { index, data -> Pair(data.first, index == selectedIndex) }
        notifyItemRangeChanged(Int.ZERO, data.size)
    }

    fun resetSelection() {
        data = data.map { Pair(it.first, false) }
        notifyItemRangeChanged(Int.ZERO, data.size)
    }

    fun setOnClickListener(listener: (position: Int, isSelected: Boolean) -> Unit) {
        onClickListener = listener
    }

    inner class ViewHolder(
        private val binding: SmvcItemFilterVoucherBinding,
        private val onClickListener: (position: Int, isSelected: Boolean) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.chipsItem.setOnClickListener {
                val isSelected = binding.chipsItem.chipType == ChipsUnify.TYPE_SELECTED
                binding.chipsItem.chipType = if (isSelected) ChipsUnify.TYPE_NORMAL else ChipsUnify.TYPE_SELECTED
                onClickListener(absoluteAdapterPosition, isSelected)
            }
        }
        fun bind(item: Pair<String, Boolean>) {
            with(binding) {
                chipsItem.chipType = if (!item.second) ChipsUnify.TYPE_NORMAL else ChipsUnify.TYPE_SELECTED
                chipsItem.chipText = item.first
            }
        }
    }
}
