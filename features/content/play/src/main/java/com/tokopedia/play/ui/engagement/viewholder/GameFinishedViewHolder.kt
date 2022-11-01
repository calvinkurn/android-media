package com.tokopedia.play.ui.engagement.viewholder

import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.ui.engagement.model.EngagementUiModel
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.view.game.*

/**
 * @author by astidhiyaa on 01/09/22
 */
class GameFinishedViewHolder(
    private val binding: InteractiveFinishView,
) : BaseViewHolder(binding) {

    fun bindGame(item: EngagementUiModel.Game) {
        when (item.game) {
            is GameUiModel.Giveaway -> binding.setupGiveaway()
            is GameUiModel.Quiz -> binding.setupQuiz()
        }
    }
}
