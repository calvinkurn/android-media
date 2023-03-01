package com.tokopedia.thankyou_native.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.TopAdsRequestParams
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.utils.TdnHelper
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.thanks_topads_view.view.*

class TopAdsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val layout = R.layout.thanks_topads_view

    init {
        LayoutInflater.from(context).inflate(layout, this, true)
    }

    fun addData(
        topAdsParams: TopAdsRequestParams
    ) {
        if (!topAdsParams.topAdsUIModelList.isNullOrEmpty()) {
            visible()
            if (topAdsParams.sectionTitle.isNotBlank())
                findViewById<TextView>(R.id.tvFeatureTitle).text = topAdsParams.sectionTitle
            else
                findViewById<TextView>(R.id.tvFeatureTitle).gone()

            if (topAdsParams.sectionDescription.isNullOrBlank())
                findViewById<TextView>(R.id.tvFeatureDescription).gone()
            else
                findViewById<TextView>(R.id.tvFeatureDescription).text = topAdsParams.sectionDescription

            val topAdsImageViewModels = mutableListOf<TopAdsImageViewModel>()
            topAdsParams.topAdsUIModelList?.forEach {
                topAdsImageViewModels.add(it.topAdsImageViewModel)
            }
            val tdnBannerList = TdnHelper.categoriesTdnBanners(topAdsImageViewModels)
            if (!tdnBannerList.isNullOrEmpty()) {
                tdnBannerView?.renderTdnBanner(
                    tdnBannerList.first(),
                    8.toPx(),
                    ::onTdnBannerClicked,
                )
            }
        } else {
            gone()
        }
    }

    private fun onTdnBannerClicked(applink: String) {
        if (applink.isNotEmpty()) RouteManager.route(tdnBannerView.context, applink)
    }
}
