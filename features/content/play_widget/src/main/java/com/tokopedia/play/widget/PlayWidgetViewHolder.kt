package com.tokopedia.play.widget

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.model.ImpressionableModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by mzennis on 05/10/20.
 */
class PlayWidgetViewHolder(
        itemView: View,
        private val coordinator: PlayWidgetCoordinator
) : RecyclerView.ViewHolder(itemView) {

    private val playWidgetView = itemView as PlayWidgetView

    private var mListener: Listener? = null

    init {
        coordinator.controlWidget(this)
    }

    fun bind(item: PlayWidgetUiModel) {
        bind(item, this)
    }

    fun bind(item: PlayWidgetUiModel, holderWrapper: RecyclerView.ViewHolder) {
        if (item is ImpressionableModel) {
            itemView.addOnImpressionListener(item.impressHolder) {
                mListener?.onWidgetImpressed(playWidgetView, holderWrapper.adapterPosition)
            }
        }

        coordinator.connect(playWidgetView, item)
    }

    fun bind(item: PlayWidgetUiModel, payloads: MutableList<Any>) {
        playWidgetView.setModel(item)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    companion object {
        val layout = R.layout.item_play_widget
    }

    interface Listener {

        fun onWidgetImpressed(
                view: PlayWidgetView,
                position: Int
        )
    }
}