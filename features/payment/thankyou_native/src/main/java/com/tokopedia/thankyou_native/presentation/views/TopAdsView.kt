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
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel
import com.tokopedia.topads.sdk.utils.TdnHelper
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.thanks_topads_view.view.*

class TopAdsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
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
            if (topAdsParams.sectionTitle.isNotBlank()) {
                findViewById<TextView>(R.id.tvFeatureTitle).text = topAdsParams.sectionTitle
            } else {
                findViewById<TextView>(R.id.tvFeatureTitle).gone()
            }

            if (topAdsParams.sectionDescription.isNullOrBlank()) {
                findViewById<TextView>(R.id.tvFeatureDescription).gone()
            } else {
                findViewById<TextView>(R.id.tvFeatureDescription).text = topAdsParams.sectionDescription
            }

            val topAdsImageUiModels = mutableListOf<TopAdsImageUiModel>()
            topAdsParams.topAdsUIModelList?.forEach {
                topAdsImageUiModels.add(it.topAdsImageUiModel)
            }
            val tdnBannerList = TdnHelper.categoriesTdnBanners(topAdsImageUiModels)
            if (!tdnBannerList.isNullOrEmpty()) {
                tdnBannerView?.renderTdnBanner(
                    tdnBannerList.first(),
                    BANNER_CORNER_RADIUS_DP.toPx(),
                    ::onTdnBannerClicked
                )
            }
        } else {
            gone()
        }
    }

    private fun onTdnBannerClicked(imageData: TopAdsImageUiModel) {
        if (!imageData.applink.isNullOrBlank()) RouteManager.route(tdnBannerView.context, imageData.applink)
    }

    companion object {
        private const val BANNER_CORNER_RADIUS_DP = 8
    }
}
