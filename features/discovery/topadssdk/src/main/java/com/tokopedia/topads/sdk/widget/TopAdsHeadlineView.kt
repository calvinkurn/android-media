package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.di.DaggerTopAdsComponent
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.listener.TopAdsAddToCartClickListener
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.listener.TopAdsShopFollowBtnClickListener
import com.tokopedia.topads.sdk.shopwidgetthreeproducts.listener.ShopWidgetAddToCartClickListener
import com.tokopedia.topads.sdk.viewmodel.TopAdsHeadlineViewModel
import com.tokopedia.topads.sdk.viewmodel.TopAdsImageViewViewModel
import com.tokopedia.unifycomponents.LoaderUnify
import java.lang.ref.WeakReference
import javax.inject.Inject

class TopAdsHeadlineView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    @JvmField @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null
    private val topAdsHeadlineViewModel by lazy {
        val vm = viewModelFactory?.let {
            ViewModelProvider(context as AppCompatActivity,
                it
            ).get(TopAdsHeadlineViewModel::class.java)
        }
        WeakReference(vm)
    }

    var topadsBannerView: TopAdsBannerView
        private set
    private val shimmerView: LoaderUnify

    init {
        val view = View.inflate(context, R.layout.layout_widget_topads_headline, this)
        topadsBannerView = view.findViewById(R.id.top_ads_banner)
        shimmerView= view.findViewById(R.id.shimmer_view)
        initDagger()
        topadsBannerView.setTopAdsBannerClickListener(object : TopAdsBannerClickListener {
            override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {
                applink?.let { RouteManager.route(context, it) }
            }
        })
        topadsBannerView.setTopAdsImpressionListener(object : TopAdsItemImpressionListener(){
        })
    }

    private fun initDagger() {
        val application = context.applicationContext as BaseMainApplication
        val component = DaggerTopAdsComponent.builder()
            .baseAppComponent(application.baseAppComponent)
            .build()
        component.inject(this)
    }

    fun getHeadlineAds(params: String, onSuccess: ((CpmModel) -> Unit)? = null, onError: (() -> Unit)? = null) {
        topAdsHeadlineViewModel.get()?.getTopAdsHeadlineData(params, onSuccess, onError)
    }

    fun displayAds(cpmModel: CpmModel, index:Int = 0) {
        topadsBannerView.displayAdsWithProductShimmer(cpmModel, index = index)
    }

    fun setTopAdsBannerClickListener(context: TopAdsBannerClickListener) {
        topadsBannerView.setTopAdsBannerClickListener(context)
    }

    fun setTopAdsProductItemListsner(context: TopAdsItemImpressionListener) {
        topadsBannerView.setTopAdsImpressionListener(context)
    }

    fun setFollowBtnClickListener(context: TopAdsShopFollowBtnClickListener) {
        topadsBannerView.setTopAdsShopFollowClickListener(context)
    }

    fun showShimmerView() {
        shimmerView.show()
    }

    fun hideShimmerView() {
        shimmerView.hide()
    }

    fun setHasAddToCartButton(hasAddToCartButton: Boolean) {
        topadsBannerView.setHasAddToCartButton(hasAddToCartButton)
    }

    fun setAddToCartClickListener(topAdsAddToCartClickListener: TopAdsAddToCartClickListener) {
        topadsBannerView.setAddToCartClickListener(topAdsAddToCartClickListener)
    }

    fun setShopWidgetAddToCartClickListener(shopWidgetAddToCartClickListener: ShopWidgetAddToCartClickListener) {
        topadsBannerView.setShopWidgetAddToCartClickListener(shopWidgetAddToCartClickListener)
    }

    fun setShowCta(isShowCta: Boolean) {
        topadsBannerView.setShowCta(isShowCta)
    }

}
