package com.tokopedia.topads.sdk.shopwidgetthreeproducts.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.shopwidgetthreeproducts.listener.ShopWidgetAddToCartClickListener
import com.tokopedia.topads.sdk.shopwidgetthreeproducts.model.ProductItemModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

class ProductItemViewHolder(
    itemView: View,
    private val topAdsBannerClickListener: TopAdsBannerClickListener?,
    private val impressionListener: TopAdsItemImpressionListener?,
    private val shopWidgetAddToCartClickListener: ShopWidgetAddToCartClickListener?
) : AbstractViewHolder<ProductItemModel>(itemView) {

    private val productCardGridView: ProductCardGridView = itemView.findViewById(R.id.product_item)
    private val topAdsUrlHitter: TopAdsUrlHitter by lazy {
        TopAdsUrlHitter(itemView.context)
    }

    override fun bind(item: ProductItemModel) {
        val productCardViewModel = item.productCardModel
        productCardGridView.run {
            applyCarousel()
            setProductModel(productCardViewModel)
            setImageProductViewHintListener(item, object : ViewHintListener {
                override fun onViewHint() {
                    impressionListener?.onImpressionProductAdsItem(
                        adapterPosition,
                        item.cpmData.cpm.cpmShop.products.getOrNull(adapterPosition - 1),
                        item.cpmData
                    )
                    impressionListener?.onImpressionHeadlineAdsItem(adapterPosition, item.cpmData)
                }
            })
            setOnClickListener {
                topAdsBannerClickListener?.onBannerAdsClicked(
                    adapterPosition,
                    item.applinks, item.cpmData
                )
                topAdsUrlHitter.hitClickUrl(
                    ProductItemViewHolder::javaClass.name,
                    item.adsClickUrl,
                    "",
                    "",
                    ""
                )
            }
            setAddToCartOnClickListener {
                shopWidgetAddToCartClickListener?.onAdToCartClicked(item)
            }
        }
    }

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.layout_ads_banner_shop_a_product
    }

}