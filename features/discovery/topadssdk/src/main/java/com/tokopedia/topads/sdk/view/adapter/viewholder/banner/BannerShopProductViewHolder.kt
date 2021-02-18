package com.tokopedia.topads.sdk.view.adapter.viewholder.banner

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductViewModel

/**
 * Created by errysuprayogi on 4/16/18.
 */

class BannerShopProductViewHolder(container: View, private val topAdsBannerClickListener: TopAdsBannerClickListener?,
                                  private val impressionListener: TopAdsItemImpressionListener?) : AbstractViewHolder<BannerShopProductViewModel?>(container) {
    private val productCardGridView: ProductCardGridView = itemView.findViewById(R.id.product_item)
    private val topAdsUrlHitter: TopAdsUrlHitter by lazy {
        TopAdsUrlHitter(itemView.context)
    }

    override fun bind(element: BannerShopProductViewModel?) {
        element?.let { model ->
            val productCardViewModel = model.product
            productCardGridView.run {
                applyCarousel()
                setProductModel(productCardViewModel)
                setImageProductViewHintListener(model, object : ViewHintListener {
                    override fun onViewHint() {
                        impressionListener?.onImpressionHeadlineAdsItem(adapterPosition, model.cpmData)
                    }
                })
                setOnClickListener {
                    topAdsBannerClickListener?.onBannerAdsClicked(adapterPosition,
                            model.appLink, model.cpmData)
                    topAdsUrlHitter.hitClickUrl(className, model.adsClickUrl, "", "", "")
                }
            }
        }
    }

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.layout_ads_banner_shop_a_product
        private val className = BannerShopProductViewHolder::class.java.simpleName
    }

}