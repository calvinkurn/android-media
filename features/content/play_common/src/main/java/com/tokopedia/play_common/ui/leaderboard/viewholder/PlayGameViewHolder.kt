package com.tokopedia.play_common.ui.leaderboard.viewholder

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
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
import com.tokopedia.play_common.view.updatePadding
import java.util.*

/**
 * @author by astidhiyaa on 16/08/22
 */
class PlayGameViewHolder {
    internal class Header(private val binding: ItemPlayLeaderboardHeaderBinding, private val listener: Listener) :
        BaseViewHolder(binding.root) {

        fun bind(item: LeaderboardGameUiModel.Header) {
            binding.root.addOnImpressionListener(item.impressHolder){
                listener.onLeaderBoarImpressed(item)
            }

            binding.tvLeaderboardTitle.text = item.title
            binding.ivLeaderboard.setImage(newIconId = if (item.leaderBoardType == LeadeboardType.Giveaway) IconUnify.GIFT else IconUnify.QUIZ)
            binding.tvReward.showWithCondition(item.reward.isNotEmpty())
            binding.ivReward.showWithCondition(item.reward.isNotEmpty())
            binding.tvReward.text = getString(R.string.play_leaderboard_reward_name, item.reward)
            binding.tvReward.setTextGradient(
                intArrayOf(
                    MethodChecker.getColor(
                        itemView.context,
                        R.color.play_dms_quiz_header_gradient_start
                    ),
                    MethodChecker.getColor(
                        itemView.context,
                        R.color.play_dms_quiz_header_gradient_end
                    )
                )
            )
            if (item.endsIn == null) hideTimer() else showTimer(item.endsIn)
        }

        private fun showTimer(duration: Calendar) {
            binding.tvEndsIn.show()
            binding.timerEndsIn.show()
            setTargetTime(duration)
        }

        private fun hideTimer() {
            binding.tvEndsIn.hide()
            binding.timerEndsIn.hide()
            binding.timerEndsIn.pause()
        }

        private fun setTargetTime(targetTime: Calendar, onFinished: () -> Unit = {}) {
            binding.tvEndsIn.show()
            binding.timerEndsIn.show()
            binding.timerEndsIn.apply {
                pause()
                targetDate = targetTime
                onFinish = onFinished
                resume()
            }
        }

        interface Listener {
            fun onLeaderBoarImpressed(item: LeaderboardGameUiModel.Header)
        }
    }

    internal class Quiz(binding: ItemQuizOptionBinding, listener: Listener) :
        QuizChoiceViewHolder(binding, listener) {
            init {
                binding.root.updatePadding(left = 16, right = 16, top = 0, bottom = 0)
            }
        }

    internal class Winner(
        private val binding: ItemPlayInteractiveLeaderboardWinnerBinding,
        private val listener: Listener
    ) : BaseViewHolder(binding.root) {

        init {
            binding.imgCrown.setImageResource(R.drawable.ic_play_interactive_crown_yellow)
        }
        fun bind(winner: LeaderboardGameUiModel.Winner) {
            with(binding) {
                tvWinnerNumber.text = winner.rank.toString()
                tvWinnerName.text = winner.name
                imgWinner.setImageUrl(winner.imageUrl)

                binding.imgCrown.showWithCondition(winner.rank == FIRST_WINNER)
                binding.flBorderImgWinner.showWithCondition(winner.rank == FIRST_WINNER)

                if (winner.allowChat()) {
                    tvWinnerChat.show()
                    tvWinnerChat.setOnClickListener {
                        listener.onChatButtonClicked(
                            winner,
                            adapterPosition
                        )
                    }
                } else {
                    tvWinnerChat.hide()
                    tvWinnerChat.setOnClickListener(null)
                }
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
            val text = if (item.totalParticipant > 0) item.otherParticipantText
            else item.emptyLeaderBoardCopyText.ifEmpty { getString(R.string.play_interactive_empty) }

            binding.tvLeaderboardFooter.showWithCondition(text.isNotBlank())
            binding.tvLeaderboardFooter.text = text
        }
    }
}
