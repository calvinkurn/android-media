package com.tokopedia.topads.sdk.v2.shopadslayout6

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.v2.base.BaseBannerAdsRendering
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.v2.shopadslayout6.listener.TopAdsCarouselListener
import com.tokopedia.topads.sdk.v2.shopadslayout6.uimodel.TopAdsCarouselModel
import com.tokopedia.topads.sdk.v2.shopadslayout6.widget.ToadsCarousel
import java.lang.ref.WeakReference

class ShopAdsLayout6View(
    view: View?,
    contextRef: WeakReference<Context>,
    topAdsUrlHitter: TopAdsUrlHitter,
    private val topAdsBannerViewClickListener: TopAdsBannerClickListener?,
    private val impressionListener: TopAdsItemImpressionListener?
) : BaseBannerAdsRendering(
    view,
    contextRef,
    topAdsUrlHitter
) {

    private var currentClassName = className(ShopAdsLayout6View::class.java.simpleName)
    override fun render(cpmModel: CpmModel, cpmData: CpmData, appLink: String, adsClickUrl: String) {
        val container = view?.findViewById<View>(R.id.container)
        val topAdsCarousel = view?.findViewById<ToadsCarousel>(R.id.TopAdsCarousel)

        container?.background = contextRef.get()?.let {
            ContextCompat.getDrawable(it, R.drawable.bg_os_gradient)
        }

        setTopAdsCarousel(cpmModel, topAdsCarousel)
    }

    protected fun setTopAdsCarousel(cpmModel: CpmModel, topAdsCarousel: ToadsCarousel?) {
        topAdsCarousel?.setTopAdsCarouselModel(
            TopAdsCarouselModel(
                title = cpmModel.data.firstOrNull()?.cpm?.widgetTitle ?: "",
                items = getTopAdsItem(cpmModel)
            ),
            object : TopAdsCarouselListener {
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
                    topAdsBannerViewClickListener?.onBannerAdsClicked(
                        position,
                        cpmData?.applinks,
                        cpmData
                    )
                    topAdsUrlHitter?.hitClickUrl(
                        currentClassName,
                        cpmData?.adClickUrl,
                        cpmData?.cpm?.cpmShop?.id,
                        cpmData?.cpm?.uri,
                        cpmData?.cpm?.cpmImage?.fullEcs
                    )
                }

                override fun onProductItemClicked(productIndex: Int, shopIndex: Int) {
                    val cpmData = cpmModel?.data?.getOrNull(shopIndex) ?: return
                    val cpmDataProducts = cpmData.cpm.cpmShop.products

                    if (!cpmDataProducts.isNullOrEmpty()) {
                        val product = cpmDataProducts[productIndex]
                        topAdsBannerViewClickListener?.onBannerAdsClicked(
                            productIndex,
                            product.applinks,
                            cpmData
                        )
                        topAdsUrlHitter?.hitClickUrl(
                            currentClassName,
                            product.imageProduct.imageClickUrl,
                            product.id,
                            product.uri,
                            product.imageProduct.imageUrl
                        )
                    }
                }
            }
        )
    }

    private fun getTopAdsItem(cpmModel: CpmModel?): List<TopAdsCarouselModel.TopAdsCarouselItem> {
        return cpmModel?.data?.mapIndexed { index, it ->
            val products = it.cpm.cpmShop.products
            val imageUrlOne = products.getOrNull(0)?.imageProduct?.imageUrl ?: ""
            val imageUrlTwo = products.getOrNull(1)?.imageProduct?.imageUrl ?: ""

            TopAdsCarouselModel.TopAdsCarouselItem(
                imageUrlOne = imageUrlOne,
                imageUrlTwo = imageUrlTwo,
                brandIcon = it.cpm.cpmImage.fullEcs,
                brandName = it.cpm.cpmShop.name,
                isOfficial = it.cpm.cpmShop.isOfficial,
                isPMPro = it.cpm.cpmShop.isPMPro,
                goldShop = if (it.cpm.cpmShop.isPowerMerchant) 1 else 0,
                impressHolder = it.cpm.cpmShop.imageShop,
                position = index
            )
        }.orEmpty()
    }
}
