package com.tokopedia.product.info.model.productdetail.uidata

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactory

/**
 * Created by Yehezkiel on 12/10/20
 */
interface ProductDetailInfoVisitable : Visitable<ProductDetailInfoAdapterFactory> {
    fun uniqueIdentifier(): Int
    fun newInstance(): ProductDetailInfoVisitable
    fun equalsWith(newData: ProductDetailInfoVisitable): Boolean
    fun setIsShowable(isShowable: Boolean)
    fun isExpand(): Boolean
}