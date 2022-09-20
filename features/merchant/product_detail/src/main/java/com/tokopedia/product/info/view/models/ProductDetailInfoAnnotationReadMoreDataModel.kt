package com.tokopedia.product.info.view.models

import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoContent
import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactory

/**
 * Created by Yehezkiel on 12/10/20
 */
data class ProductDetailInfoAnnotationReadMoreDataModel(
    val componentId: Int = 0,
    val data: List<ProductDetailInfoContent> = listOf()
) : ProductDetailInfoVisitable {

    companion object {
        const val SPECIFICATION_SIZE_THRESHOLD = 11
    }

    override fun newInstance(): ProductDetailInfoVisitable {
        return this.copy()
    }

    override fun uniqueIdentifier(): Int = componentId

    override fun setIsShowable(isShowable: Boolean) {}

    override fun isExpand(): Boolean = true

    override fun equalsWith(newData: ProductDetailInfoVisitable): Boolean {
        return if (newData is ProductDetailInfoAnnotationReadMoreDataModel) {
            componentId == newData.componentId && data.size == newData.data.size
        } else false
    }

    override fun type(typeFactory: ProductDetailInfoAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
