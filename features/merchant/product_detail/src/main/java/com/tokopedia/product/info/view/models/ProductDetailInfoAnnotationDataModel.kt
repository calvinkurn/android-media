package com.tokopedia.product.info.view.models

import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoContent
import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactory

/**
 * Created by Yehezkiel on 12/10/20
 */
data class ProductDetailInfoAnnotationDataModel(
    val componentId: Int = 0,
    val data: ProductDetailInfoContent = ProductDetailInfoContent()
) : ProductDetailInfoVisitable {

    override fun newInstance(): ProductDetailInfoVisitable {
        return this.copy()
    }

    override fun uniqueIdentifier(): Int = componentId

    override fun setIsShowable(isShowable: Boolean) {}

    override fun isExpand(): Boolean = true

    override fun equalsWith(newData: ProductDetailInfoVisitable): Boolean {
        return if (newData is ProductDetailInfoAnnotationDataModel) {
            data.title == newData.data.title
                && data.subtitle == newData.data.subtitle
                && data.showAtBottomSheet == newData.data.showAtBottomSheet
        } else false
    }

    override fun type(typeFactory: ProductDetailInfoAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
