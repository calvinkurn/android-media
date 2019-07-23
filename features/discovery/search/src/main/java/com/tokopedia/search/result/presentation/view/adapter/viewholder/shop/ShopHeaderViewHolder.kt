package com.tokopedia.search.result.presentation.view.adapter.viewholder.shop

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import kotlinx.android.synthetic.main.search_result_shop_header_layout.view.*

class ShopHeaderViewHolder(
        itemView: View,
        val bannerAdsListener: BannerAdsListener?
): AbstractViewHolder<ShopHeaderViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_result_shop_header_layout
    }

    init {
        itemView.adsBannerView?.setTopAdsBannerClickListener { position, applink, data ->
            bannerAdsListener?.onBannerAdsClicked(position, applink, data)
        }

        itemView.adsBannerView?.setTopAdsImpressionListener(object : TopAdsItemImpressionListener() {
            override fun onImpressionHeadlineAdsItem(position: Int, data: CpmData) {
                bannerAdsListener?.onBannerAdsImpressionListener(position, data)
            }
        })
    }

    override fun bind(shopHeaderViewModel: ShopHeaderViewModel?) {
        if(shopHeaderViewModel == null) return

        initCpmModel(shopHeaderViewModel)
        initTextViewShopCount(shopHeaderViewModel)
    }

    private fun initCpmModel(shopHeaderViewModel: ShopHeaderViewModel) {
        itemView.adsBannerView?.displayAds(shopHeaderViewModel.cpmModel)
    }

    private fun initTextViewShopCount(shopHeaderViewModel: ShopHeaderViewModel) {
        itemView.textViewShopCount?.showWithCondition(shopHeaderViewModel.totalShopCount > 0)

        itemView.textViewShopCount?.text = getString(R.string.shop_total_count, shopHeaderViewModel.totalShopCount.toString())
    }
}