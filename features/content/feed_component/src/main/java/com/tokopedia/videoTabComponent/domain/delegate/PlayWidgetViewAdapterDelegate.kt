package com.tokopedia.videoTabComponent.domain.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.R
import com.tokopedia.videoTabComponent.view.viewholder.PlayFeedWidgetViewHolder
import com.tokopedia.play.widget.sample.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.model.PlayFeedUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetJumboUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetLargeUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumUiModel

/**
 * Created by meyta.taliti on 29/01/22.
 */
class PlayWidgetViewAdapterDelegate private constructor() {

    internal class Jumbo(
        private val coordinator: PlayWidgetCoordinator
    ) :
        TypedAdapterDelegate<PlayWidgetJumboUiModel, PlayFeedUiModel, PlayFeedWidgetViewHolder.Jumbo>(
            R.layout.item_play_widget_jumbo
        ) {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayFeedWidgetViewHolder.Jumbo {
            return PlayFeedWidgetViewHolder.Jumbo.create(basicView, coordinator)
        }

        override fun onBindViewHolder(
            item: PlayWidgetJumboUiModel,
            holder: PlayFeedWidgetViewHolder.Jumbo
        ) {
            holder.bind(item.model)
        }
    }

    internal class Large(
        private val coordinator: PlayWidgetCoordinator
    ) :
        TypedAdapterDelegate<PlayWidgetLargeUiModel, PlayFeedUiModel, PlayFeedWidgetViewHolder.Large>(
            R.layout.item_play_widget_large
        ) {
        override fun onBindViewHolder(
            item: PlayWidgetLargeUiModel,
            holder: PlayFeedWidgetViewHolder.Large
        ) {
            holder.bind(item.model)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayFeedWidgetViewHolder.Large {
            return PlayFeedWidgetViewHolder.Large.create(basicView, coordinator)
        }
    }

    internal class Medium(
        private val coordinator: PlayWidgetCoordinator
    ) :
        TypedAdapterDelegate<PlayWidgetMediumUiModel, PlayFeedUiModel, PlayFeedWidgetViewHolder.Medium>(
            R.layout.item_play_widget_medium
        ) {
        override fun onBindViewHolder(
            item: PlayWidgetMediumUiModel,
            holder: PlayFeedWidgetViewHolder.Medium
        ) {
            holder.bind(item.model)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayFeedWidgetViewHolder.Medium {
            return PlayFeedWidgetViewHolder.Medium.create(basicView, coordinator)
        }
    }
}