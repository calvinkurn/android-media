package com.tokopedia.videoTabComponent.view.viewholder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedcomponent.util.util.scrollLayout
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.R
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.videoTabComponent.callback.PlaySlotTabCallback
import com.tokopedia.videoTabComponent.domain.model.data.PlaySlotTabMenuUiModel

/**
 * Created by meyta.taliti on 01/02/22.
 */
class PlayFeedSlotTabViewHolder private constructor() {

    internal class SlotTab private constructor(
        itemView: View, private val tabClickListener: PlaySlotTabCallback
    ) : RecyclerView.ViewHolder(itemView), PlaySlotTabCallback {

        private val rvSlotTab: RecyclerView = itemView.findViewById(R.id.rv_labels)
        private val adapter = SlotTabViewAdapter(this)

        fun bind(item: PlaySlotTabMenuUiModel) {
            rvSlotTab.adapter = adapter
            adapter.setItems(item.items)

            item.items.forEachIndexed { index, obj ->
                if (obj.isSelected) rvSlotTab.scrollLayout(index)
            }
        }

        override fun clickTabMenu(item: PlaySlotTabMenuUiModel.Item, position: Int) {
            tabClickListener.clickTabMenu(item, position)
            rvSlotTab.scrollLayout(position)
        }

        override fun impressTabMenu(item: PlaySlotTabMenuUiModel.Item) {
            tabClickListener.impressTabMenu(item)
        }

        class SlotTabViewAdapter(
            private val listener: PlaySlotTabCallback?
        ) : RecyclerView.Adapter<SlotTabViewHolder>() {

            private var selectedTabMenuPosition: Int = 0
            private val mItems: MutableList<PlaySlotTabMenuUiModel.Item> = mutableListOf()

            @SuppressLint("NotifyDataSetChanged")
            fun setItems(labels: List<PlaySlotTabMenuUiModel.Item>) {
                mItems.clear()
                mItems.addAll(labels)
                notifyDataSetChanged()
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotTabViewHolder {
                return SlotTabViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_play_slot_tab_card, parent, false)
                )
            }

            override fun onBindViewHolder(
                holder: SlotTabViewHolder, position: Int
            ) {
                val slot = mItems[holder.adapterPosition]
                holder.bind(slot) {
                    mItems[selectedTabMenuPosition].isSelected = false
                    notifyItemChanged(selectedTabMenuPosition)
                    listener?.clickTabMenu(slot, holder.adapterPosition)
                    selectedTabMenuPosition = holder.adapterPosition
                }
                holder.itemView.addOnImpressionListener(slot.impressHolder) {
                    listener?.impressTabMenu(slot)
                }
            }

            override fun getItemCount(): Int = mItems.size
        }

        class SlotTabViewHolder(
            itemView: View
        ) : RecyclerView.ViewHolder(itemView) {

            private val chip = itemView.findViewById<ChipsUnify>(R.id.cu_label)

            fun bind(slot: PlaySlotTabMenuUiModel.Item, chipSelected: () -> Unit) {
                updateChipView(chip, slot.isSelected)
                chip.chipText = slot.label

                chip.setOnClickListener {
                    if (slot.isSelected) return@setOnClickListener
                    slot.isSelected = true
                    updateChipView(chip, slot.isSelected)
                    chipSelected()
                }
            }

            private fun updateChipView(chip: ChipsUnify, isSelected: Boolean) {
                chip.chipType = if (isSelected) {
                    ChipsUnify.TYPE_SELECTED
                } else ChipsUnify.TYPE_NORMAL
            }
        }

        companion object {
            fun create(itemView: View, listener: PlaySlotTabCallback) =
                SlotTab(itemView, listener)
        }

    }
}
