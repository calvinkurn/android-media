package com.tokopedia.createpost.producttag.analytic.coordinator

import com.tokopedia.createpost.producttag.analytic.product.ProductTagAnalytic
import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
class ProductImpressionCoordinator @Inject constructor(
    private val analytic: ProductTagAnalytic,
) {
    private val mProductImpress = mutableListOf<Pair<ProductUiModel, Int>>()

    private var mTagSource: ProductTagSource = ProductTagSource.Unknown
    private var mIsEntryPoint: Boolean = false

    fun setInitialData(
        source: ProductTagSource,
        isEntryPoint: Boolean,
    ) {
        mTagSource = source
        mIsEntryPoint = isEntryPoint
    }

    fun saveProductImpress(productImpress: List<Pair<ProductUiModel, Int>>) {
        mProductImpress.addAll(productImpress)
    }

    fun sendProductImpress() {
        val finalProduct = mProductImpress.distinctBy { it.first.id }

        if(finalProduct.isEmpty()) return

        if(mTagSource == ProductTagSource.Shop) {
            analytic.impressProductCardOnShop(finalProduct)
        }
        else {
            analytic.impressProductCard(
                mTagSource,
                finalProduct,
                mIsEntryPoint,
            )
        }

        analytic.sendAll()
        mProductImpress.clear()
    }
}