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
        binding.setOnClickListener {
            listener.onWidgetClicked(engagement = item)
        }
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
            is InteractiveUiModel.Giveaway -> setupGiveaway(item.interactive, item)
            is InteractiveUiModel.Quiz-> setupQuiz(item.interactive, item)
        }
        listener.onWidgetClicked(engagement = item)
    }

    private fun setupGiveaway(giveaway: InteractiveUiModel.Giveaway, item: EngagementUiModel.Game){
        when(val current = giveaway.status){
            is InteractiveUiModel.Giveaway.Status.Upcoming -> binding.setupUpcomingGiveaway(
                title = giveaway.title,
                targetTime = current.startTime,
                onDurationEnd = {
                    listener.onWidgetGameEnded(item)
                }
            )
            is InteractiveUiModel.Giveaway.Status.Ongoing -> binding.setupOngoingGiveaway(
                title = giveaway.title,
                targetTime = current.endTime,
                onDurationEnd = {
                    listener.onWidgetGameEnded(item)
                }
            )
        }
    }

    private fun setupQuiz(quiz: InteractiveUiModel.Quiz, item: EngagementUiModel.Game) {
        when(val current = quiz.status){
            is InteractiveUiModel.Quiz.Status.Ongoing -> binding.setupQuiz(
                question = quiz.title,
                targetTime = current.endTime,
                onDurationEnd = {
                    listener.onWidgetGameEnded(item)
                }
            )
        }
    }

    interface Listener {
        fun onWidgetGameEnded(engagement: EngagementUiModel.Game)
        fun onWidgetClicked(engagement: EngagementUiModel)
    }
}

