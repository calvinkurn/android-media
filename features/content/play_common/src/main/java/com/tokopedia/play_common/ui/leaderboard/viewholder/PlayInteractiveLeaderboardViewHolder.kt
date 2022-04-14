package com.tokopedia.play_common.ui.leaderboard.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play_common.R
import com.tokopedia.play_common.model.ui.LeadeboardType
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.ui.leaderboard.adapter.PlayInteractiveWinnerAdapter
import com.tokopedia.play_common.ui.leaderboard.itemdecoration.PlayLeaderboardWinnerItemDecoration
import com.tokopedia.play_common.view.quiz.QuizChoiceViewHolder
import com.tokopedia.play_common.view.quiz.QuizListAdapter
import com.tokopedia.play_common.view.quiz.QuizOptionItemDecoration
import com.tokopedia.play_common.view.setTextGradient
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by mzennis on 30/06/21.
 */
class PlayInteractiveLeaderboardViewHolder(itemView: View, listener: Listener) : BaseViewHolder(itemView) {

    private val tvTitle = itemView.findViewById<Typography>(R.id.tv_leaderboard_title)
    private val rvWinner = itemView.findViewById<RecyclerView>(R.id.rv_winner)
    private val tvOtherParticipant = itemView.findViewById<Typography>(R.id.tv_leaderboard_other_participant)
    private val tvEmpty = itemView.findViewById<Typography>(R.id.tv_leaderboard_empty)
    private val ivLeaderBoard = itemView.findViewById<IconUnify>(R.id.iv_leaderboard)

    /**
     * Quiz
     */
    private val ivReward = itemView.findViewById<IconUnify>(R.id.iv_reward)
    private val tvReward = itemView.findViewById<Typography>(R.id.tv_reward)
    private val rvChoices = itemView.findViewById<RecyclerView>(R.id.rv_choices)
    private val choicesAdapter = QuizListAdapter(object : QuizChoiceViewHolder.Listener{
        override fun onClicked(item: QuizChoicesUiModel.Complete) {}
    })

    private val winnerAdapter = PlayInteractiveWinnerAdapter(object : PlayInteractiveWinnerViewHolder.Listener{
        override fun onChatButtonClicked(item: PlayWinnerUiModel, position: Int) {
            listener.onChatWinnerButtonClicked(item, position)
        }
    })

    init {
        rvWinner.adapter = winnerAdapter
        rvWinner.addItemDecoration(
                PlayLeaderboardWinnerItemDecoration(itemView.context)
        )

        rvChoices.adapter = choicesAdapter
        rvChoices.addItemDecoration(QuizOptionItemDecoration(itemView.context))
    }

    fun bind(leaderboard: PlayLeaderboardUiModel) {
        tvTitle.text = leaderboard.title

        setupLeaderboardType(leaderboard)

        if (leaderboard.winners.isEmpty()) hideParticipant(leaderboard) else showParticipant(leaderboard)
        if(leaderboard.choices.isEmpty()) hideQuiz(leaderboard) else showQuiz(leaderboard)
    }

    private fun setupLeaderboardType(leaderboard: PlayLeaderboardUiModel){
        when(leaderboard.leaderBoardType){
            LeadeboardType.Quiz -> {
                ivReward.show()
                tvReward.show()

                ivLeaderBoard.setImage(newIconId = IconUnify.QUIZ)
                ivReward.setImage(newIconId = IconUnify.GIFT, newLightEnable = MethodChecker.getColor(itemView.context, R.color.play_dms_quiz_header_gradient_start), newDarkEnable = MethodChecker.getColor(itemView.context, R.color.play_dms_quiz_header_gradient_start))
                tvReward.text = "Hadiah: ${leaderboard.reward}"

                tvReward.setTextGradient(intArrayOf(MethodChecker.getColor(itemView.context, R.color.play_dms_quiz_header_gradient_start), MethodChecker.getColor(itemView.context, R.color.play_dms_quiz_header_gradient_end)))
            }
            LeadeboardType.Giveaway -> ivLeaderBoard.setImage(newIconId = IconUnify.GIFT)
            else -> {
                ivReward.hide()
                tvReward.hide()
            }
        }
    }

    private fun showParticipant(leaderboard: PlayLeaderboardUiModel) {
        tvOtherParticipant.text = leaderboard.otherParticipantText

        winnerAdapter.setItems(leaderboard.winners)
        winnerAdapter.notifyDataSetChanged()

        if (leaderboard.otherParticipant > 0) tvOtherParticipant.show() else tvOtherParticipant.hide()
        rvWinner.show()
        tvEmpty.hide()
    }

    private fun hideParticipant(leaderboard: PlayLeaderboardUiModel) {
        tvEmpty.text = leaderboard.emptyLeaderBoardCopyText
        tvOtherParticipant.hide()
        rvWinner.hide()
        tvEmpty.show()
    }

    private fun showQuiz(leaderboard: PlayLeaderboardUiModel){
        rvChoices.show()
        choicesAdapter.setItemsAndAnimateChanges(leaderboard.choices)
    }

    private fun hideQuiz(leaderboard: PlayLeaderboardUiModel){
        rvChoices.hide()
    }

    interface Listener {
        fun onChatWinnerButtonClicked(winner: PlayWinnerUiModel, position: Int)
    }

    companion object {

        val LAYOUT = R.layout.item_play_interactive_leaderboard
    }
}