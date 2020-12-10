package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.applink.RouteManager
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.viewmodel.TopAdsHeadlineViewModel
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsShopFollowBtnClickListener
import com.tokopedia.topads.sdk.widget.TopAdsBannerView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

class TopAdsHeadlineView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private val topAdsHeadlineViewModel: TopAdsHeadlineViewModel by lazy {
        ViewModelProvider(context as AppCompatActivity).get(TopAdsHeadlineViewModel::class.java)
    }

    private var topadsBannerView: TopAdsBannerView

    init {
        val view = View.inflate(context, R.layout.layout_widget_topads_headline, this)
        topadsBannerView = view.findViewById(R.id.top_ads_banner)
        topadsBannerView.setTopAdsBannerClickListener(TopAdsBannerClickListener { _, appLink, _ ->
            RouteManager.route(context, appLink)
        })
    }

    fun getHeadlineAds(params: String, onSuccess: ((CpmModel) -> Unit)? = null, onError: (() -> Unit)? = null) {
        topAdsHeadlineViewModel.getTopAdsHeadlineDate(params)
        topAdsHeadlineViewModel.getHeadlineAdsLiveData().observe(context as LifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.displayAds.data.isNotEmpty()) {
                        onSuccess?.invoke(it.data.displayAds)
                        displayAds(it.data.displayAds)
                    } else {
                        onError?.invoke()
                    }
                }

                is Fail -> {
                    it.throwable.printStackTrace()
                    onError?.invoke()
                }
            }
        })
    }

    fun displayAds(cpmModel: CpmModel) {
        topadsBannerView.displayAdsWithProductShimmer(cpmModel)
    }

    fun setFollowBtnClickListener(context: TopAdsShopFollowBtnClickListener) {
        topadsBannerView.setTopAdsShopFollowClickListener(context)
    }

}