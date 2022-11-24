package com.tokopedia.mvc.presentation.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.mvc.databinding.SmvcItemFilterVoucherBinding
import com.tokopedia.unifycomponents.ChipsUnify

class FilterVoucherAdapter: RecyclerView.Adapter<FilterVoucherAdapter.ViewHolder>() {

    private var data: List<String> = emptyList()
    private var onClickListener: (position: Int) -> Unit = {}

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

    fun setDataList(newData: List<String>) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }

    fun setOnClickListener(listener: (position: Int) -> Unit) {
        onClickListener = listener
    }

    inner class ViewHolder(
        private val binding: SmvcItemFilterVoucherBinding,
        private val onClickListener: (position: Int) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.chipsItem.setOnClickListener {
                binding.chipsItem.chipType =
                    if (binding.chipsItem.chipType == ChipsUnify.TYPE_SELECTED)
                        ChipsUnify.TYPE_NORMAL
                    else ChipsUnify.TYPE_SELECTED
                onClickListener(absoluteAdapterPosition)
            }
        }
        fun bind(item: String) {
            with(binding) {
                chipsItem.chipText = item
            }
        }
    }
}
