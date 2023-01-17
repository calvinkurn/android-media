package com.tokopedia.play_common.ui.leaderboard.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play_common.model.ui.LeaderboardGameUiModel
import com.tokopedia.play_common.ui.leaderboard.delegate.PlayGameAdapterDelegate
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayGameViewHolder
import com.tokopedia.play_common.view.quiz.QuizChoiceViewHolder

/**
 * @author by astidhiyaa on 16/08/22
 */
class PlayGameAdapter internal constructor(
    quizListener: QuizChoiceViewHolder.Listener,
    winnerListener: PlayGameViewHolder.Winner.Listener,
    headerListener: PlayGameViewHolder.Header.Listener,
) : BaseDiffUtilAdapter<LeaderboardGameUiModel>() {

    init {
        delegatesManager.addDelegate(PlayGameAdapterDelegate.Header(headerListener))
        delegatesManager.addDelegate(PlayGameAdapterDelegate.Quiz(quizListener))
        delegatesManager.addDelegate(PlayGameAdapterDelegate.Winner(winnerListener))
        delegatesManager.addDelegate(PlayGameAdapterDelegate.Footer())
    }

    override fun areItemsTheSame(
        oldItem: LeaderboardGameUiModel,
        newItem: LeaderboardGameUiModel
    ): Boolean {
        return if (oldItem is LeaderboardGameUiModel.Header && newItem is LeaderboardGameUiModel.Header) {
            oldItem.id == newItem.id
        } else if (oldItem is LeaderboardGameUiModel.Footer && newItem is LeaderboardGameUiModel.Footer) {
            oldItem.id == newItem.id
        } else if (oldItem is LeaderboardGameUiModel.Winner && newItem is LeaderboardGameUiModel.Winner) {
            oldItem.id == newItem.id
        } else if (oldItem is LeaderboardGameUiModel.QuizOption && newItem is LeaderboardGameUiModel.QuizOption) {
            oldItem.option.id == newItem.option.id
        }
        else oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: LeaderboardGameUiModel,
        newItem: LeaderboardGameUiModel
    ): Boolean {
        return oldItem == newItem
    }
}
