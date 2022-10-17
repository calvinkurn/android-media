package com.tokopedia.play.widget.sample.adapter.feed.viewholder

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.sample.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.PlayWidgetJumboView
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.R


/**
 * Created by meyta.taliti on 31/01/22.
 */
class PlayWidgetViewHolder private constructor() {

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
            view.setCustomHeader(
                LayoutInflater.from(itemView.context)
                    .inflate(R.layout.view_play_widget_custom_header, view, false)
            )
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

    internal class Small private constructor(
        itemView: View,
        private val coordinator: PlayWidgetCoordinator
    ) : RecyclerView.ViewHolder(itemView) {

        private val view: PlayWidgetSmallView = itemView as PlayWidgetSmallView

        init {
            view.setCustomHeader(
                LayoutInflater.from(itemView.context)
                    .inflate(R.layout.view_play_widget_custom_header, view, false)
            )
            coordinator.controlWidget(view)
        }

        fun bind(item: PlayWidgetUiModel) {
            coordinator.connect(view, item)
        }

        companion object {
            fun create(
                itemView: View,
                coordinator: PlayWidgetCoordinator
            ) = Small(itemView, coordinator)
        }
    }
}