package com.tokopedia.play.widget.ui.widget.jumbo.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.widget.jumbo.PlayWidgetCardJumboView

/**
 * Created by meyta.taliti on 29/01/22.
 */
class PlayWidgetJumboViewHolder {

    class Channel private constructor(
        itemView: View,
        private val listener: Listener,
    ) : RecyclerView.ViewHolder(itemView) {

        private val channelView = itemView as PlayWidgetCardJumboView

        init {
            channelView.setListener(object : PlayWidgetCardJumboView.Listener {

                override fun onChannelClicked(view: View, item: PlayWidgetChannelUiModel) {
                    listener.onChannelClicked(view, item, adapterPosition)
                }

                override fun onToggleReminderChannelClicked(
                    item: PlayWidgetChannelUiModel,
                    reminderType: PlayWidgetReminderType
                ) {
                    listener.onToggleReminderChannelClicked(item, reminderType, adapterPosition)
                }

                override fun onRemindMeImpressed(view: View, item: PlayWidgetChannelUiModel) {
                    listener.onRemindIconImpressed(view, item, adapterPosition)
                }
            })
        }

        fun bind(data: PlayWidgetChannelUiModel) {
            itemView.addOnImpressionListener(data.impressHolder) {
                listener.onChannelImpressed(itemView, data, adapterPosition)
            }
            channelView.setModel(data)
        }

        companion object {
            fun create(
                itemView: View,
                listener: Listener,
            ) = Channel(
                itemView = itemView,
                listener = listener,
            )
        }

        interface Listener {

            fun onChannelImpressed(
                view: View,
                item: PlayWidgetChannelUiModel,
                position: Int
            )

            fun onChannelClicked(
                view: View,
                item: PlayWidgetChannelUiModel,
                position: Int
            )

            fun onToggleReminderChannelClicked(
                item: PlayWidgetChannelUiModel,
                reminderType: PlayWidgetReminderType,
                position: Int
            )

            fun onRemindIconImpressed(
                view: View,
                item: PlayWidgetChannelUiModel,
                position: Int
            )
        }
    }
}