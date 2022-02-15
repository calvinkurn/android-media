package com.tokopedia.videoTabComponent.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.sample.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.PlayWidgetJumboView
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel


/**
 * Created by meyta.taliti on 31/01/22.
 */
class PlayFeedWidgetViewHolder private constructor() {

    internal class Jumbo private constructor(
        itemView: View,
        private val coordinator: PlayWidgetCoordinator
    ) : RecyclerView.ViewHolder(itemView) {

        private val view: PlayWidgetJumboView = itemView as PlayWidgetJumboView

        init {
            coordinator.controlWidget(view)
        }

        fun bind(item: PlayWidgetUiModel) {
            coordinator.connect(view, item)
        }

        companion object {
            fun create(
                itemView: View,
                coordinator: PlayWidgetCoordinator
            ) = Jumbo(itemView, coordinator)
        }
    }

    internal class Large private constructor(
        itemView: View,
        private val coordinator: PlayWidgetCoordinator
    ) : RecyclerView.ViewHolder(itemView) {

        private val view: PlayWidgetLargeView = itemView as PlayWidgetLargeView

        init {
            coordinator.controlWidget(view)
        }

        fun bind(item: PlayWidgetUiModel) {
            coordinator.connect(view, item)
        }

        companion object {
            fun create(
                itemView: View,
                coordinator: PlayWidgetCoordinator
            ) = Large(itemView, coordinator)
        }
    }

    internal class Medium private constructor(
        itemView: View,
        private val coordinator: PlayWidgetCoordinator
    ) : RecyclerView.ViewHolder(itemView) {

        private val view: PlayWidgetMediumView = itemView as PlayWidgetMediumView

        init {
            coordinator.controlWidget(view)
        }

        fun bind(item: PlayWidgetUiModel) {
            coordinator.connect(view, item)
        }

        companion object {
            fun create(
                itemView: View,
                coordinator: PlayWidgetCoordinator
            ) = Medium(itemView, coordinator)
        }
    }
}