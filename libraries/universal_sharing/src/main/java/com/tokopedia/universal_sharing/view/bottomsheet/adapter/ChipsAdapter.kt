package com.tokopedia.universal_sharing.view.bottomsheet.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.universal_sharing.view.bottomsheet.viewholder.ChipViewHolder
import com.tokopedia.universal_sharing.view.model.ChipProperties

class ChipsAdapter constructor(
    private val listener: Listener?
) : RecyclerView.Adapter<ChipViewHolder>(), ChipViewHolder.Listener {

    private val chipList = mutableListOf<ChipProperties>()
    private var selectedFilter: ChipProperties? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        return ChipViewHolder.create(parent, this)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        holder.onBind(chipList[position])
    }

    override fun getItemCount() = chipList.size

    override fun onChipSelected(chip: ChipProperties) {
        deselectOtherSelectedFilter(chip)
        notifyChipChanged(chip)
    }

    override fun isSelected(chip: ChipProperties): Boolean {
        return selectedFilter == chip
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ChipProperties>?) {
        if (data == null) return

        chipList.clear()
        chipList.addAll(data)

        setDefaultChipSelectedState(data)
        notifyDataSetChanged()
    }

    private fun setDefaultChipSelectedState(data: List<ChipProperties>?) {
        if (data == null) return
        if (data.isEmpty()) return

        selectedFilter = data.find { it.isSelected } ?: return
        selectedFilter?.let { notifyChipChanged(it) }
    }

    private fun deselectOtherSelectedFilter(element: ChipProperties) {
        if (isSelected(element) || selectedFilter == null) return

        val previouslySelectedChipPosition = chipList.indexOf(selectedFilter)

        if (previouslySelectedChipPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(previouslySelectedChipPosition)
        }
    }

    private fun notifyChipChanged(element: ChipProperties) {
        selectedFilter = if (isSelected(element)) selectedFilter else element
        listener?.onChipChanged(element)
    }

    interface Listener {
        fun onChipChanged(chip: ChipProperties)
    }
}
