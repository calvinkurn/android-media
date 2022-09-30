package com.tokopedia.product.info.view.models

import com.tokopedia.product.info.data.response.ItemCatalog
import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactory

data class ProductDetailInfoCatalogDataModel(
    val componentName: Int = 0,
    val title: String = "",
    val items: List<ItemCatalog>
) : ProductDetailInfoVisitable {
    override fun uniqueIdentifier(): Int = componentName

    override fun newInstance(): ProductDetailInfoVisitable = this.copy()

    override fun equalsWith(newData: ProductDetailInfoVisitable): Boolean {
        return newData is ProductDetailInfoCatalogDataModel && title == newData.title
    }

    override fun setIsShowable(isShowable: Boolean) {}

    override fun isExpand(): Boolean = false

    override fun type(typeFactory: ProductDetailInfoAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
