package com.tokopedia.analyticsdebugger.serverlogger.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.databinding.ItemPriorityChipsBinding
import com.tokopedia.analyticsdebugger.serverlogger.presentation.uimodel.ItemPriorityUiModel
import com.tokopedia.unifycomponents.ChipsUnify

class PriorityServerLoggerAdapter(private val serverLoggerListener: ServerLoggerListener) :
    RecyclerView.Adapter<PriorityServerLoggerAdapter.PriorityServerLoggerViewHolder>() {

    private val priorityLoggerList = mutableListOf<ItemPriorityUiModel>()

    fun setPriorityList(items: List<ItemPriorityUiModel>) {
        if (items.isNullOrEmpty()) return
        this.priorityLoggerList.clear()
        this.priorityLoggerList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PriorityServerLoggerViewHolder {
        return PriorityServerLoggerViewHolder(
            ItemPriorityChipsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PriorityServerLoggerViewHolder, position: Int) {
        holder.bind(priorityLoggerList[position])
    }

    override fun onBindViewHolder(
        holder: PriorityServerLoggerViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemCount(): Int = priorityLoggerList.size

    inner class PriorityServerLoggerViewHolder(private val binding: ItemPriorityChipsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemPriorityUiModel) {
            with(binding) {
                chipsPriority.chipText = item.priorityName
                chipsPriority.chipSize = ChipsUnify.SIZE_MEDIUM
                chipsPriority.chipType =
                    if (item.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
                chipsPriority.setOnClickListener {
                    serverLoggerListener.onChipsClicked(
                        adapterPosition,
                        chipsPriority.chipText.orEmpty()
                    )
                }
            }
        }
    }
}