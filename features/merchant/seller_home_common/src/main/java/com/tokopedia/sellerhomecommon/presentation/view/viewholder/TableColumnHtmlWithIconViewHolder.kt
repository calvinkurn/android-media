package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextPaint
import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.DeepLinkChecker
import com.tokopedia.applink.DeeplinkMatcher
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.setClickableUrlHtml
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcItemTableColumnHtmlWithIconBinding
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel
import com.tokopedia.sellerhomecommon.utils.SpannableTouchListener
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 01/07/20
 */

class TableColumnHtmlWithIconViewHolder(
    itemView: View,
    private val listener: TableColumnHtmlViewHolder.Listener
) : AbstractViewHolder<TableRowsUiModel.RowColumnHtmlWithIcon>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_item_table_column_html_with_icon

        private const val SCHEME_EXTERNAL = "tokopedia"
        private const val SCHEME_SELLERAPP = "sellerapp"

        private const val NUNITO_TYPOGRAPHY_FONT = "NunitoSansExtraBold.ttf"
    }

    private val binding by lazy { ShcItemTableColumnHtmlWithIconBinding.bind(itemView) }

    private val deeplinkMatcher by lazy {
        DeeplinkMatcher()
    }

    override fun bind(element: TableRowsUiModel.RowColumnHtmlWithIcon) {
        with(binding) {
            setOnHtmlTextClicked(element)
            if (element.isLeftAlign) {
                tvTableColumnHtml.gravity = Gravity.START
            } else {
                tvTableColumnHtml.gravity = Gravity.END
            }
        }
    }

    private fun setOnHtmlTextClicked(element: TableRowsUiModel.RowColumnHtmlWithIcon) {
        with(binding) {
            val textColorInt = element.colorInt ?: MethodChecker.getColor(
                root.context,
                com.tokopedia.unifyprinciples.R.color.Unify_N700_96
            )
            element.colorInt = textColorInt
            icon.showWithCondition(element.icon.isNotEmpty())
            icon.loadIcon(element.icon)
            tvTableColumnHtml.run {
                setClickableUrlHtml(
                    element.valueStr,
                    applyCustomStyling = {
                        isUnderlineText = false
                        color = textColorInt
                        context?.let {
                            applyTypographyFont(it)
                        }
                    },
                    onTouchListener = { spannable ->
                        SpannableTouchListener(spannable)
                    },
                    onUrlClicked = { url, _ ->
                        listener.onHyperlinkClicked(url, String.EMPTY, element.meta)
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
                )
                setTextColor(textColorInt)
            }
        }
    }

    private fun isAppLink(uri: Uri): Boolean {
        return uri.scheme == SCHEME_EXTERNAL || uri.scheme == SCHEME_SELLERAPP
    }

    private fun TextPaint.applyTypographyFont(context: Context) {
        try {
            typeface = com.tokopedia.unifyprinciples.getTypeface(context, NUNITO_TYPOGRAPHY_FONT)
        } catch (ex: Exception) {
            Timber.e(ex)
        }
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
                            RouteManager.route(
                                context,
                                ApplinkConstInternalMarketplace.PRODUCT_DETAIL_DOMAIN,
                                shopDomain,
                                productKey
                            )
                        }
                    }
                }
                true
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
}
