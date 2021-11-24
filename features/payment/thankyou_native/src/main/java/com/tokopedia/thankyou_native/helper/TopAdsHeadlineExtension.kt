package com.tokopedia.thankyou_native.helper

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.fragment.ThankYouBaseFragment
import com.tokopedia.topads.sdk.widget.TopAdsHeadlineView

fun Context.getTopAdsHeadlinesView(): TopAdsHeadlineView {
    return LayoutInflater.from(this)
        .inflate(R.layout.thanks_item_top_ads_headlines_view, null, false) as TopAdsHeadlineView
}

fun LinearLayout.attachTopAdsHeadlinesView(above: Boolean, topadsView: TopAdsHeadlineView) {
    if (above) {
        this.findViewWithTag<LinearLayout>(ThankYouBaseFragment.TOP_ADS_HEADLINE_ABOVE_RECOM)
            ?.addView(topadsView)
    } else {
        this.findViewWithTag<LinearLayout>(ThankYouBaseFragment.TOP_ADS_HEADLINE_BELOW_RECOM)
            ?.addView(topadsView)
    }
}

fun LinearLayout.addContainer(tag: String) {
    val ll = LinearLayout(context)
    ll.tag = tag
    addView(ll)
}