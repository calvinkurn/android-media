package com.tokopedia.play_common.ui.leaderboard.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
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
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*


/**
 * Created by mzennis on 30/06/21.
 */
class PlayInteractiveLeaderboardViewHolder(itemView: View, private val listener: Listener) : BaseViewHolder(itemView) {

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
    private val tvEndsIn = itemView.findViewById<Typography>(R.id.tv_ends_in)
    private val timerEndsIn = itemView.findViewById<TimerUnifySingle>(R.id.timer_ends_in)
    private val choicesAdapter = QuizListAdapter(object : QuizChoiceViewHolder.Listener {
        override fun onClicked(item: QuizChoicesUiModel) {
            listener.onChoiceItemClicked(item)
        }
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
        if (leaderboard.choices.isEmpty()) hideQuiz() else showQuiz(leaderboard)
        if (leaderboard.reward.isBlank()) hideReward() else showReward(leaderboard)
        if (leaderboard.endsIn == 0) hideTimer() else showTimer(leaderboard.endsIn.toLong())

        tvOtherParticipant.text = leaderboard.otherParticipantText
        if (leaderboard.otherParticipantText.isNotBlank() && leaderboard.winners.isNotEmpty()) tvOtherParticipant.show() else tvOtherParticipant.hide()
        itemView.addOnImpressionListener(leaderboard.impressHolder){
            listener.onLeaderBoardImpressed(leaderboard)
        }
    }

    private fun setupLeaderboardType(leaderboard: PlayLeaderboardUiModel) {
        when (leaderboard.leaderBoardType) {
            LeadeboardType.Quiz -> {
                ivReward.showWithCondition(leaderboard.reward.isNotEmpty())
                tvReward.showWithCondition(leaderboard.reward.isNotEmpty())
                ivLeaderBoard.setImage(newIconId = IconUnify.QUIZ)
                ivLeaderBoard.showWithCondition(leaderboard.endsIn == 0 )
            }
            LeadeboardType.Giveaway -> {
                ivLeaderBoard.setImage(newIconId = IconUnify.GIFT)
                ivReward.hide()
                tvReward.hide()
            }
            else -> {
                ivReward.hide()
                tvReward.hide()
            }
        }
    }

    private fun showParticipant(leaderboard: PlayLeaderboardUiModel) {
        winnerAdapter.setItems(leaderboard.winners)
        winnerAdapter.notifyDataSetChanged()
        if (leaderboard.otherParticipant > 0 && leaderboard.leaderBoardType == LeadeboardType.Giveaway) tvOtherParticipant.show() else tvOtherParticipant.hide()
        rvWinner.show()
        tvEmpty.hide()
    }

    private fun hideParticipant(leaderboard: PlayLeaderboardUiModel) {
        tvEmpty.text = leaderboard.emptyLeaderBoardCopyText
        tvOtherParticipant.hide()
        rvWinner.hide()
        tvEmpty.showWithCondition(leaderboard.emptyLeaderBoardCopyText.isNotBlank())
    }

    private fun showQuiz(leaderboard: PlayLeaderboardUiModel){
        rvChoices.show()
        choicesAdapter.setItemsAndAnimateChanges(leaderboard.choices)
    }

    private fun hideQuiz(){
        rvChoices.hide()
    }

    private fun showReward(leaderboard: PlayLeaderboardUiModel) {
        ivReward.show()
        tvReward.show()
        tvReward.text = "Hadiah: ${leaderboard.reward}"
        tvReward.setTextGradient(
            intArrayOf(
                MethodChecker.getColor(
                    itemView.context,
                    R.color.play_dms_quiz_header_gradient_start
                ),
                MethodChecker.getColor(itemView.context, R.color.play_dms_quiz_header_gradient_end)
            )
        )
    }

    private fun hideReward() {
        ivReward.hide()
        tvReward.hide()
    }

    private fun showTimer(duration: Long) {
        tvEndsIn.show()
        timerEndsIn.show()
        setTimer(duration) {}
    }

    private fun hideTimer() {
        tvEndsIn.hide()
        timerEndsIn.hide()
    }

    private fun setTimer(duration: Long, onFinished: () -> Unit) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, duration.toInt())
        setTargetTime(calendar, onFinished)
    }

    private fun setTargetTime(targetTime: Calendar, onFinished: () -> Unit) {
        tvEndsIn.show()
        timerEndsIn.show()
        timerEndsIn.apply {
            pause()
            targetDate = targetTime
            onFinish = onFinished
            resume()
        }
    }

    interface Listener {
        fun onChatWinnerButtonClicked(winner: PlayWinnerUiModel, position: Int)
        fun onChoiceItemClicked(item: QuizChoicesUiModel){}
        fun onLeaderBoardImpressed(leaderboard: PlayLeaderboardUiModel)
    }

    companion object {

        val LAYOUT = R.layout.item_play_interactive_leaderboard
    }
}