package com.tokopedia.play_common.ui.leaderboard.viewholder

import android.view.View
import android.widget.FrameLayout
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play_common.R
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by mzennis on 30/06/21.
 */
class PlayInteractiveWinnerViewHolder(itemView: View, private val listener: Listener) : BaseViewHolder(itemView) {

    private val tvNumber = itemView.findViewById<Typography>(R.id.tv_winner_number)
    private val tvName = itemView.findViewById<Typography>(R.id.tv_winner_name)
    private val tvWinnerChat = itemView.findViewById<Typography>(R.id.tv_winner_chat)
    private val ivCrown = itemView.findViewById<ImageUnify>(R.id.img_crown)
    private val ivWinner = itemView.findViewById<ImageUnify>(R.id.img_winner)
    private val borderIvWinner = itemView.findViewById<FrameLayout>(R.id.fl_border_img_winner)

    fun bind(winner: PlayWinnerUiModel) {
        tvNumber.text = winner.rank.toString()
        tvName.text = winner.name
        ivWinner.setImageUrl(winner.imageUrl)

        handleFirstWinner(winner)

        if (winner.allowChat()) {
            tvWinnerChat.show()
            tvWinnerChat.setOnClickListener { listener.onChatButtonClicked(winner, adapterPosition) }
        } else {
            tvWinnerChat.hide()
            tvWinnerChat.setOnClickListener(null)
        }
    }

    private fun handleFirstWinner(winner: PlayWinnerUiModel) {
        if (winner.rank == FIRST_WINNER) {
            ivCrown.show()
            borderIvWinner.show()
        } else {
            ivCrown.hide()
            borderIvWinner.hide()
        }
    }

    interface Listener {
        fun onChatButtonClicked(item: PlayWinnerUiModel, position: Int)
    }

    companion object {

        val LAYOUT = R.layout.item_play_interactive_leaderboard_winner

        private const val FIRST_WINNER = 1
    }
}