package com.tokopedia.product.info.model.productdetail.uidata

import com.tokopedia.product.detail.common.data.model.product.Video
import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactory

/**
 * Created by Yehezkiel on 13/10/20
 */
data class ProductDetailInfoExpandableDataModel(
        var componentName: Int = 0,
        var textValue: String = "",
        var video: List<Video> = listOf(),
        var isShowable: Boolean = false
) : ProductDetailInfoVisitable {

    override fun newInstance(): ProductDetailInfoVisitable {
        return this.copy()
    }

    override fun uniqueIdentifier(): Int = componentName

    override fun equalsWith(newData: ProductDetailInfoVisitable): Boolean {
        return if (newData is ProductDetailInfoExpandableDataModel) {
            textValue == newData.textValue
        } else false
    }

    override fun setIsShowable(isShowable: Boolean) {
        this.isShowable = isShowable
    }

    override fun isExpand(): Boolean {
        return isShowable
    }

    override fun type(typeFactory: ProductDetailInfoAdapterFactory): Int {
        return typeFactory.type(this)
    }
}