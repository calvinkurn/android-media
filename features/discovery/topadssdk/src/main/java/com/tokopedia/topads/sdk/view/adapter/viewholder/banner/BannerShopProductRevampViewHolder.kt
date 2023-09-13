package com.tokopedia.topads.sdk.view.adapter.viewholder.banner

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.reimagine.ProductCardGridCarouselView
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.base.adapter.viewholder.AbstractViewHolder
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductUiModel

class BannerShopProductRevampViewHolder(
    container: View,
    private val topAdsBannerClickListener: TopAdsBannerClickListener?,
    private val impressionListener: TopAdsItemImpressionListener?
) : AbstractViewHolder<BannerShopProductUiModel?>(container) {
    private val productCardGridViewA: ProductCardGridCarouselView =
        itemView.findViewById(R.id.topAdsProductItem)
    private val topAdsUrlHitter: TopAdsUrlHitter by lazy {
        TopAdsUrlHitter(itemView.context)
    }

    override fun bind(element: BannerShopProductUiModel?) {
        element?.let { model ->
            val productCardViewModel = model.product
            productCardGridViewA.run {
                setProductModel(mapperToProductModelReimagine(productCardViewModel))

                addOnImpressionListener(element) {
                    impressionListener?.onImpressionProductAdsItem(
                        adapterPosition,
                        model.cpmData.cpm.cpmShop.products.getOrNull(adapterPosition - 1),
                        model.cpmData
                    )
                    impressionListener?.onImpressionHeadlineAdsItem(adapterPosition, model.cpmData)
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

    private fun mapperToProductModelReimagine(item: ProductCardModel): com.tokopedia.productcard.reimagine.ProductCardModel {
        return com.tokopedia.productcard.reimagine.ProductCardModel(
            imageUrl = item.productImageUrl,
            isAds = item.isTopAds,
            name = item.productName,
            price = item.formattedPrice,
            slashedPrice = item.slashedPrice,
            discountPercentage = getDiscountProduct(item),
            labelGroupList = item.labelGroupList.map { labelGroup ->
                com.tokopedia.productcard.reimagine.ProductCardModel.LabelGroup(
                    title = labelGroup.title,
                    position = labelGroup.position,
                    type = labelGroup.type,
                    imageUrl = labelGroup.imageUrl
                )
            },
            rating = item.ratingString,
            freeShipping = com.tokopedia.productcard.reimagine.ProductCardModel.FreeShipping(item.freeOngkir.imageUrl),
            hasMultilineName = false
        )
    }

    private fun getDiscountProduct(item: ProductCardModel): Int {
        val discount = item.discountPercentage.removeSuffix("%")
        return if (discount.isNotEmpty()) discount.toInt() else 0
    }

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.layout_ads_banner_shop_a_product_reimagine
        private val className = BannerShopProductRevampViewHolder::class.java.simpleName
    }
}
