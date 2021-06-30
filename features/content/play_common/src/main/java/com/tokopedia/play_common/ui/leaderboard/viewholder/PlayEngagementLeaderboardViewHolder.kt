package com.tokopedia.play_common.ui.leaderboard.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play_common.R
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.play_common.ui.leaderboard.adapter.PlayEngagementWinnerAdapter
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by mzennis on 30/06/21.
 */
class PlayEngagementLeaderboardViewHolder(itemView: View, listener: Listener) : BaseViewHolder(itemView) {

    private val tvTitle = itemView.findViewById<Typography>(R.id.tv_leaderboard_title)
    private val rvWinner = itemView.findViewById<RecyclerView>(R.id.rv_winner)
    private val tvOtherParticipant = itemView.findViewById<Typography>(R.id.tv_leaderboard_other_participant)
    private val tvEmpty = itemView.findViewById<Typography>(R.id.tv_leaderboard_empty)

    private val winnerAdapter = PlayEngagementWinnerAdapter(object : PlayEngagementWinnerViewHolder.Listener{

        override fun onChatButtonClicked(item: PlayWinnerUiModel, position: Int) {
            listener.onChatWinnerButtonClicked(item, position)
        }

    })

    init {
        rvWinner.adapter = winnerAdapter
    }

    fun bind(leaderboard: PlayLeaderboardUiModel) {
        tvTitle.text = leaderboard.title

        if (leaderboard.winners.isEmpty()) hideParticipant() else showParticipant(leaderboard)
    }

    private fun showParticipant(leaderboard: PlayLeaderboardUiModel) {
        tvOtherParticipant.text = leaderboard.otherParticipantText

        winnerAdapter.setItems(leaderboard.winners)

        tvOtherParticipant.show()
        rvWinner.show()
        tvEmpty.hide()
    }

    private fun hideParticipant() {
        tvOtherParticipant.hide()
        rvWinner.hide()
        tvEmpty.show()
    }

    interface Listener {
        fun onChatWinnerButtonClicked(winner: PlayWinnerUiModel, position: Int)
    }

    companion object {

        val LAYOUT = R.layout.item_play_engagement_leaderboard
    }
}