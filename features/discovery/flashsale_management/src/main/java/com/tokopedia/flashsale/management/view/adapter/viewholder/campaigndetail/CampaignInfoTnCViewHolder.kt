package com.tokopedia.flashsale.management.view.adapter.viewholder.campaigndetail

import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.ekstension.gone
import com.tokopedia.flashsale.management.view.viewmodel.CampaignInfoTnCViewModel
import kotlinx.android.synthetic.main.item_flash_sale_info_tnc.view.*
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.launch


class CampaignInfoTnCViewHolder(view: View) : AbstractViewHolder<CampaignInfoTnCViewModel>(view) {

    override fun bind(element: CampaignInfoTnCViewModel) {
        with(itemView) {
            webview_tnc.isVerticalScrollBarEnabled = false
            webview_tnc.isHorizontalScrollBarEnabled = false
            webview_tnc.loadData(processWebViewHtmlStyle(element.tnc), "text/html; charset=utf-8", "UTF-8")
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
            with(itemView) {
                webview_tnc.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                card_tnc.invalidate()
                card_tnc.requestLayout()
                see_full_tnc.gone()
                (label_last_updated.layoutParams as ConstraintLayout.LayoutParams).topMargin =
                        context.resources.getDimensionPixelOffset(R.dimen.dp_12)
            }
        }
    }

    private fun processWebViewHtmlStyle(html_string: String): String {
        return getString(R.string.html_process_web_view_no_padding, html_string)
    }

    companion object {
        val LAYOUT = R.layout.item_flash_sale_info_tnc
    }

}