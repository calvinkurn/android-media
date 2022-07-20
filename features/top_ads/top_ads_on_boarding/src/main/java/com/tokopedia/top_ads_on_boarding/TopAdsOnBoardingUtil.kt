package com.tokopedia.top_ads_on_boarding

import androidx.core.text.HtmlCompat

object TopAdsOnBoardingUtil {

    fun getSpannedText(htmlText: String): CharSequence {
        return HtmlCompat.fromHtml(
            htmlText,
            HtmlCompat.FROM_HTML_MODE_COMPACT
        ).trim()
    }
}
