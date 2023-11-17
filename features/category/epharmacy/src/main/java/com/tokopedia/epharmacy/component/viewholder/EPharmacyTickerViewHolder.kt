package com.tokopedia.epharmacy.component.viewholder

import android.content.pm.PackageInfo
import android.os.Build
import android.view.View
import android.webkit.WebView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.component.model.EPharmacyTickerDataModel
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.epharmacy.R as epharmacyR


class EPharmacyTickerViewHolder(
    val view: View
) : AbstractViewHolder<EPharmacyTickerDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.epharmacy_ticker_view_item
    }

    private val webViewTicker = view.findViewById<Ticker>(R.id.webViewTicker)

    override fun bind(data: EPharmacyTickerDataModel) {
        view.findViewById<ImageUnify>(R.id.ticker_icon).loadImage(data.tickerLogo)
        view.findViewById<ImageUnify>(R.id.ticker_background_image).loadImageWithoutPlaceholder(data.tickerBackground)
        view.findViewById<Typography>(R.id.ticker_text).text = data.tickerText?.parseAsHtml()
        renderWebViewTicker()
    }

    private fun renderWebViewTicker() {
        getWebViewVersion().let { version ->
            if(errorWebViewCondition(version)){
                showWebViewTicker()
            }
        }
    }

    // WebViewCompat code with exception handling
    private fun getWebViewVersion(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val info = WebView.getCurrentWebViewPackage()
                info?.versionName.orEmpty()
            }catch (e: Exception){
                String.EMPTY
            }
        }else {
            try {
                val webViewFactoryClass =
                    Class.forName("android.webkit.WebViewFactory")
                return (webViewFactoryClass.getMethod(
                    "getLoadedPackageInfo"
                ).invoke(null) as? PackageInfo)?.versionName.orEmpty()
            }catch (e: Exception){
                String.EMPTY
            }
        }
    }

    private fun errorWebViewCondition(version: String): Boolean{
        return version.split(".").firstOrNull().toIntOrZero() < 70
    }

    private fun showWebViewTicker(){
        val tickerText = itemView.context.resources?.getString(epharmacyR.string.epharmacy_webview_ticker).orEmpty()
        webViewTicker.setHtmlDescription(tickerText)
        webViewTicker.show()
    }
}
