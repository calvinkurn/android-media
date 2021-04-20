package com.tokopedia.product.info.model.productdetail.uidata

import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactory

/**
 * Created by Yehezkiel on 16/10/20
 */
data class ProductDetailInfoDiscussionDataModel(
        var componentName: Int = 0,
        var title: String = "",
        var discussionCount :Int = 0,
        var isShowable: Boolean = false
) : ProductDetailInfoVisitable {
    override fun uniqueIdentifier(): Int = componentName

    override fun newInstance(): ProductDetailInfoVisitable = this.copy()

    override fun equalsWith(newData: ProductDetailInfoVisitable): Boolean {
        return if (newData is ProductDetailInfoDiscussionDataModel) {
            title == newData.title
        } else false
    }

    override fun setIsShowable(isShowable: Boolean) {}

    override fun isExpand(): Boolean = false

    override fun type(typeFactory: ProductDetailInfoAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
