package com.tokopedia.play.ui.explorewidget.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.databinding.ViewPlayGridBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.widget.large.PlayWidgetCardLargeChannelView
import com.tokopedia.play_common.util.addImpressionListener

/**
 * @author by astidhiyaa on 02/12/22
 */
class PlayExploreWidgetViewHolder {
    internal class Widget(
        private val listener: Listener,
        binding: ViewPlayGridBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val card = binding.root

        init {
            card.setListener(object : PlayWidgetCardLargeChannelView.Listener {
                override fun onChannelClicked(view: View, item: PlayWidgetChannelUiModel) {
                    listener.onClickChannelCard(item, absoluteAdapterPosition)
                }

                override fun onToggleReminderChannelClicked(
                    item: PlayWidgetChannelUiModel,
                    reminderType: PlayWidgetReminderType
                ) {
                    listener.onToggleReminderClicked(
                        item.channelId,
                        reminderType,
                        absoluteAdapterPosition
                    )
                }

                override fun onMenuActionButtonClicked(
                    view: View,
                    item: PlayWidgetChannelUiModel
                ) {
                }
            })
        }

        fun bind(item: PlayWidgetChannelUiModel) {
            card.addImpressionListener(item.impressHolder) {
                listener.onImpressChannelCard(item, absoluteAdapterPosition)
            }
            card.setModel(item)
        }

        interface Listener {
            fun onToggleReminderClicked(
                channelId: String,
                reminderType: PlayWidgetReminderType,
                position: Int
            )

            fun onClickChannelCard(
                item: PlayWidgetChannelUiModel,
                channelPositionInList: Int
            )

            fun onImpressChannelCard(
                item: PlayWidgetChannelUiModel,
                channelPositionInList: Int
            )
        }

        companion object {
            fun create(
                binding: ViewPlayGridBinding,
                listener: Listener,
            ) = Widget(binding = binding, listener = listener)
        }
    }
}
