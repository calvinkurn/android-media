package com.tokopedia.product.info.model.productdetail

import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactory

/**
 * Created by Yehezkiel on 12/10/20
 */
data class ProductDetailInfoHeaderDataModel(
        val img: String = "",
        val productTitle: String = ""
) : ProductDetailInfoVisitable {

    override fun equalsWith(newData: ProductDetailInfoVisitable): Boolean {
        return if (newData is ProductDetailInfoHeaderDataModel) {
            img == newData.img && productTitle == newData.productTitle
        } else false
    }

    override fun type(typeFactory: ProductDetailInfoAdapterFactory): Int {
        return typeFactory.type(this)
    }
}