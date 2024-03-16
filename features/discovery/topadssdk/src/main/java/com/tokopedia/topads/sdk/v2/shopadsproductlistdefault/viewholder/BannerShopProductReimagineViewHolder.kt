package com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.productcard.reimagine.ProductCardGridCarouselView
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.common.adapter.viewholder.AbstractViewHolder
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopProductUiModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.productcard.reimagine.ProductCardModel.Companion as ProductCardModelReimagine

class BannerShopProductReimagineViewHolder(
    container: View,
    private val topAdsBannerClickListener: TopAdsBannerClickListener?,
    private val impressionListener: TopAdsItemImpressionListener?,
) : AbstractViewHolder<BannerShopProductUiModel?>(container) {
    private val productCardGridViewA: ProductCardGridCarouselView =
        itemView.findViewById(R.id.topAdsProductItem)
    private val topAdsUrlHitter: TopAdsUrlHitter by lazy {
        TopAdsUrlHitter(itemView.context)
    }

    override fun bind(element: BannerShopProductUiModel?) {
        element?.let { model ->
            val productCardViewModel = model.product
            val cpmData = model.cpmData
            productCardGridViewA.run {
                setProductModel(ProductCardModelReimagine.from(productCardViewModel))

                val impressHolder = model.cpmData.cpm.cpmShop.products.getOrNull(bindingAdapterPosition)?.imageProduct

                impressHolder?.let {
                    addOnImpressionListener(it) {
                        impressionListener?.onImpressionProductAdsItem(
                            adapterPosition,
                            model.cpmData.cpm.cpmShop.products.getOrNull(adapterPosition - 1),
                            model.cpmData
                        )
                    }
                }

                setOnClickListener {
                    topAdsBannerClickListener?.onBannerAdsClicked(
                        adapterPosition,
                        model.appLink,
                        model.cpmData
                    )
                    topAdsUrlHitter.hitClickUrl(className, model.adsClickUrl, "", "", "")
                }
            }
        }
    }

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.layout_ads_banner_shop_a_product_reimagine
        private val className = BannerShopProductReimagineViewHolder::class.java.simpleName
    }
}
