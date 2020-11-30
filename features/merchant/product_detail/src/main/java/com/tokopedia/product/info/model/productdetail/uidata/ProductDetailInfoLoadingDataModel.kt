package com.tokopedia.product.info.model.productdetail.uidata

import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactory

/**
 * Created by Yehezkiel on 12/10/20
 */
data class ProductDetailInfoLoadingDataModel(
        val isLoading: Boolean = false
) : ProductDetailInfoVisitable {

    companion object {
        const val LOADING_ID = 999
    }

    override fun newInstance(): ProductDetailInfoVisitable {
        return this.copy()
    }

    override fun uniqueIdentifier(): Int = LOADING_ID

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