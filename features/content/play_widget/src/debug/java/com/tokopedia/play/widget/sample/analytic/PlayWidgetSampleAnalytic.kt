package com.tokopedia.play.widget.sample.analytic

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 19/10/20
 */
class PlayWidgetSampleAnalytic(
        private val context: Context
) : PlayWidgetAnalyticListener {

    private var widgetPosition = RecyclerView.NO_POSITION

    override fun onImpressChannelCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
        Toast.makeText(context, "${object{}.javaClass.enclosingMethod!!.name}, position: $channelPositionInList, widgetPos: $widgetPosition", Toast.LENGTH_SHORT).show()
    }

    override fun onImpressPlayWidget(view: PlayWidgetView, item: PlayWidgetUiModel, widgetPositionInList: Int) {
        widgetPosition = widgetPositionInList
        Toast.makeText(context, "${object{}.javaClass.enclosingMethod!!.name}, widgetPos: $widgetPosition", Toast.LENGTH_SHORT).show()
    }

    override fun onImpressOverlayCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetBackgroundUiModel,
        channelPositionInList: Int
    ) {
        Toast.makeText(context, "${object{}.javaClass.enclosingMethod!!.name}, position: $channelPositionInList, widgetPos: $widgetPosition", Toast.LENGTH_SHORT).show()
    }

    override fun onClickOverlayCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetBackgroundUiModel,
        channelPositionInList: Int
    ) {
        Toast.makeText(context, "${object{}.javaClass.enclosingMethod!!.name}, position: $channelPositionInList, widgetPos: $widgetPosition", Toast.LENGTH_SHORT).show()
    }
}
