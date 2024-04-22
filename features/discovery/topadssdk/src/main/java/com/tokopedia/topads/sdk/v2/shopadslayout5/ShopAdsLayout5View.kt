package com.tokopedia.topads.sdk.v2.shopadslayout5

import android.content.Context
import android.view.View
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.v2.base.BaseBannerAdsRendering
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.v2.shopadslayout5.listener.ShopAdsProductListener
import com.tokopedia.topads.sdk.v2.shopadslayout5.uimodel.ShopProductModel
import com.tokopedia.topads.sdk.v2.shopadslayout5.widget.ShopAdsWithOneProductReimagineView
import com.tokopedia.topads.sdk.v2.shopadslayout5.widget.ShopAdsWithOneProductView
import java.lang.ref.WeakReference

class ShopAdsLayout5View(
    view: View?,
    contextRef: WeakReference<Context>,
    topAdsUrlHitter: TopAdsUrlHitter,
    private val topAdsBannerViewClickListener: TopAdsBannerClickListener?,
    private val impressionListener: TopAdsItemImpressionListener?,
    isReimagine: Boolean = false
) : BaseBannerAdsRendering(
    view,
    contextRef,
    topAdsUrlHitter,
    isReimagine
) {

    private var currentClassName = className(ShopAdsLayout5View::class.java.simpleName)
    override fun render(cpmModel: CpmModel, cpmData: CpmData, appLink: String, adsClickUrl: String) {
        val container = view?.findViewById<View>(R.id.container)

        container?.setBackgroundResource(0)

        if (isReimagine) {
            setShopAdsProductReimagine(cpmModel)
        } else {
            setShopAdsProduct(cpmModel)
        }
    }

    private fun setShopAdsProductReimagine(cpmModel: CpmModel) {
        val shopAdsProductView = view?.findViewById<ShopAdsWithOneProductReimagineView>(R.id.shopAdsProductView)

        shopAdsProductView?.setShopProductModel(
            ShopProductModel(
                title = cpmModel.data.firstOrNull()?.cpm?.widgetTitle ?: "",
                items = getShopProductItem(cpmModel)
            ),
            object : ShopAdsProductListener {
                override fun onItemImpressed(position: Int) {
                    val cpmData = cpmModel.data.getOrNull(position)
                    cpmData?.let { impressionListener?.onImpressionHeadlineAdsItem(position, it) }
                    topAdsUrlHitter?.hitImpressionUrl(
                        currentClassName,
                        cpmData?.cpm?.cpmImage?.fullUrl,
                        cpmData?.cpm?.cpmShop?.id,
                        cpmData?.cpm?.uri,
                        cpmData?.cpm?.cpmImage?.fullEcs
                    )
                }

                override fun onItemClicked(position: Int) {
                    val cpmData = cpmModel.data.getOrNull(position)
                    topAdsBannerViewClickListener?.onBannerAdsClicked(position, cpmData?.applinks, cpmData)
                    topAdsUrlHitter?.hitClickUrl(
                        currentClassName,
                        cpmData?.adClickUrl,
                        cpmData?.cpm?.cpmShop?.id,
                        cpmData?.cpm?.uri,
                        cpmData?.cpm?.cpmImage?.fullEcs
                    )
                }
            },
            null
        )
    }

    private fun setShopAdsProduct(cpmModel: CpmModel) {
        val shopAdsWithOneProductView = view?.findViewById<ShopAdsWithOneProductView>(R.id.shopAdsProductView)

        shopAdsWithOneProductView?.setShopProductModel(
            ShopProductModel(
                title = cpmModel.data.firstOrNull()?.cpm?.widgetTitle ?: "",
                items = getShopProductItem(cpmModel)
            ),
            object : ShopAdsProductListener {
                override fun onItemImpressed(position: Int) {
                    val cpmData = cpmModel.data.getOrNull(position)
                    cpmData?.let { impressionListener?.onImpressionHeadlineAdsItem(position, it) }
                    topAdsUrlHitter?.hitImpressionUrl(
                        currentClassName,
                        cpmData?.cpm?.cpmImage?.fullUrl,
                        cpmData?.cpm?.cpmShop?.id,
                        cpmData?.cpm?.uri,
                        cpmData?.cpm?.cpmImage?.fullEcs
                    )
                }

                override fun onItemClicked(position: Int) {
                    val cpmData = cpmModel.data.getOrNull(position)
                    topAdsBannerViewClickListener?.onBannerAdsClicked(position, cpmData?.applinks, cpmData)
                    topAdsUrlHitter?.hitClickUrl(
                        currentClassName,
                        cpmData?.adClickUrl,
                        cpmData?.cpm?.cpmShop?.id,
                        cpmData?.cpm?.uri,
                        cpmData?.cpm?.cpmImage?.fullEcs
                    )
                }
            },
            null
        )
    }

    protected fun getShopProductItem(cpmModel: CpmModel): List<ShopProductModel.ShopProductModelItem> {
        return cpmModel.data.mapIndexed { index, it ->
            val product = it.cpm.cpmShop.products.firstOrNull()
            ShopProductModel.ShopProductModelItem(
                imageUrl = product?.imageProduct?.imageUrl ?: "",
                shopIcon = it.cpm.cpmImage.fullEcs,
                shopName = it.cpm.cpmShop.name,
                ratingCount = product?.countReviewFormat ?: "",
                ratingAverage = product?.headlineProductRatingAverage ?: "",
                isOfficial = it.cpm.cpmShop.isOfficial,
                isPMPro = it.cpm.cpmShop.isPMPro,
                goldShop = if (it.cpm.cpmShop.isPowerMerchant) 1 else 0,
                impressHolder = it.cpm.cpmShop.imageShop,
                location = it.cpm.cpmShop.location,
                position = index
            )
        }
    }
}
