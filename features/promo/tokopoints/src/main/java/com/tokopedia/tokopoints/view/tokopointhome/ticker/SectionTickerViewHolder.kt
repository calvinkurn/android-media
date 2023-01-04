package com.tokopedia.tokopoints.view.tokopointhome.ticker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.TickerMetadata
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback

class SectionTickerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val tickerDataList= arrayListOf<TickerData>()
    var link = ""
    var desc = ""
    var linkDesc = ""
    private val INDEX_TEXT = 0
    private val INDEX_LINK = 1

    fun bind(content: SectionContent) {
        val tickerContainer = view.findViewById<View>(R.id.cons_ticker_container)
        val pager: Ticker? = view.findViewById(R.id.ticker_new)
        if (content.layoutTickerAttr.tickerList.isEmpty()) {
            tickerContainer.visibility = View.GONE
            return
        }

        if (!content.layoutTickerAttr.tickerList.isNullOrEmpty()) {
            content.layoutTickerAttr.tickerList?.let { tickerData ->
                tickerData.forEachIndexed { index, tickerContainer ->
                    val tickerItemText =  tickerContainer.metadata?.get(INDEX_TEXT)?: TickerMetadata()
                    val tickerItemLink =  tickerContainer.metadata?.getOrNull(INDEX_LINK)?: TickerMetadata()

                    link = if (tickerItemLink.link[CommonConstant.TickerMapKeys.APP_LINK]?.length != 0) {
                        tickerItemLink.link[CommonConstant.TickerMapKeys.APP_LINK].toString()
                    } else {
                        tickerItemLink.link[CommonConstant.TickerMapKeys.URL].toString()
                    }
                    if (link.isNotEmpty()) {
                        linkDesc = tickerItemLink.text[CommonConstant.TickerMapKeys.CONTENT].toString()
                    }
                    desc = tickerItemText.text[CommonConstant.TickerMapKeys.CONTENT].toString()
                    val descriptionText = desc + "<a href=\"${link}\">" + linkDesc + "</a>"
                    tickerDataList.add(index,TickerData(descriptionText,Ticker.TYPE_ANNOUNCEMENT))
                }
            }
        }
        val tickerViewPager = TickerPagerAdapter(view.context, tickerDataList)
        pager?.addPagerView(tickerViewPager,tickerDataList)
        pager?.findViewById<TextView>(com.tokopedia.unifycomponents.R.id.ticker_description)?.setMargin(16, 8, 22, 8)
        tickerViewPager.setPagerDescriptionClickEvent(object : TickerPagerCallback {
            override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
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
        })
        tickerContainer.visibility = View.VISIBLE
    }
}
