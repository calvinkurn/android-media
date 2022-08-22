package com.tokopedia.play_common.ui.leaderboard.viewholder

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play_common.R
import com.tokopedia.play_common.databinding.ItemPlayInteractiveLeaderboardWinnerBinding
import com.tokopedia.play_common.databinding.ItemPlayLeaderboardFooterBinding
import com.tokopedia.play_common.databinding.ItemPlayLeaderboardHeaderBinding
import com.tokopedia.play_common.databinding.ItemQuizOptionBinding
import com.tokopedia.play_common.model.ui.LeadeboardType
import com.tokopedia.play_common.model.ui.LeaderboardGameUiModel
import com.tokopedia.play_common.view.quiz.QuizChoiceViewHolder
import com.tokopedia.play_common.view.setTextGradient
import java.util.*

/**
 * @author by astidhiyaa on 16/08/22
 */
class PlayGameViewHolder {
    internal class Header(private val binding: ItemPlayLeaderboardHeaderBinding) :
        BaseViewHolder(binding.root) {
        fun bind(item: LeaderboardGameUiModel.Header) {
            binding.tvLeaderboardTitle.text = item.title
            binding.ivLeaderboard.setImage(newIconId = if(item.leaderBoardType == LeadeboardType.Giveaway) IconUnify.GIFT else IconUnify.GAME)
            binding.tvReward.showWithCondition(item.reward.isNotEmpty())
            binding.ivReward.showWithCondition(item.reward.isNotEmpty())
            binding.tvReward.text = getString(R.string.play_leaderboard_reward_name, item.reward)
            binding.tvReward.setTextGradient(
                intArrayOf(
                    MethodChecker.getColor(
                        itemView.context,
                        R.color.play_dms_quiz_header_gradient_start
                    ),
                    MethodChecker.getColor(itemView.context, R.color.play_dms_quiz_header_gradient_end)
                )
            )
            if (item.endsIn == 0) hideTimer() else showTimer(item.endsIn.toLong())
        }

        private fun showTimer(duration: Long) {
            binding.tvEndsIn.show()
            binding.timerEndsIn.show()
            setTimer(duration) {}
        }

        private fun hideTimer() {
            binding.tvEndsIn.hide()
            binding.timerEndsIn.hide()
        }

        private fun setTimer(duration: Long, onFinished: () -> Unit) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.SECOND, duration.toInt())
            setTargetTime(calendar, onFinished)
        }

        private fun setTargetTime(targetTime: Calendar, onFinished: () -> Unit) {
            binding.tvEndsIn.show()
            binding.timerEndsIn.show()
            binding.timerEndsIn.apply {
                pause()
                targetDate = targetTime
                onFinish = onFinished
                resume()
            }
        }
    }

    internal class Quiz(binding: ItemQuizOptionBinding, listener: Listener) :
        QuizChoiceViewHolder(binding, listener) {}

    internal class Winner(
        private val binding: ItemPlayInteractiveLeaderboardWinnerBinding,
        private val listener: Listener
    ) : BaseViewHolder(binding.root) {
        fun bind(winner: LeaderboardGameUiModel.Winner) {
            with(binding){
                tvWinnerNumber.text = winner.rank.toString()
                tvWinnerName.text = winner.name
                imgWinner.setImageUrl(winner.imageUrl)

                handleFirstWinner(winner)

                if (winner.allowChat()) {
                    tvWinnerChat.show()
                    tvWinnerChat.setOnClickListener { listener.onChatButtonClicked(winner, adapterPosition) }
                } else {
                    tvWinnerChat.hide()
                    tvWinnerChat.setOnClickListener(null)
                }
            }
        }

        private fun handleFirstWinner(winner: LeaderboardGameUiModel.Winner) {
            if (winner.rank == FIRST_WINNER) {
                binding.imgCrown.show()
                binding.flBorderImgWinner.show()
            } else {
                binding.imgCrown.hide()
                binding.flBorderImgWinner.hide()
            }
        }

        interface Listener {
            fun onChatButtonClicked(item: LeaderboardGameUiModel.Winner, position: Int)
        }

        companion object {
            private const val FIRST_WINNER = 1
        }
    }

    internal class Footer(private val binding: ItemPlayLeaderboardFooterBinding) :
        BaseViewHolder(binding.root) {
        fun bind(item: LeaderboardGameUiModel.Footer) {
            //TODO() need to adjust
            binding.tvLeaderboardFooter.text = item.otherParticipantText
        }
    }
}