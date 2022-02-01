package com.tokopedia.play.widget.sample.adapter.feed.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.sample.adapter.feed.viewholder.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.model.PlayFeedUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetJumboUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetLargeUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumUiModel

/**
 * Created by meyta.taliti on 29/01/22.
 */
class PlayWidgetViewAdapterDelegate private constructor() {

    internal class Jumbo :
        TypedAdapterDelegate<PlayWidgetJumboUiModel, PlayFeedUiModel, PlayWidgetViewHolder.Jumbo>(
            R.layout.item_play_widget_jumbo
        ) {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayWidgetViewHolder.Jumbo {
            return PlayWidgetViewHolder.Jumbo.create(basicView)
        }

        override fun onBindViewHolder(
            item: PlayWidgetJumboUiModel,
            holder: PlayWidgetViewHolder.Jumbo
        ) {
            holder.bind(item.model)
        }
    }

    internal class Large :
        TypedAdapterDelegate<PlayWidgetLargeUiModel, PlayFeedUiModel, PlayWidgetViewHolder.Large>(
            R.layout.item_play_widget_large
        ) {
        override fun onBindViewHolder(
            item: PlayWidgetLargeUiModel,
            holder: PlayWidgetViewHolder.Large
        ) {
            holder.bind(item.model)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayWidgetViewHolder.Large {
            return PlayWidgetViewHolder.Large.create(basicView)
        }
    }

    internal class Medium :
        TypedAdapterDelegate<PlayWidgetMediumUiModel, PlayFeedUiModel, PlayWidgetViewHolder.Medium>(
            R.layout.item_play_widget_medium
        ) {
        override fun onBindViewHolder(
            item: PlayWidgetMediumUiModel,
            holder: PlayWidgetViewHolder.Medium
        ) {
            holder.bind(item.model)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayWidgetViewHolder.Medium {
            return PlayWidgetViewHolder.Medium.create(basicView)
        }
    }
}