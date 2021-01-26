package com.tokopedia.product.estimasiongkir.view.bottomsheet

import androidx.recyclerview.widget.AsyncDifferConfig
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.estimasiongkir.view.adapter.ProductBaseListAdapter
import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingFactoryImpl

/**
 * Created by Yehezkiel on 25/01/21
 */

class ProductDetailShippingAdapter(asyncDifferConfig: AsyncDifferConfig<Visitable<*>>,
                                   typeFactory: ProductShippingFactoryImpl) : ProductBaseListAdapter<ProductShippingFactoryImpl>(asyncDifferConfig, typeFactory)