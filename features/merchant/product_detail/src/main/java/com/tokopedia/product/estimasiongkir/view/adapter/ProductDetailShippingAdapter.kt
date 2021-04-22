package com.tokopedia.product.estimasiongkir.view.adapter

import androidx.recyclerview.widget.AsyncDifferConfig
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by Yehezkiel on 25/01/21
 */

class ProductDetailShippingAdapter(asyncDifferConfig: AsyncDifferConfig<Visitable<*>>,
                                   typeFactory: ProductShippingFactoryImpl) : ProductBaseListAdapter<ProductShippingFactoryImpl>(asyncDifferConfig, typeFactory)