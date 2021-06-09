package com.tokopedia.play.widget

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.analytic.ImpressionableModel

/**
 * Created by mzennis on 05/10/20.
 */
class PlayWidgetViewHolder(
        itemView: View,
        val coordinator: PlayWidgetCoordinator
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
            coordinator.getImpressionHelper().impress(itemView, item) {
                mListener?.onWidgetImpressed(playWidgetView, item, holderWrapper.adapterPosition)
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
                item: PlayWidgetUiModel,
                position: Int,
        )
    }
}