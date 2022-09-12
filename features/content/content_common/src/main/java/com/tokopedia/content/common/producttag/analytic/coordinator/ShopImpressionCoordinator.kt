package com.tokopedia.content.common.producttag.analytic.coordinator

import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.producttag.view.uimodel.ShopUiModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 24, 2022
 */
class ShopImpressionCoordinator @Inject constructor() {

    private val mShopImpress = mutableListOf<Pair<ShopUiModel, Int>>()

    private var mAnalytic: ContentProductTagAnalytic? = null
    private var mTagSource: ProductTagSource = ProductTagSource.Unknown

    fun setInitialData(
        analytic: ContentProductTagAnalytic?,
        source: ProductTagSource
    ) {
        mAnalytic = analytic
        mTagSource = source
    }

    fun saveShopImpress(shopImpress: List<Pair<ShopUiModel, Int>>) {
        mShopImpress.addAll(shopImpress)
    }

    fun sendShopImpress() {
        val finalShop = mShopImpress.distinctBy { it.first.shopId }

        if(finalShop.isEmpty()) return

        mAnalytic?.impressShopCard(mTagSource, finalShop)
        mAnalytic?.sendAll()

        mShopImpress.clear()
    }
}