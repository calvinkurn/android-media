package com.tokopedia.topads.sdk.v2.shopadslayout2

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.tokopedia.shopwidget.shopcard.ShopCardListener
import com.tokopedia.shopwidget.shopcard.ShopCardModel
import com.tokopedia.shopwidget.shopcard.ShopCardView
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.v2.base.BaseBannerAdsRendering
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener
import com.tokopedia.unifycomponents.toPx
import java.lang.ref.WeakReference

class ShopAdsLayout2View(
    view: View?,
    contextRef: WeakReference<Context>,
    topAdsUrlHitter: TopAdsUrlHitter,
    private val impressionListener: TopAdsItemImpressionListener?,
    private val topAdsBannerClickListener: TopAdsBannerClickListener?,
    isReimagine: Boolean = false
) : BaseBannerAdsRendering(view, contextRef, topAdsUrlHitter, isReimagine) {

    private var currentClassName = className(ShopAdsLayout2View::class.java.simpleName)

    override fun render(cpmModel: CpmModel, cpmData: CpmData, appLink: String, adsClickUrl: String) {
        val adsBannerShopCardViewV2 = view?.findViewById<ShopCardView>(R.id.adsBannerShopCardViewV2)
        val container = view?.findViewById<View>(R.id.container)
        container?.setBackgroundResource(0)

        (container?.layoutParams as? ViewGroup.MarginLayoutParams)?.setMargins(0, 4.toPx(), 0, 0)

        setHeadlineShopDataCardWidget(cpmData, adsBannerShopCardViewV2)
    }

    private fun setHeadlineShopDataCardWidget(
        cpmData: CpmData,
        adsBannerShopCardView: ShopCardView?
    ) {
        val productList = cpmData.cpm.cpmShop.products

        adsBannerShopCardView?.setShopCardModel(
            mapToShopCardModel(cpmData),
            object : ShopCardListener {
                override fun onItemImpressed() {
                    impressionListener?.onImpressionHeadlineAdsItem(0, cpmData)

                    topAdsUrlHitter?.hitImpressionUrl(
                        currentClassName,
                        cpmData.cpm.cpmImage.fullUrl,
                        cpmData.cpm.cpmShop.id,
                        cpmData.cpm.uri,
                        cpmData.cpm.cpmImage.fullEcs
                    )
                }

                override fun onItemClicked() {
                    topAdsBannerClickListener?.onBannerAdsClicked(0, cpmData.applinks, cpmData)

                    topAdsUrlHitter?.hitClickUrl(
                        currentClassName,
                        cpmData.adClickUrl,
                        cpmData.cpm.cpmShop.id,
                        cpmData.cpm.uri,
                        cpmData.cpm.cpmImage.fullEcs
                    )
                }

                override fun onProductItemImpressed(productPreviewIndex: Int) {
                    val product = productList.getOrNull(productPreviewIndex) ?: return
                    impressionListener?.onImpressionProductAdsItem(
                        productPreviewIndex,
                        product,
                        cpmData
                    )
                }

                override fun onProductItemClicked(productPreviewIndex: Int) {
                    val product = productList.getOrNull(productPreviewIndex) ?: return

                    topAdsBannerClickListener?.onBannerAdsClicked(productPreviewIndex, product.applinks, cpmData)

                    topAdsUrlHitter?.hitClickUrl(
                        currentClassName,
                        product.imageProduct.imageClickUrl,
                        product.id,
                        product.uri,
                        product.imageProduct.imageUrl
                    )
                }
            }
        )
    }

    protected fun mapToShopCardModel(cpmData: CpmData): ShopCardModel {
        val productList = cpmData.cpm.cpmShop.products
        return ShopCardModel(
            id = cpmData.cpm.cpmShop.id,
            name = cpmData.cpm.cpmShop.name,
            image = cpmData.cpm.cpmImage.fullEcs,
            location = contextRef.get()?.getString(R.string.title_promote_by).orEmpty(),
            goldShop = if (cpmData.cpm.cpmShop.isPowerMerchant) 1 else 0,
            productList = productList.map {
                ShopCardModel.ShopItemProduct(
                    id = it.id,
                    name = it.name,
                    priceFormat = it.priceFormat,
                    imageUrl = it.imageProduct.imageUrl
                )
            },
            isOfficial = cpmData.cpm.cpmShop.isOfficial,
            isPMPro = cpmData.cpm.cpmShop.isPMPro,
            impressHolder = cpmData.cpm.cpmShop.imageShop,
            isReimagine = isReimagine
        )
    }
}
