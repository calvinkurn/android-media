package com.tokopedia.play_common.ui.leaderboard.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play_common.R
import com.tokopedia.play_common.databinding.ItemPlayInteractiveLeaderboardWinnerBinding
import com.tokopedia.play_common.databinding.ItemPlayLeaderboardFooterBinding
import com.tokopedia.play_common.databinding.ItemPlayLeaderboardHeaderBinding
import com.tokopedia.play_common.databinding.ItemQuizOptionBinding
import com.tokopedia.play_common.model.ui.LeaderboardGameUiModel
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayGameViewHolder
import com.tokopedia.play_common.view.quiz.QuizChoiceViewHolder

/**
 * @author by astidhiyaa on 16/08/22
 */
class PlayGameAdapterDelegate {

    internal class Header (private val listener: PlayGameViewHolder.Header.Listener):
        TypedAdapterDelegate<LeaderboardGameUiModel.Header, LeaderboardGameUiModel, PlayGameViewHolder.Header>(
            R.layout.view_play_empty
        ) {
        override fun onBindViewHolder(
            item: LeaderboardGameUiModel.Header,
            holder: PlayGameViewHolder.Header
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayGameViewHolder.Header {
            val view = ItemPlayLeaderboardHeaderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return PlayGameViewHolder.Header(view, listener)
        }
    }

    internal class Quiz (private val listener: QuizChoiceViewHolder.Listener):
        TypedAdapterDelegate<LeaderboardGameUiModel.QuizOption, LeaderboardGameUiModel, PlayGameViewHolder.Quiz>(
            R.layout.view_play_empty
        ) {
        override fun onBindViewHolder(
            item: LeaderboardGameUiModel.QuizOption,
            holder: PlayGameViewHolder.Quiz
        ) {
            holder.bind(item = item.option)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayGameViewHolder.Quiz {
            val view = ItemQuizOptionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return PlayGameViewHolder.Quiz(view, listener)
        }
    }

    internal class Winner (private val listener: PlayGameViewHolder.Winner.Listener) :
        TypedAdapterDelegate<LeaderboardGameUiModel.Winner, LeaderboardGameUiModel, PlayGameViewHolder.Winner>(
            R.layout.view_play_empty
        ) {
        override fun onBindViewHolder(
            item: LeaderboardGameUiModel.Winner,
            holder: PlayGameViewHolder.Winner
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayGameViewHolder.Winner {
            val view = ItemPlayInteractiveLeaderboardWinnerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return PlayGameViewHolder.Winner(view, listener)
        }
    }

    internal class Footer :
        TypedAdapterDelegate<LeaderboardGameUiModel.Footer, LeaderboardGameUiModel, PlayGameViewHolder.Footer>(
            R.layout.view_play_empty
        ) {
        override fun onBindViewHolder(
            item: LeaderboardGameUiModel.Footer,
            holder: PlayGameViewHolder.Footer
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayGameViewHolder.Footer {
            val view = ItemPlayLeaderboardFooterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return PlayGameViewHolder.Footer(view)
        }
    }
}