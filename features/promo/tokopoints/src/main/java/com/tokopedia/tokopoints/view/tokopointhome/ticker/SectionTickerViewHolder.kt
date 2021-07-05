package com.tokopedia.tokopoints.view.tokopointhome.ticker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback

class SectionTickerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(content: SectionContent) {

        val tickerContainer = view.findViewById<View>(R.id.cons_ticker_container)

        if (view == null || content == null || content.layoutTickerAttr == null || content.layoutTickerAttr.tickerList == null || content.layoutTickerAttr.tickerList.isEmpty()) {
            tickerContainer.visibility = View.GONE
            return
        }

        val pager: Ticker? = view?.findViewById(R.id.ticker_new)
        var link = ""
        var desc = ""
        var linkDesc = ""
        if (!content.layoutTickerAttr.tickerList.isNullOrEmpty()) {
            for (tickerItem in content.layoutTickerAttr.tickerList[0].metadata) {
                link = if (tickerItem.link[CommonConstant.TickerMapKeys.APP_LINK]?.length != 0) {
                    tickerItem.link[CommonConstant.TickerMapKeys.APP_LINK].toString()
                } else {
                    tickerItem.link[CommonConstant.TickerMapKeys.URL].toString()
                }

                if (link.isNotEmpty()) {
                    linkDesc = tickerItem.text[CommonConstant.TickerMapKeys.CONTENT].toString()
                } else {
                    desc = tickerItem.text[CommonConstant.TickerMapKeys.CONTENT].toString()
                }
            }
        }
        val descriptionText = desc + "<a href=\"${link}\">" + ". " + linkDesc + "</a>"
        pager?.setHtmlDescription(descriptionText)
        pager?.findViewById<TextView>(com.tokopedia.unifycomponents.R.id.ticker_description)?.setMargin(16, 8, 22, 8)
        pager?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                if (link.startsWith(CommonConstant.TickerMapKeys.TOKOPEDIA)) {
                    RouteManager.route(view.context, link)
                } else {
                    RouteManager.route(view.context, String.format(view.context.resources.getString(R.string.tp_webview_format), ApplinkConst.WEBVIEW, link))
                }
                AnalyticsTrackerUtil.sendEvent(view.context,
                        AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_TICKER,
                        "$desc $linkDesc")
            }

            override fun onDismiss() {
            }
        })

        tickerContainer.visibility = View.VISIBLE

    }

}