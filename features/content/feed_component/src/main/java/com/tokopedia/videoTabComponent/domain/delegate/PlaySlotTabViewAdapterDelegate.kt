package com.tokopedia.videoTabComponent.domain.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.R
import com.tokopedia.videoTabComponent.domain.model.data.PlayFeedUiModel
import com.tokopedia.videoTabComponent.view.viewholder.PlayFeedSlotTabViewHolder
import com.tokopedia.videoTabComponent.domain.model.data.PlaySlotTabMenuUiModel
import com.tokopedia.videoTabComponent.callback.PlaySlotTabCallback

/**
 * Created by meyta.taliti on 01/02/22.
 */
class PlaySlotTabViewAdapterDelegate private constructor() {

    internal class SlotTab(private val listener: PlaySlotTabCallback, private val position: Int) :
        TypedAdapterDelegate<PlaySlotTabMenuUiModel, PlayFeedUiModel, PlayFeedSlotTabViewHolder.SlotTab>(
            R.layout.item_play_slot_tab
        ) {
        override fun onBindViewHolder(
            item: PlaySlotTabMenuUiModel,
            holder: PlayFeedSlotTabViewHolder.SlotTab
        ) {
            holder.bind(item, position)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup, basicView: View
        ): PlayFeedSlotTabViewHolder.SlotTab {
            return PlayFeedSlotTabViewHolder.SlotTab.create(basicView, listener)
        }
    }
}