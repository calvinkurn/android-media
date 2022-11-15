package com.tokopedia.play.ui.engagement.viewholder

import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.R as playR
import com.tokopedia.play.ui.engagement.model.EngagementUiModel
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.view.game.*

/**
 * @author by astidhiyaa on 24/08/22
 */
class EngagementWidgetViewHolder(
    private val binding: GameSmallWidgetView,
    private val listener: Listener
) :
    BaseViewHolder(binding) {

    private val impressHolder by lazy(LazyThreadSafetyMode.NONE) { ImpressHolder() }

    fun bindPromo(item: EngagementUiModel.Promo){
        binding.setupPromo(title = item.info.title, description = getPromoDescription(item), id = item.info.id)
        binding.setOnClickListener {
            listener.onWidgetClicked(engagement = item)
        }
        binding.addOnImpressionListener(impressHolder){
            listener.onWidgetImpressed(engagement = item)
        }
    }

    private fun getPromoDescription(item: EngagementUiModel.Promo): String {
        return when {
            item.size == 0 -> {
                getString(playR.string.play_voucher_widget_single)
            }
            else -> {
                getString(playR.string.play_voucher_widget_default, item.size)
            }
        }
    }

    fun bindGame(item: EngagementUiModel.Game) {
        when(item.game){
            is GameUiModel.Giveaway -> setupGiveaway(item.game, item)
            is GameUiModel.Quiz-> setupQuiz(item.game, item)
        }
        binding.setOnClickListener {
            listener.onWidgetClicked(engagement = item)
        }
        binding.addOnImpressionListener(impressHolder){
            listener.onWidgetImpressed(engagement = item)
        }
    }

    private fun setupGiveaway(giveaway: GameUiModel.Giveaway, item: EngagementUiModel.Game){
        when(val current = giveaway.status){
            is GameUiModel.Giveaway.Status.Upcoming -> binding.setupUpcomingGiveaway(
                title = giveaway.title,
                targetTime = current.startTime,
                onDurationEnd = {
                    listener.onWidgetGameEnded(item)
                },
                onTick = {
                    listener.onWidgetTimerTick(item,it)
                },
                id = item.game.id,
            )
            is GameUiModel.Giveaway.Status.Ongoing -> binding.setupOngoingGiveaway(
                title = giveaway.title,
                targetTime = current.endTime,
                onDurationEnd = {
                    listener.onWidgetGameEnded(item)
                },
                onTick = {
                    listener.onWidgetTimerTick(item,it)
                },
                id = item.game.id,
            )
        }
    }

    private fun setupQuiz(quiz: GameUiModel.Quiz, item: EngagementUiModel.Game) {
        when(val current = quiz.status){
            is GameUiModel.Quiz.Status.Ongoing -> binding.setupQuiz(
                question = quiz.title,
                targetTime = current.endTime,
                onDurationEnd = {
                    listener.onWidgetGameEnded(item)
                },
                onTick = {
                    listener.onWidgetTimerTick(item,it)
                },
                id = item.game.id,
            )
        }
    }

    companion object {
        private const val VOUCHER_THRESHOLD = 20
    }

    interface Listener {
        fun onWidgetGameEnded(engagement: EngagementUiModel.Game)
        fun onWidgetClicked(engagement: EngagementUiModel)
        fun onWidgetTimerTick(engagement: EngagementUiModel.Game, timeInMillis: Long)
        fun onWidgetImpressed(engagement: EngagementUiModel)
    }
}

