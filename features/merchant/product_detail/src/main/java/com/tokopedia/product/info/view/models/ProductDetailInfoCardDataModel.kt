package com.tokopedia.product.info.view.models

import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactory

data class ProductDetailInfoCardDataModel(
    var componentName: Int = 0,
    var title: String = "",
    var image: String = "",
    var applink: String = ""
) : ProductDetailInfoVisitable {
    override fun uniqueIdentifier(): Int = componentName

    override fun newInstance(): ProductDetailInfoVisitable = this.copy()

    override fun equalsWith(newData: ProductDetailInfoVisitable): Boolean {
        return if (newData is ProductDetailInfoCardDataModel) {
            title == newData.title &&
                    image == newData.image &&
                    applink == newData.applink
        } else false
    }

    override fun setIsShowable(isShowable: Boolean) {}

    override fun isExpand(): Boolean = false

    override fun type(typeFactory: ProductDetailInfoAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
