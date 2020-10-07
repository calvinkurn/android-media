package com.tokopedia.play.widget.ui.adapter.viewholder.medium

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.adapter.PlayWidgetCardMediumAdapter
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumOverlayUiModel


/**
 * Created by mzennis on 07/10/20.
 */
class PlayWidgetCardMediumOverlayViewHolder(
        itemView: View,
        private val cardMediumListener: PlayWidgetCardMediumAdapter.CardMediumListener
) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: PlayWidgetMediumOverlayUiModel) {
        setupListener(item)
    }

    private fun setupListener(item: PlayWidgetMediumOverlayUiModel) {
        itemView.setOnClickListener {
            cardMediumListener.onCardMediumClicked(item, adapterPosition)
        }
        itemView.addOnImpressionListener(item.impress) {
            cardMediumListener.onCardMediumVisible(item, adapterPosition)
        }
    }

    companion object {
        @LayoutRes val layoutRes = R.layout.item_play_widget_card_overlay_medium
    }
}