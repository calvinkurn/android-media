package com.tokopedia.videoTabComponent.domain.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.R
import com.tokopedia.videoTabComponent.view.viewholder.PlayFeedWidgetViewHolder
import com.tokopedia.videoTabComponent.domain.model.data.PlayFeedUiModel
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetJumboUiModel
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetLargeUiModel
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetMediumUiModel
import com.tokopedia.videoTabComponent.view.coordinator.PlayWidgetCoordinatorVideoTab

/**
 * Created by meyta.taliti on 29/01/22.
 */
class PlayWidgetViewAdapterDelegate private constructor() {

    internal class Jumbo(
        private val coordinator: PlayWidgetCoordinatorVideoTab
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

     class Large(
        private val coordinator: PlayWidgetCoordinatorVideoTab
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
        private val coordinator: PlayWidgetCoordinatorVideoTab
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