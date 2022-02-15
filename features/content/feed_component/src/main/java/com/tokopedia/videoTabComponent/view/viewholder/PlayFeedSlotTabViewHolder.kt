package com.tokopedia.videoTabComponent.view.viewholder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetSlotTabUiModel
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by meyta.taliti on 01/02/22.
 */
class PlayFeedSlotTabViewHolder private constructor() {

    internal class SlotTab private constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val rvSlotTab: RecyclerView = itemView.findViewById(R.id.rv_labels)
        private val adapter = SlotTabViewAdapter()

        fun bind(item: PlayWidgetSlotTabUiModel) {
            rvSlotTab.adapter = adapter
            adapter.setItems(item.labels)
        }

        class SlotTabViewAdapter : RecyclerView.Adapter<SlotTabViewHolder>() {

            private val mItems: MutableList<Pair<String, Boolean>> = mutableListOf()

            @SuppressLint("NotifyDataSetChanged")
            fun setItems(labels: List<Pair<String, Boolean>>) {
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

            override fun onBindViewHolder(holder: SlotTabViewHolder, position: Int) {
                holder.bind(mItems[position])
            }

            override fun getItemCount(): Int = mItems.size

        }

        class SlotTabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val chip = itemView.findViewById<ChipsUnify>(R.id.cu_label)

            fun bind(label: Pair<String, Boolean>) {
                chip.chipType = if (label.second) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
                chip.chipText = label.first
                chip.setOnClickListener {
                    chip.chipType = ChipsUnify.TYPE_SELECTED
                }
            }
        }

        companion object {
            fun create(itemView: View) = SlotTab(itemView)
        }

    }
}
