package com.tokopedia.flashsale.management.view.adapter.viewholder.campaigndetail

import android.view.MotionEvent
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoTnCViewModel
import kotlinx.android.synthetic.main.item_flash_sale_info_tnc.view.*
import android.widget.LinearLayout
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.tokopedia.flashsale.management.ekstension.gone
import com.tokopedia.flashsale.management.ekstension.visible
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.launch


class CampaignInfoTnCViewHolder(view: View): AbstractViewHolder<CampaignInfoTnCViewModel>(view) {
    private var heightBodyWebView = 0f
    private val heightFullExpanded: Int
        get() = (heightBodyWebView * itemView.context.resources.displayMetrics.density).toInt()



    override fun bind(element: CampaignInfoTnCViewModel) {
        with(itemView) {
            webview_tnc.isVerticalScrollBarEnabled = false
            webview_tnc.isHorizontalScrollBarEnabled = false
            webview_tnc.settings.javaScriptEnabled = true
            webview_tnc.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            webview_tnc.setOnTouchListener { _, event ->  event.action == MotionEvent.ACTION_MOVE}
            webview_tnc.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    webview_tnc.loadUrl("javascript:$TAG.setFullHeight(document.body.getBoundingClientRect().height)")
                    super.onPageFinished(view, url)
                }
            }
            webview_tnc.loadData(processWebViewHtmlStyle(element.tnc), "text/html; charset=utf-8", "UTF-8")
            webview_tnc.addJavascriptInterface(this@CampaignInfoTnCViewHolder, TAG)

            see_full_tnc.setOnClickListener { expandView() }

            if (element.tncLastUpdated.isEmpty()) {
                label_last_updated.visibility = View.GONE
            } else {
                label_last_updated.text = element.tncLastUpdated
                label_last_updated.visibility = View.VISIBLE
            }

        }
    }

    private fun expandView() {
        CoroutineScope(Dispatchers.Main).launch {
            with(itemView){
                webview_tnc.layoutParams = LinearLayout.LayoutParams(webview_tnc.width, heightFullExpanded)
                card_tnc.invalidate()
                card_tnc.requestLayout()
                see_full_tnc.gone()
            }
        }
    }

    private fun processWebViewHtmlStyle(html_string: String): String {
        return getString(R.string.html_process_web_view_no_padding, html_string)
    }

    companion object {
        val LAYOUT = R.layout.item_flash_sale_info_tnc
        private const val TAG = "TnCHolder"
    }

    @JavascriptInterface
    fun setFullHeight(height: Float) {
        this.heightBodyWebView = height
        if (itemView.webview_tnc.height < heightFullExpanded)
            itemView.see_full_tnc.visible()
        else
            itemView.see_full_tnc.gone()
    }
}