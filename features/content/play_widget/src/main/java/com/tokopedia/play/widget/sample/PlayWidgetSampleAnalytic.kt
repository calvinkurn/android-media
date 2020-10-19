package com.tokopedia.play.widget.sample

import android.content.Context
import android.widget.Toast
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.PlayWidgetView

/**
 * Created by jegul on 19/10/20
 */
class PlayWidgetSampleAnalytic(
        private val context: Context
) : PlayWidgetAnalyticListener {

    override fun onClickViewAll(view: PlayWidgetSmallView) {
        Toast.makeText(context, object{}.javaClass.enclosingMethod!!.name, Toast.LENGTH_SHORT).show()
    }

    override fun onClickChannelCard(view: PlayWidgetSmallView, channelPositionInList: Int) {
        Toast.makeText(context, "${object{}.javaClass.enclosingMethod!!.name}, position: $channelPositionInList", Toast.LENGTH_SHORT).show()
    }

    override fun onClickBannerCard(view: PlayWidgetSmallView) {
        Toast.makeText(context, object{}.javaClass.enclosingMethod!!.name, Toast.LENGTH_SHORT).show()
    }

    override fun onImpressChannelCard(view: PlayWidgetSmallView, channelPositionInList: Int) {
        Toast.makeText(context, object{}.javaClass.enclosingMethod!!.name, Toast.LENGTH_SHORT).show()
    }

    override fun onImpressPlayWidget(view: PlayWidgetView) {
        Toast.makeText(context, object{}.javaClass.enclosingMethod!!.name, Toast.LENGTH_SHORT).show()
    }
}