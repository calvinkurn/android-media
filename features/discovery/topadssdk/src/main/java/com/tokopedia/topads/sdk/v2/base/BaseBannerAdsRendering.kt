package com.tokopedia.topads.sdk.v2.base

import android.content.Context
import android.view.View
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.v2.uimodel.ShopAdsWithSingleProductModel
import java.lang.ref.WeakReference

abstract class BaseBannerAdsRendering(
    val view: View?,
    val contextRef: WeakReference<Context>,
    val topAdsUrlHitter: TopAdsUrlHitter? = null,
    val isReimagine: Boolean = false
) {
    abstract fun render(cpmModel: CpmModel, cpmData: CpmData, appLink: String, adsClickUrl: String)

    val className: (String) -> String = { currentClassName ->
        "com.tokopedia.topads.sdk.widget.TopAdsBannerView.$currentClassName"
    }

    protected fun getSingleAdsProductModel(
        cpmData: CpmData,
        appLink: String,
        adsClickUrl: String,
        topAdsBannerClickListener: TopAdsBannerClickListener?,
        hasAddProductToCartButton: Boolean,
        impressionListener: TopAdsItemImpressionListener?
    ): ShopAdsWithSingleProductModel {
        return ShopAdsWithSingleProductModel(
            isOfficial = cpmData.cpm.cpmShop.isOfficial,
            isPMPro = cpmData.cpm.cpmShop.isPMPro,
            isPowerMerchant = cpmData.cpm.cpmShop.isPowerMerchant,
            shopBadge = cpmData.cpm.badges.firstOrNull()?.imageUrl ?: "",
            shopName = cpmData.cpm.cpmShop.name,
            shopImageUrl = cpmData.cpm.cpmImage.fullEcs,
            slogan = cpmData.cpm.cpmShop.slogan,
            shopWidgetImageUrl = cpmData.cpm.widgetImageUrl,
            merchantVouchers = cpmData.cpm.cpmShop.merchantVouchers,
            listItem = cpmData.cpm.cpmShop.products.firstOrNull(),
            shopApplink = appLink,
            adsClickUrl = adsClickUrl,
            hasAddToCartButton = hasAddProductToCartButton,
            variant = cpmData.cpm.layout,
            topAdsBannerClickListener = topAdsBannerClickListener,
            impressionListener = impressionListener,
            cpmData = cpmData,
            impressHolder = cpmData.cpm.cpmShop.imageShop
        )
    }
}
