package com.tokopedia.content.common.producttag.analytic.coordinator

import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
class ProductImpressionCoordinator @Inject constructor() {
    private val mProductImpress = mutableListOf<Pair<ProductUiModel, Int>>()

    private var mAnalytic: ContentProductTagAnalytic? = null
    private var mTagSource: ProductTagSource = ProductTagSource.Unknown
    private var mIsEntryPoint: Boolean = false

    fun setInitialData(
        analytic: ContentProductTagAnalytic?,
        source: ProductTagSource,
        isEntryPoint: Boolean,
    ) {
        mAnalytic = analytic
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
            mAnalytic?.impressProductCardOnShop(finalProduct)
        }
        else {
            mAnalytic?.impressProductCard(
                mTagSource,
                finalProduct,
                mIsEntryPoint,
            )
        }

        mAnalytic?.sendAll()
        mProductImpress.clear()
    }
}