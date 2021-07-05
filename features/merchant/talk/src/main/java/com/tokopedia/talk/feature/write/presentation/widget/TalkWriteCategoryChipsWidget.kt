package com.tokopedia.talk.feature.write.presentation.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.talk.feature.write.presentation.uimodel.TalkWriteCategory
import com.tokopedia.talk.R
import com.tokopedia.unifycomponents.ChipsUnify

class TalkWriteCategoryChipsWidget(private val listener: ChipClickListener) : RecyclerView.Adapter<TalkWriteCategoryChipsWidget.ItemViewHolder>() {

    private var categories: List<TalkWriteCategory> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_talk_write_chips, parent, false)
        return ItemViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    fun setData(talkWriteCategoryChips: List<TalkWriteCategory>) {
        this.categories = talkWriteCategoryChips
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View,
                               private val chipClickListener: ChipClickListener) : RecyclerView.ViewHolder(itemView) {
        private var chips: ChipsUnify =  itemView.findViewById(R.id.writeChipsItem)

        fun bind(category: TalkWriteCategory) {
            chips.centerText = true
            chips.chipText = category.categoryName
            chips.chipSize = ChipsUnify.SIZE_MEDIUM
            chips.chipType = if(category.isSelected) {
                ChipsUnify.TYPE_SELECTED
            } else {
                ChipsUnify.TYPE_NORMAL
            }
            chips.setOnClickListener {
                chipClickListener.onChipClicked(category)
            }
        }
    }

    interface ChipClickListener {
        fun onChipClicked(category: TalkWriteCategory)
    }
}