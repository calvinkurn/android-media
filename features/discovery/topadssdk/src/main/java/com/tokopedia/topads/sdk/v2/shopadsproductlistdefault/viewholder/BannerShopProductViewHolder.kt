package com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.productcard.reimagine.ProductCardGridView
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.common.adapter.viewholder.AbstractViewHolder
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.v2.listener.TopAdsAddToCartClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopProductUiModel

/**
 * Created by errysuprayogi on 4/16/18.
 */

class BannerShopProductViewHolder(
    container: View,
    private val topAdsBannerClickListener: TopAdsBannerClickListener?,
    private val impressionListener: TopAdsItemImpressionListener?,
    private val addToCartClickListener: TopAdsAddToCartClickListener?
) : AbstractViewHolder<BannerShopProductUiModel?>(container) {
    private val productCardGridView: ProductCardGridView = itemView.findViewById(R.id.product_item)
    private val topAdsUrlHitter: TopAdsUrlHitter by lazy {
        TopAdsUrlHitter(itemView.context)
    }

    override fun bind(element: BannerShopProductUiModel?) {
        element?.let { model ->
            val productCardViewModel = model.product.copy(isInBackground = true)
            productCardGridView.run {
                setProductModel(ProductCardModel.from(productCardViewModel))
                val product = model.cpmData.cpm.cpmShop.products.getOrNull(absoluteAdapterPosition)
                product?.let {
                    addOnImpressionListener(it.imageProduct) {
                        impressionListener?.onImpressionProductAdsItem(
                            absoluteAdapterPosition,
                            product,
                            model.cpmData
                        )
                    }
                }
                setOnClickListener {
                    topAdsBannerClickListener?.onBannerAdsClicked(absoluteAdapterPosition,
                            model.appLink, model.cpmData)
                    topAdsUrlHitter.hitClickUrl(className, model.adsClickUrl, "", "", "")
                }
                addToCartClickListener?.let { listener ->
                    setAddToCartOnClickListener {
                        listener.onAdToCartClicked(element)
                    }
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
