package com.tokopedia.tokopedianow.home.presentation.viewholder.quest.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.tokopedianow.home.presentation.uimodel.quest.HomeQuestCardItemUiModel

class HomeQuestCardDiffCallback : DiffUtil.ItemCallback<HomeQuestCardItemUiModel>() {
    override fun areItemsTheSame(
        oldItem: HomeQuestCardItemUiModel,
        newItem: HomeQuestCardItemUiModel
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: HomeQuestCardItemUiModel,
        newItem: HomeQuestCardItemUiModel
    ): Boolean = oldItem.equals(newItem)
}
