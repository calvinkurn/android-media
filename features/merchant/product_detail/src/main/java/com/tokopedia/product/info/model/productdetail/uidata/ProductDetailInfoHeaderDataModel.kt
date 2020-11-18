package com.tokopedia.product.info.model.productdetail.uidata

import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoContent
import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactory

/**
 * Created by Yehezkiel on 12/10/20
 */
data class ProductDetailInfoHeaderDataModel(
        var componentId: Int = 0,
        var img: String = "",
        var productTitle: String = "",
        var listOfInfo: List<ProductDetailInfoContent> = listOf(),
        var listOfAnnotation: List<ProductDetailInfoContent> = listOf(),
        var isShowable: Boolean = false
) : ProductDetailInfoVisitable {

    fun needToShowSpecification(): Boolean = (listOfInfo.size + listOfAnnotation.size) > 8

    override fun newInstance(): ProductDetailInfoVisitable {
        return this.copy()
    }

    override fun uniqueIdentifier(): Int = componentId

    override fun setIsShowable(isShowable: Boolean) {}

    override fun isExpand(): Boolean {
        return isShowable
    }

    override fun equalsWith(newData: ProductDetailInfoVisitable): Boolean {
        return if (newData is ProductDetailInfoHeaderDataModel) {
            img == newData.img && productTitle == newData.productTitle
        } else false
    }

    override fun type(typeFactory: ProductDetailInfoAdapterFactory): Int {
        return typeFactory.type(this)
    }
}