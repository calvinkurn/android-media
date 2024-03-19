package com.tokopedia.topads.sdk.v2.shopadslayout8or9

import android.content.Context
import android.view.View
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.common.adapter.Item
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.ShopAdsWithThreeProductModel
import com.tokopedia.topads.sdk.widget.ITEM_3
import com.tokopedia.topads.sdk.v2.base.BaseBannerAdsRendering
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.listener.ShopWidgetAddToCartClickListener
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.widget.ShopAdsWithThreeProducts
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerProductShimmerUiModel
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopProductUiModel
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopUiModel
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopViewMoreUiModel
import java.lang.ref.WeakReference
import java.util.ArrayList

class ShopAdsLayout8or9View(
    view: View?,
    contextRef: WeakReference<Context>,
    private val topAdsBannerViewClickListener: TopAdsBannerClickListener?,
    private val impressionListener: TopAdsItemImpressionListener?,
    private val shopWidgetAddToCartClickListener: ShopWidgetAddToCartClickListener?
) : BaseBannerAdsRendering(
    view,
    contextRef
) {

    private val shopAdsWithThreeProducts = view?.findViewById<ShopAdsWithThreeProducts>(R.id.shopAdsWithThreeProducts)

    private var hasAddProductToCartButton: Boolean = false
    private var isShowCta: Boolean = false

    override fun render(cpmModel: CpmModel, cpmData: CpmData, appLink: String, adsClickUrl: String) {
        setWidget(cpmData, appLink, adsClickUrl)
    }

    fun setIsShowCta(isShowCta: Boolean) {
        this.isShowCta = isShowCta
    }

    fun setHasAddProductToCartButton(hasAddProductToCartButton: Boolean) {
        this.hasAddProductToCartButton = hasAddProductToCartButton
    }

    protected open fun setWidget(cpmData: CpmData, appLink: String, adsClickUrl: String) {
        shopAdsWithThreeProducts?.setWidgetModel(
            ShopAdsWithThreeProductModel(
                isOfficial = cpmData.cpm.cpmShop.isPowerMerchant,
                isPMPro = cpmData.cpm.cpmShop.isPMPro,
                isPowerMerchant = cpmData.cpm.cpmShop.isPowerMerchant,
                shopBadge = cpmData.cpm.badges.firstOrNull()?.imageUrl ?: "",
                shopName = cpmData.cpm.cpmShop.name,
                shopImageUrl = cpmData.cpm.cpmImage.fullEcs,
                shopWidgetImageUrl = cpmData.cpm.widgetImageUrl,
                merchantVouchers = cpmData.cpm.cpmShop.merchantVouchers,
                listItems = getItems(cpmData, appLink, adsClickUrl),
                items = cpmData,
                shopApplink = appLink,
                adsClickUrl = adsClickUrl,
                topAdsBannerClickListener = topAdsBannerViewClickListener,
                hasAddToCartButton = hasAddProductToCartButton,
                impressionListener = impressionListener,
                shopWidgetAddToCartClickListener = shopWidgetAddToCartClickListener,
                variant = cpmData.cpm.layout
            )
        )
    }

    private fun getItems(cpmData: CpmData, appLink: String, adsClickUrl: String): MutableList<Item<*>> {
        val items = ArrayList<Item<*>>()
        val shop = cpmData.cpm.cpmShop

        items.add(
            BannerShopUiModel(
                cpmData,
                appLink,
                adsClickUrl,
                isShowCta
            )
        )
        if (cpmData.cpm.cpmShop.products.isNotEmpty()) {
            val productCardModelList = getProductCardModels(cpmData.cpm.cpmShop.products, hasAddProductToCartButton)
            for (i in 0 until productCardModelList.size) {
                if (i < ITEM_3) {
                    val product = shop.products[i]

                    val model = BannerShopProductUiModel(
                        cpmData,
                        productCardModelList[i],
                        product.applinks,
                        product.image.m_url,
                        product.imageProduct.imageClickUrl
                    ).apply {
                        productId = product.id
                        productName = product.name
                        productMinOrder = product.productMinimumOrder
                        productCategory = product.categoryBreadcrumb
                        productPrice = product.priceFormat
                        shopId = cpmData.cpm.cpmShop.id
                    }
                    items.add(model)
                }
            }
            if (productCardModelList.size < ITEM_3) {
                items.add(
                    BannerShopViewMoreUiModel(
                        cpmData,
                        appLink,
                        adsClickUrl
                    )
                )
            }
        } else {
            repeat(ITEM_3) { items.add(BannerProductShimmerUiModel()) }
        }
        return items
    }
}
