package com.tokopedia.play.broadcaster.ui.viewholder.game

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.databinding.ItemPlayBroInteractiveParticipantBinding
import com.tokopedia.play.broadcaster.ui.model.game.GameParticipantUiModel
import com.tokopedia.play.broadcaster.R

class GameParticipantViewHolder(itemView: View) : BaseViewHolder(itemView) {
    private val binding = ItemPlayBroInteractiveParticipantBinding.bind(itemView)

    fun bind(participant: GameParticipantUiModel) = with(binding) {
        tvParticipantName.text = participant.name
        imgParticipant.setImageUrl(participant.imageUrl)
        imgCrown.showWithCondition(participant.isWinner)
        flBorderImgWinner.showWithCondition(participant.isWinner)
    }

    companion object {
        val LAYOUT = R.layout.item_play_bro_interactive_participant
    }
}