package com.tokopedia.createpost.producttag.analytic.coordinator

import com.tokopedia.createpost.producttag.analytic.ContentProductTagAnalytic
import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
class ProductImpressionCoordinator @Inject constructor(
    private val analytic: ContentProductTagAnalytic,
) {
    private val mProductImpress = mutableSetOf<Pair<ProductUiModel, Int>>()

    private var mTagSource: ProductTagSource = ProductTagSource.Unknown
    private var mIsGlobalSearch: Boolean = false

    fun setInitialData(
        source: ProductTagSource,
        isGlobalSearch: Boolean,
    ) {
        mTagSource = source
        mIsGlobalSearch = isGlobalSearch
    }

    fun saveProductImpress(productImpress: List<Pair<ProductUiModel, Int>>) {
        mProductImpress.addAll(productImpress)
    }

    fun sendProductImpress() {
        analytic.impressProductCard(
            mTagSource,
            "",
            "",
            mProductImpress.toList(),
            mIsGlobalSearch,
        )
        mProductImpress.clear()
    }
}