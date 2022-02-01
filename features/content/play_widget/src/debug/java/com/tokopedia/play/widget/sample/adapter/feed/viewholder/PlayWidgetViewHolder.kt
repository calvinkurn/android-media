package com.tokopedia.play.widget.sample.adapter.feed.viewholder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.PlayWidgetJumboView
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.model.PlayWidgetSlotTabUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.unifycomponents.ChipsUnify


/**
 * Created by meyta.taliti on 31/01/22.
 */
class PlayWidgetViewHolder private constructor() {

    internal class Jumbo private constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val view: PlayWidgetJumboView = itemView as PlayWidgetJumboView

        fun bind(item: PlayWidgetUiModel) {
            view.setData(item)
        }

        companion object {
            fun create(itemView: View) = Jumbo(itemView)
        }
    }

    internal class Large private constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val view: PlayWidgetLargeView = itemView as PlayWidgetLargeView

        fun bind(item: PlayWidgetUiModel) {
            view.setData(item)
        }

        companion object {
            fun create(itemView: View) = Large(itemView)
        }
    }

    internal class Medium private constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val view: PlayWidgetMediumView = itemView as PlayWidgetMediumView

        fun bind(item: PlayWidgetUiModel) {
            view.setData(item)
        }

        companion object {
            fun create(itemView: View) = Medium(itemView)
        }
    }

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

            private val mItems: MutableList<String> = mutableListOf()

            @SuppressLint("NotifyDataSetChanged")
            fun setItems(labels: List<String>) {
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

            fun bind(label: String) {
                chip.chipText = label
            }
        }

        companion object {
            fun create(itemView: View) = SlotTab(itemView)
        }
    }
}