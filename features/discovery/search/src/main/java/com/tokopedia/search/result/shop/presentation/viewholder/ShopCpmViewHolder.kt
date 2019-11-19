package com.tokopedia.search.result.shop.presentation.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener
import com.tokopedia.search.result.shop.presentation.model.ShopCpmViewModel
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import kotlinx.android.synthetic.main.search_result_shop_cpm_layout.view.*

internal class ShopCpmViewHolder(
        itemView: View,
        val bannerAdsListener: BannerAdsListener?
): AbstractViewHolder<ShopCpmViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_result_shop_cpm_layout
    }

    init {
        itemView.adsBannerViewSearchShop?.setTopAdsBannerClickListener { position, applink, data ->
            bannerAdsListener?.onBannerAdsClicked(position, applink, data)
        }

        itemView.adsBannerViewSearchShop?.setTopAdsImpressionListener(createTopAdsItemImpressionListener())
    }

    private fun createTopAdsItemImpressionListener(): TopAdsItemImpressionListener {
        return object : TopAdsItemImpressionListener() {
            override fun onImpressionHeadlineAdsItem(position: Int, data: CpmData) {
                bannerAdsListener?.onBannerAdsImpressionListener(position, data)
            }
        }
    }

    override fun bind(shopCpmViewModel: ShopCpmViewModel?) {
        if (shopCpmViewModel == null) return

        initCpmModel(shopCpmViewModel)
    }

    private fun initCpmModel(shopCpmViewModel: ShopCpmViewModel) {
        itemView.adsBannerViewSearchShop?.shouldShowWithAction(shopCpmViewModel.cpmModel.data.size > 0) {
            itemView.adsBannerViewSearchShop?.displayAds(shopCpmViewModel.cpmModel)
        }
    }
}