package com.tokopedia.product.info.model.productdetail.uidata

import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactory

/**
 * Created by Yehezkiel on 14/10/20
 */
data class ProductDetailInfoExpandableImageDataModel(
        var componentName: Int = 0,
        var title: String = "",
        var imageUrl: String = "",
        var isShowable: Boolean = false
) : ProductDetailInfoVisitable {

    override fun uniqueIdentifier(): Int = componentName

    override fun newInstance(): ProductDetailInfoVisitable = this.copy()

    override fun equalsWith(newData: ProductDetailInfoVisitable): Boolean {
        return if (newData is ProductDetailInfoExpandableImageDataModel) {
            title == newData.title && imageUrl == newData.imageUrl
        } else false
    }

    override fun setIsShowable(isShowable: Boolean) {
        this.isShowable = isShowable
    }

    override fun isExpand(): Boolean = isShowable

    override fun type(typeFactory: ProductDetailInfoAdapterFactory): Int {
        return typeFactory.type(this)
    }
}

