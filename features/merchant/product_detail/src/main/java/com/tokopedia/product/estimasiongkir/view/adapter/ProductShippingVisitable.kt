package com.tokopedia.product.estimasiongkir.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by Yehezkiel on 25/01/21
 */
interface ProductShippingVisitable : Visitable<ProductShippingFactory> {
    fun uniqueId(): Long
    fun isEqual(newData: ProductShippingVisitable): Boolean
}
