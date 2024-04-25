package com.tokopedia.topads.sdk.v2.shopadslayout10

import android.content.Context
import android.view.View
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.v2.base.BaseBannerAdsRendering
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.v2.shopadslayout10.widget.ShopAdsSingleItemHorizontalLayout
import java.lang.ref.WeakReference

class ShopAdsLayout10View(
    view: View?,
    contextRef: WeakReference<Context>,
    private val topAdsBannerViewClickListener: TopAdsBannerClickListener?,
    private val impressionListener: TopAdsItemImpressionListener?
) : BaseBannerAdsRendering(
    view,
    contextRef
) {

    private val shopAdsWithSingleProductHorizontal = view?.findViewById<ShopAdsSingleItemHorizontalLayout>(R.id.shopAdsWithSingleProductHorizontal)

    private var hasAddProductToCartButton: Boolean = false

    override fun render(cpmModel: CpmModel, cpmData: CpmData, appLink: String, adsClickUrl: String) {
        shopAdsWithSingleProductHorizontal?.setShopProductModel(
            getSingleAdsProductModel(
                cpmData,
                appLink,
                adsClickUrl,
                topAdsBannerViewClickListener,
                hasAddProductToCartButton,
                impressionListener
            )
        )
    }

    fun setHasAddProductToCartButton(hasAddProductToCartButton: Boolean) {
        this.hasAddProductToCartButton = hasAddProductToCartButton
    }
}
