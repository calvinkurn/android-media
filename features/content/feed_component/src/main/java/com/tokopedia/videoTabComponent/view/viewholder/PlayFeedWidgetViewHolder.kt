package com.tokopedia.videoTabComponent.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetJumboView
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.videoTabComponent.callback.PlayWidgetCardClickListener
import com.tokopedia.videoTabComponent.view.coordinator.PlayWidgetCoordinatorVideoTab

/**
 * Created by meyta.taliti on 31/01/22.
 */
class PlayFeedWidgetViewHolder private constructor() {

    internal class Jumbo private constructor(
        itemView: View,
        private val coordinator: PlayWidgetCoordinatorVideoTab,
        private val clickListener: PlayWidgetCardClickListener,
    ) : RecyclerView.ViewHolder(itemView) {

        private val view: PlayWidgetJumboView = itemView as PlayWidgetJumboView

        init {
            coordinator.setAnalyticListener(object : PlayWidgetAnalyticListener {
                override fun onClickChannelCard(
                    view: PlayWidgetJumboView,
                    item: PlayWidgetChannelUiModel,
                    config: PlayWidgetConfigUiModel,
                    channelPositionInList: Int
                ) {
                    super.onClickChannelCard(view, item, config, channelPositionInList)
                    clickListener.invoke(item.channelId, channelPositionInList)
                }
            })
            coordinator.controlWidget(view)
        }

        fun bind(item: PlayWidgetUiModel) {
            coordinator.connect(view, item)
        }

        companion object {
            fun create(
                itemView: View,
                coordinator: PlayWidgetCoordinatorVideoTab,
                clickListener: PlayWidgetCardClickListener,
            ) = Jumbo(itemView, coordinator, clickListener)
        }
    }

     class Large private constructor(
        itemView: View,
        private val coordinator: PlayWidgetCoordinatorVideoTab,
        private val clickListener: PlayWidgetCardClickListener,
    ) : RecyclerView.ViewHolder(itemView) {

        private val view: PlayWidgetLargeView = itemView as PlayWidgetLargeView

        init {
            coordinator.setAnalyticListener(object : PlayWidgetAnalyticListener {
                override fun onClickChannelCard(
                    view: PlayWidgetLargeView,
                    item: PlayWidgetChannelUiModel,
                    config: PlayWidgetConfigUiModel,
                    channelPositionInList: Int
                ) {
                    super.onClickChannelCard(view, item, config, channelPositionInList)
                    clickListener.invoke(item.channelId, channelPositionInList)
                }
            })
            coordinator.controlWidget(view)
        }

        fun bind(item: PlayWidgetUiModel) {
            coordinator.connect(view, item)
        }

        companion object {
            fun create(
                itemView: View,
                coordinator: PlayWidgetCoordinatorVideoTab,
                clickListener: PlayWidgetCardClickListener,
            ) = Large(itemView, coordinator, clickListener)
        }
    }

    internal class Medium private constructor(
        itemView: View,
        private val coordinator: PlayWidgetCoordinatorVideoTab,
        private val clickListener: PlayWidgetCardClickListener,
    ) : RecyclerView.ViewHolder(itemView) {

        private val view: PlayWidgetMediumView = itemView as PlayWidgetMediumView

        init {
            coordinator.setAnalyticListener(object : PlayWidgetAnalyticListener {
                override fun onClickChannelCard(
                    view: PlayWidgetMediumView,
                    item: PlayWidgetChannelUiModel,
                    config: PlayWidgetConfigUiModel,
                    channelPositionInList: Int
                ) {
                    super.onClickChannelCard(view, item, config, channelPositionInList)
                    clickListener.invoke(item.channelId, channelPositionInList)
                }
            })
            coordinator.controlWidget(view)
        }

        fun bind(item: PlayWidgetUiModel) {
            coordinator.connect(view, item)
        }

        companion object {
            fun create(
                itemView: View,
                coordinator: PlayWidgetCoordinatorVideoTab,
                clickListener: PlayWidgetCardClickListener,
            ) = Medium(itemView, coordinator, clickListener)
        }
    }
}