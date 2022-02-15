package com.tokopedia.videoTabComponent.view.viewholder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.R
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.videoTabComponent.domain.model.data.PlaySlotTabMenuUiModel
import com.tokopedia.videoTabComponent.view.PlaySlotTabCallback

/**
 * Created by meyta.taliti on 01/02/22.
 */
class PlayFeedSlotTabViewHolder private constructor() {

    internal class SlotTab private constructor(
        itemView: View, tabClickListener: PlaySlotTabCallback
    ) : RecyclerView.ViewHolder(itemView) {

        private val rvSlotTab: RecyclerView = itemView.findViewById(R.id.rv_labels)
        private val adapter = SlotTabViewAdapter(tabClickListener)

        fun bind(item: PlaySlotTabMenuUiModel) {
            rvSlotTab.adapter = adapter
            adapter.setItems(item.items)
        }

        class SlotTabViewAdapter(private val listener: PlaySlotTabCallback?) :
            RecyclerView.Adapter<SlotTabViewHolder>() {

            private var selectedTab: Int = 0
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
                        .inflate(R.layout.item_play_slot_tab_card, parent, false),
                    listener
                )
            }

            override fun onBindViewHolder(holder: SlotTabViewHolder, position: Int) {
                holder.bind(mItems[position]) {
                    mItems[selectedTab].isSelected = false
                    notifyItemChanged(selectedTab)

                    selectedTab = holder.adapterPosition
                }
            }

            override fun getItemCount(): Int = mItems.size
        }

        class SlotTabViewHolder(
            itemView: View, private val listener: PlaySlotTabCallback?
        ) : RecyclerView.ViewHolder(itemView) {

            private val chip = itemView.findViewById<ChipsUnify>(R.id.cu_label)

            fun bind(slot: PlaySlotTabMenuUiModel.Item, unSelect: () -> Unit) {
                itemView.addOnImpressionListener(slot.impressHolder) {
                    listener?.impressTabMenu(slot)
                }
                updateChipView(chip, slot.isSelected)
                chip.chipText = slot.label

                chip.setOnClickListener {
                    if (slot.isSelected) return@setOnClickListener
                    slot.isSelected = true
                    unSelect()
                    updateChipView(chip, slot.isSelected)
                    listener?.clickTabMenu(slot)
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
