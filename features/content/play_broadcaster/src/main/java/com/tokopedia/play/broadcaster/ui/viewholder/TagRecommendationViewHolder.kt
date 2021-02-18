package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.R

/**
 * Created by jegul on 18/02/21
 */
class TagRecommendationViewHolder(
        itemView: View,
        private val listener: Listener
) : BaseViewHolder(itemView) {

    private val tvTagName: TextView = itemView.findViewById(R.id.tv_tag_name)

    fun bind(item: String) {
        tvTagName.text = item
        tvTagName.setOnClickListener {
            listener.onTagClicked(item)
        }
    }

    companion object {

        val LAYOUT = R.layout.item_tag_recommendation
    }

    interface Listener {

        fun onTagClicked(tag: String)
    }
}