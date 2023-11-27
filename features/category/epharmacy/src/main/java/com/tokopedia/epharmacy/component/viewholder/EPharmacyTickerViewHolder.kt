package com.tokopedia.epharmacy.component.viewholder

import android.os.Build
import android.view.View
import android.webkit.WebView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.adapters.EPharmacyListener
import com.tokopedia.epharmacy.component.model.EPharmacyTickerDataModel
import com.tokopedia.epharmacy.utils.EPharmacyUtils
import com.tokopedia.epharmacy.utils.WEB_VIEW_MIN_VERSION_SUPPORT_CONSULTATION
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography

class EPharmacyTickerViewHolder(
    val view: View,
    val ePharmacyListener: EPharmacyListener?
) : AbstractViewHolder<EPharmacyTickerDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.epharmacy_ticker_view_item
    }

    private val webViewTicker = view.findViewById<Ticker>(R.id.webViewTicker)

    override fun bind(data: EPharmacyTickerDataModel) {
        view.findViewById<ImageUnify>(R.id.ticker_icon).loadImage(data.tickerLogo)
        view.findViewById<ImageUnify>(R.id.ticker_background_image).loadImageWithoutPlaceholder(data.tickerBackground)
        view.findViewById<Typography>(R.id.ticker_text).text = data.tickerText?.parseAsHtml()
        renderWebViewTicker(data)
    }

    private fun renderWebViewTicker(data: EPharmacyTickerDataModel) {
        if (data.tickerWebViewText.isNullOrBlank()) return
        getWebViewVersion().let { version ->
            if (errorWebViewCondition(version)) {
                showWebViewTicker(data)
            }
        }
    }

    private fun getWebViewVersion(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val info = WebView.getCurrentWebViewPackage()
                info?.versionName.orEmpty()
            } catch (e: Exception) {
                EPharmacyUtils.logException(e)
                String.EMPTY
            }
        } else {
            String.EMPTY
        }
    }

    private fun errorWebViewCondition(version: String): Boolean {
        return version.split(".").firstOrNull().toIntOrZero() < WEB_VIEW_MIN_VERSION_SUPPORT_CONSULTATION
    }

    private fun showWebViewTicker(data: EPharmacyTickerDataModel) {
        if (data.tickerWebViewText.isNullOrBlank()) return

        webViewTicker.setHtmlDescription(data.tickerWebViewText.orEmpty())
        webViewTicker.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                val url = linkUrl.toString()
                if (url.isNotBlank()) {
                    ePharmacyListener?.redirect(url)
                }
            }

            override fun onDismiss() {}
        })
        webViewTicker.show()
    }
}
