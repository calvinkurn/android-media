package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.DeepLinkChecker
import com.tokopedia.applink.DeeplinkMatcher
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.setClickableUrlHtml
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel
import kotlinx.android.synthetic.main.shc_item_table_column_html.view.*

/**
 * Created By @ilhamsuaib on 01/07/20
 */

class TableColumnHtmlViewHolder(itemView: View?,
                                private val listener: Listener) : AbstractViewHolder<TableRowsUiModel.RowColumnHtml>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_item_table_column_html
    }

    private val deeplinkMatcher by lazy {
        DeeplinkMatcher()
    }

    override fun bind(element: TableRowsUiModel.RowColumnHtml) {
        with(itemView) {
            tvTableColumnHtml?.setClickableUrlHtml(
                    element.valueStr,
                    applyCustomStyling = {
                        isUnderlineText = false
                        color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_96)
                    },
                    onUrlClicked = { url ->
                        listener.onHyperlinkClicked(url)
                        goToPage(context, Uri.parse(url))
                    })
            if (element.isLeftAlign) {
                tvTableColumnHtml.gravity = Gravity.START
            } else {
                tvTableColumnHtml.gravity = Gravity.END
            }
        }
    }

    /**
     * Mimicks RouteManager.kt#moveToNativePageFromWebView
     */
    private fun goToPage(context: Context?, uri: Uri) {
        when(deeplinkMatcher.match(uri)) {
            DeepLinkChecker.PRODUCT -> {
                with(uri.pathSegments) {
                    getOrNull(0)?.let { shopDomain ->
                        getOrNull(1)?.let { productKey ->
                            RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL_DOMAIN, shopDomain, productKey)
                        }
                    }
                }
            }
            else -> goToDefaultIntent(context, uri)
        }
    }

    /**
     * Go to Tokopedia app if url registered or open the browser
     */
    private fun goToDefaultIntent(context: Context?, uri: Uri) {
        try {
            val myIntent = Intent(Intent.ACTION_VIEW, uri)
            context?.startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    interface Listener {
        fun onHyperlinkClicked(url: String)
    }
}