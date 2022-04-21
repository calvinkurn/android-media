package com.tokopedia.play.widget.sample.adapter.feed.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.sample.adapter.feed.viewholder.PlaySlotTabViewHolder
import com.tokopedia.play.widget.ui.model.PlayFeedUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetSlotTabUiModel

/**
 * Created by meyta.taliti on 01/02/22.
 */
class PlaySlotTabViewAdapterDelegate private constructor() {

    internal class SlotTab :
        TypedAdapterDelegate<PlayWidgetSlotTabUiModel, PlayFeedUiModel, PlaySlotTabViewHolder.SlotTab>(
            R.layout.item_play_slot_tab
        ) {
        override fun onBindViewHolder(
            item: PlayWidgetSlotTabUiModel,
            holder: PlaySlotTabViewHolder.SlotTab
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlaySlotTabViewHolder.SlotTab {
            return PlaySlotTabViewHolder.SlotTab.create(basicView)
        }
    }
}