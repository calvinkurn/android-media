package com.tokopedia.createpost.producttag.analytic.coordinator

import com.tokopedia.createpost.producttag.analytic.product.ProductTagAnalytic
import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.ShopUiModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 24, 2022
 */
class ShopImpressionCoordinator @Inject constructor(
    private val analytic: ProductTagAnalytic,
) {
    private val mShopImpress = mutableListOf<Pair<ShopUiModel, Int>>()

    private var mTagSource: ProductTagSource = ProductTagSource.Unknown

    fun setInitialData(source: ProductTagSource) {
        mTagSource = source
    }

    fun saveShopImpress(shopImpress: List<Pair<ShopUiModel, Int>>) {
        mShopImpress.addAll(shopImpress)
    }

    fun sendShopImpress() {
        val finalShop = mShopImpress.distinctBy { it.first.shopId }

        if(finalShop.isEmpty()) return

        analytic.impressShopCard(mTagSource, finalShop)
        analytic.sendAll()

        mShopImpress.clear()
    }
}