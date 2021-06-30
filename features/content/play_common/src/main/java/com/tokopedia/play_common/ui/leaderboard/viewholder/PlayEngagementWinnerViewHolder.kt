package com.tokopedia.play_common.ui.leaderboard.viewholder

import android.graphics.Color
import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play_common.R
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by mzennis on 30/06/21.
 */
class PlayEngagementWinnerViewHolder(itemView: View, private val listener: Listener) : BaseViewHolder(itemView) {

    private val container = itemView.findViewById<ContainerUnify>(R.id.container_winner)
    private val tvNumber = itemView.findViewById<Typography>(R.id.tv_winner_number)
    private val tvName = itemView.findViewById<Typography>(R.id.tv_winner_name)
    private val ivWinner = itemView.findViewById<ImageUnify>(R.id.img_winner)
    private val lblWinner = itemView.findViewById<Label>(R.id.lbl_winner)
    private val ivCrown = itemView.findViewById<ImageUnify>(R.id.img_crown)
    private val iconChat = itemView.findViewById<IconUnify>(R.id.icon_winner_chat)

    fun bind(winner: PlayWinnerUiModel) {
        tvNumber.text = winner.rank.toString()
        tvName.text = winner.name
        ivWinner.setImageUrl(winner.imageUrl)

        handleFirstWinner(winner)

        if (winner.allowChat) {
            iconChat.show()
            iconChat.setOnClickListener { listener.onChatButtonClicked(winner, adapterPosition) }
        } else {
            iconChat.hide()
            iconChat.setOnClickListener(null)
        }
    }

    private fun handleFirstWinner(winner: PlayWinnerUiModel) {
        if (winner.rank == FIRST_WINNER) {
            ivCrown.show()
            lblWinner.show()
            container.setContainerColor(ContainerUnify.YELLOW)
        } else {
            ivCrown.hide()
            container.setCustomContainerColor(Pair(Color.WHITE, Color.WHITE))
            lblWinner.hide()
        }
    }

    interface Listener {
        fun onChatButtonClicked(item: PlayWinnerUiModel, position: Int)
    }

    companion object {

        val LAYOUT = R.layout.item_play_engagement_leaderboard_winner

        private const val FIRST_WINNER = 1
    }
}