package com.tokopedia.play.widget.sample.adapter.feed.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.ui.PlayWidgetJumboView
import com.tokopedia.play.widget.ui.PlayWidgetLargeView
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by meyta.taliti on 31/01/22.
 */
class PlayWidgetViewHolder private constructor() {

    internal class Jumbo private constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val view: PlayWidgetJumboView = itemView as PlayWidgetJumboView

        fun bind(item: PlayWidgetUiModel) {
            view.setData(item)
        }

        companion object {
            fun create(itemView: View) = Jumbo(itemView)
        }
    }

    internal class Large private constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val view: PlayWidgetLargeView = itemView as PlayWidgetLargeView

        fun bind(item: PlayWidgetUiModel) {
            view.setData(item)
        }

        companion object {
            fun create(itemView: View) = Large(itemView)
        }
    }

    internal class Medium private constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val view: PlayWidgetMediumView = itemView as PlayWidgetMediumView

        fun bind(item: PlayWidgetUiModel) {
            view.setData(item)
        }

        companion object {
            fun create(itemView: View) = Medium(itemView)
        }
    }
}