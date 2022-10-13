package com.tokopedia.product.info.view.models

import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactory

/**
 * Created by Yehezkiel on 12/10/20
 */
data class ProductDetailInfoLoadingDataModel(
    val isLoading: Boolean = false
) : ProductDetailInfoVisitable {

    override fun newInstance(): ProductDetailInfoVisitable {
        return this.copy()
    }

    override fun uniqueIdentifier(): Int = hashCode()

    override fun setIsShowable(isShowable: Boolean) {}

    override fun isExpand(): Boolean {
        return false
    }

    override fun equalsWith(newData: ProductDetailInfoVisitable): Boolean {
        return false
    }

    override fun type(typeFactory: ProductDetailInfoAdapterFactory): Int {
        return typeFactory.type(this)
    }
}