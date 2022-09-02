package com.tokopedia.play.ui.engagement.viewholder

import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.ui.engagement.model.EngagementUiModel
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.view.game.*

/**
 * @author by astidhiyaa on 24/08/22
 */
class EngagementWidgetViewHolder(
    private val binding: GameSmallWidgetView,
    private val listener: Listener
) :
    BaseViewHolder(binding) {

    fun bindPromo(item: EngagementUiModel.Promo){
        binding.setupPromo(title = item.info.title, description = getPromoDescription(item))
    }

    private fun getPromoDescription(item: EngagementUiModel.Promo): String {
        return when {
            item.info.voucherStock <= 20 -> {
                "Sisa ${item.info.voucherStock - 1} kupon!"
            }
            item.size == 1 -> {
                "Cek kuponnya!"
            }
            else -> {
                "+${item.size} Kupon lainnya"
            }
        }
    }

    fun bindGame(item: EngagementUiModel.Game) {
        when(item.interactive){
            is InteractiveUiModel.Giveaway -> setupGiveaway(item.interactive)
            is InteractiveUiModel.Quiz-> setupQuiz(item.interactive)
        }
    }

    private fun setupGiveaway(giveaway: InteractiveUiModel.Giveaway){
        when(val current = giveaway.status){
            is InteractiveUiModel.Giveaway.Status.Upcoming -> binding.setupUpcomingGiveaway(
                title = giveaway.title,
                targetTime = current.startTime,
                onDurationEnd = {
                    listener.onGiveawayUpcomingEnd(giveaway)
                }
            )
            is InteractiveUiModel.Giveaway.Status.Ongoing -> binding.setupOngoingGiveaway(
                title = giveaway.title,
                targetTime = current.endTime,
                onDurationEnd = {
                    listener.onGiveawayEnd(giveaway)
                }
            )
        }
    }

    private fun setupQuiz(quiz: InteractiveUiModel.Quiz) {
        when(val current = quiz.status){
            is InteractiveUiModel.Quiz.Status.Ongoing -> binding.setupQuiz(
                question = quiz.title,
                targetTime = current.endTime,
                onDurationEnd = {
                    listener.onQuizEnd(quiz)
                }
            )
        }
    }

    interface Listener {
        fun onGiveawayEnd(giveaway: InteractiveUiModel.Giveaway)
        fun onGiveawayUpcomingEnd(giveaway: InteractiveUiModel.Giveaway)
        fun onQuizEnd(quiz: InteractiveUiModel.Quiz)
        fun onWidgetClicked(engagement: EngagementUiModel)
    }
}

