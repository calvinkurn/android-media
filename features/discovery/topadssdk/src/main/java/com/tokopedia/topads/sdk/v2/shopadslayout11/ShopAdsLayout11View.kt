package com.tokopedia.topads.sdk.v2.shopadslayout11

import android.content.Context
import android.view.View
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.v2.base.BaseBannerAdsRendering
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.v2.shopadslayout11.widget.ShopAdsSingleItemVerticalLayout
import java.lang.ref.WeakReference

class ShopAdsLayout11View(
    view: View?,
    contextRef: WeakReference<Context>,
    private val topAdsBannerViewClickListener: TopAdsBannerClickListener?,
    private val impressionListener: TopAdsItemImpressionListener?
) : BaseBannerAdsRendering(view, contextRef) {

    private val shopAdsWithSingleProductVertical = view?.findViewById<ShopAdsSingleItemVerticalLayout>(R.id.shopAdsWithSingleProductVertical)

    private var hasAddProductToCartButton: Boolean = false

    override fun render(cpmModel: CpmModel, cpmData: CpmData, appLink: String, adsClickUrl: String) {
        shopAdsWithSingleProductVertical?.setShopProductModel(
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
