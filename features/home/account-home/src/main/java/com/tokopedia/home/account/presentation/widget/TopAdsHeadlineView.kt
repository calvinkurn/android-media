package com.tokopedia.home.account.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.viewmodel.TopAdsHeadlineViewModel
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.widget.TopAdsBannerView

class TopAdsHeadlineView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private val topAdsHeadlineViewModel: TopAdsHeadlineViewModel by lazy {
        ViewModelProvider(context as AppCompatActivity).get(TopAdsHeadlineViewModel::class.java)
    }

    private var topadsBannerView: TopAdsBannerView

    init {
        val view = View.inflate(context, R.layout.layout_widget_topads_headline_old, this)
        topadsBannerView = view.findViewById(R.id.top_ads_banner)
        topadsBannerView.setTopAdsBannerClickListener(TopAdsBannerClickListener { _, appLink, _ ->
            RouteManager.route(context, appLink)
        })
        topadsBannerView.setTopAdsImpressionListener(object : TopAdsItemImpressionListener(){
        })
    }

    fun getHeadlineAds(params: String, onSuccess: ((CpmModel) -> Unit)? = null, onError: (() -> Unit)? = null) {
        topAdsHeadlineViewModel.getTopAdsHeadlineData(params, onSuccess, onError)
    }

    fun displayAds(cpmModel: CpmModel) {
        topadsBannerView.displayAdsWithProductShimmer(cpmModel)
    }

}