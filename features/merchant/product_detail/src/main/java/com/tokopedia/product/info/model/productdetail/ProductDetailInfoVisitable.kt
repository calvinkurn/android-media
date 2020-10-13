package com.tokopedia.product.info.model.productdetail

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactory

/**
 * Created by Yehezkiel on 12/10/20
 */
interface ProductDetailInfoVisitable : Visitable<ProductDetailInfoAdapterFactory> {
    fun equalsWith(newData: ProductDetailInfoVisitable) : Boolean
}