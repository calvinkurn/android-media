package com.tokopedia.product.info.model.productdetail

import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactory

/**
 * Created by Yehezkiel on 12/10/20
 */
data class ProductDetailInfoLoadingDataModel(
        val isLoading: Boolean = false
): ProductDetailInfoVisitable {

    override fun equalsWith(newData: ProductDetailInfoVisitable): Boolean {
        return false
    }

    override fun type(typeFactory: ProductDetailInfoAdapterFactory): Int {
        return typeFactory.type(this)
    }
}