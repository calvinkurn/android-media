package com.tokopedia.search.result.shop.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultShopCpmLayoutBinding
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener
import com.tokopedia.search.result.shop.presentation.model.ShopCpmDataView
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

internal class ShopCpmViewHolder(
    itemView: View,
    val bannerAdsListener: BannerAdsListener?,
    private val isReimagine: Boolean = false
): AbstractViewHolder<ShopCpmDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_result_shop_cpm_layout
    }
    private var binding: SearchResultShopCpmLayoutBinding? by viewBinding()

    init {
        binding?.adsBannerViewSearchShop?.setTopAdsBannerClickListener(object : TopAdsBannerClickListener{
            override fun onBannerAdsClicked(position: Int, applink: String?, data: CpmData?) {
                bannerAdsListener?.onBannerAdsClicked(position, applink, data)
            }
        })

        binding?.adsBannerViewSearchShop?.setTopAdsImpressionListener(createTopAdsItemImpressionListener())
    }

    private fun createTopAdsItemImpressionListener(): TopAdsItemImpressionListener {
        return object : TopAdsItemImpressionListener() {
            override fun onImpressionHeadlineAdsItem(position: Int, data: CpmData) {
                bannerAdsListener?.onBannerAdsImpressionListener(position, data)
            }
        }
    }

    override fun bind(shopCpmDataView: ShopCpmDataView?) {
        if (shopCpmDataView == null) return

        initCpmModel(shopCpmDataView)
    }

    private fun initCpmModel(shopCpmDataView: ShopCpmDataView) {
        binding?.adsBannerViewSearchShop?.displayHeadlineAds(shopCpmDataView.cpmModel.apply {
            isReimagine = this@ShopCpmViewHolder.isReimagine
        })
    }
}
