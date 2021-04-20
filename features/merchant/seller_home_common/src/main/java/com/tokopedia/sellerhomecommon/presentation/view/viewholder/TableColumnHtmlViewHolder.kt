package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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

class TableColumnHtmlViewHolder(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<TableRowsUiModel.RowColumnHtml>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_item_table_column_html

        private const val SCHEME_EXTERNAL = "tokopedia"
        private const val SCHEME_SELLERAPP = "sellerapp"
    }

    private val deeplinkMatcher by lazy {
        DeeplinkMatcher()
    }

    override fun bind(element: TableRowsUiModel.RowColumnHtml) {
        with(itemView) {
            setOnHtmlTextClicked(element)
            if (element.isLeftAlign) {
                tvTableColumnHtml.gravity = Gravity.START
            } else {
                tvTableColumnHtml.gravity = Gravity.END
            }
        }
    }

    private fun setOnHtmlTextClicked(element: TableRowsUiModel.RowColumnHtml) {
        with(itemView) {
            tvTableColumnHtml?.setClickableUrlHtml(element.valueStr) { url ->
                listener.onHyperlinkClicked(url)
                Uri.parse(url).let { uri ->
                    if (isAppLink(uri)) {
                        RouteManager.route(context, url)
                    } else {
                        if (!checkUrlForNativePage(context, uri)) {
                            goToDefaultIntent(context, uri)
                        }
                    }
                }
            }
        }
    }

    private fun isAppLink(uri: Uri): Boolean {
        return uri.scheme == SCHEME_EXTERNAL || uri.scheme == SCHEME_SELLERAPP
    }

    /**
     * Mimicks RouteManager.kt#moveToNativePageFromWebView
     */
    private fun checkUrlForNativePage(context: Context?, uri: Uri): Boolean {
        return when (deeplinkMatcher.match(uri)) {
            DeepLinkChecker.PRODUCT -> {
                with(uri.pathSegments) {
                    getOrNull(0)?.let { shopDomain ->
                        getOrNull(1)?.let { productKey ->
                            RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL_DOMAIN, shopDomain, productKey)
                        }
                    }
                }
                false
            }
            else -> false
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