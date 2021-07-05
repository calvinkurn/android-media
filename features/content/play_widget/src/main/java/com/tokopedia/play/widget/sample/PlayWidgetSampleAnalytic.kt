package com.tokopedia.play.widget.sample

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.model.PlayWidgetSmallChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 19/10/20
 */
class PlayWidgetSampleAnalytic(
        private val context: Context
) : PlayWidgetAnalyticListener {

    private var widgetPosition = RecyclerView.NO_POSITION

    override fun onClickViewAll(view: PlayWidgetSmallView) {
        Toast.makeText(context, object{}.javaClass.enclosingMethod!!.name, Toast.LENGTH_SHORT).show()
    }

    override fun onClickChannelCard(view: PlayWidgetSmallView, item: PlayWidgetSmallChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean) {
        Toast.makeText(context, "${object{}.javaClass.enclosingMethod!!.name}, position: $channelPositionInList, widgetPos: $widgetPosition", Toast.LENGTH_SHORT).show()
    }

    override fun onClickBannerCard(view: PlayWidgetSmallView) {
        Toast.makeText(context, object{}.javaClass.enclosingMethod!!.name, Toast.LENGTH_SHORT).show()
    }

    override fun onImpressChannelCard(view: PlayWidgetSmallView, item: PlayWidgetSmallChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean) {
        Toast.makeText(context, "${object{}.javaClass.enclosingMethod!!.name}, position: $channelPositionInList, widgetPos: $widgetPosition", Toast.LENGTH_SHORT).show()
    }

    override fun onImpressPlayWidget(view: PlayWidgetView, item: PlayWidgetUiModel, widgetPositionInList: Int) {
        widgetPosition = widgetPositionInList
        Toast.makeText(context, "${object{}.javaClass.enclosingMethod!!.name}, widgetPos: $widgetPosition", Toast.LENGTH_SHORT).show()
    }
}