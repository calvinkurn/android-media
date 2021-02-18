package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.R
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by jegul on 18/02/21
 */
class TagAddedViewHolder(
        itemView: View,
        private val listener: Listener
) : BaseViewHolder(itemView) {

    private val chipTag: ChipsUnify = itemView.findViewById(R.id.chip_tag)

    fun bind(item: String) {
        chipTag.chipText = item
        chipTag.setOnRemoveListener {
            listener.onTagRemoved(item)
        }
    }

    companion object {

        val LAYOUT = R.layout.item_tag_added
    }

    interface Listener {

        fun onTagRemoved(tag: String)
    }
}