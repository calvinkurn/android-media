package com.tokopedia.createpost.producttag.analytic.coordinator

import com.tokopedia.createpost.producttag.analytic.ContentProductTagAnalytic
import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import com.tokopedia.createpost.producttag.view.uimodel.ShopUiModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 24, 2022
 */
class ShopImpressionCoordinator @Inject constructor(
    private val analytic: ContentProductTagAnalytic,
) {
    private val mShopImpress = mutableListOf<Pair<ShopUiModel, Int>>()

    private var mTagSource: ProductTagSource = ProductTagSource.Unknown
    private var mIsGlobalSearch: Boolean = false

    fun setInitialData(source: ProductTagSource) {
        mTagSource = source
    }

    fun saveShopImpress(shopImpress: List<Pair<ShopUiModel, Int>>) {
        mShopImpress.addAll(shopImpress)
    }

    fun sendShopImpress() {
        val finalShop = mShopImpress.distinctBy { it.first.shopId + 1}

        analytic.impressShopCard(
            mTagSource,
            "",
            finalShop,
        )
        analytic.sendAll()

        mShopImpress.clear()
    }
}