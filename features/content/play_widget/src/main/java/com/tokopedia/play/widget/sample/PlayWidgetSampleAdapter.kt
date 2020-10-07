package com.tokopedia.play.widget.sample

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.PlayWidgetUiModel
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.type.PlayWidgetCardSize

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetSampleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val widgetList: MutableList<PlayWidgetUiModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlayWidgetViewHolder(
                when (viewType) {
                    PlayWidgetCardSize.Small.ordinal -> PlayWidgetSmallView(parent.context).apply {
                        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    }
                    PlayWidgetCardSize.Medium.ordinal -> PlayWidgetMediumView(parent.context).apply {
                        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    }
                    else -> throw IllegalArgumentException("Not Supported")
                }
        )
    }

    override fun getItemCount(): Int {
        return widgetList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PlayWidgetViewHolder).bind(widgetList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return widgetList[position].size.ordinal
    }

    fun setWidgets(widgets: List<PlayWidgetUiModel>) {
        widgetList.clear()
        widgetList.addAll(widgets)
    }
}