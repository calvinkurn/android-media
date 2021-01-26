package com.tokopedia.product.estimasiongkir.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingHeaderDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingServiceDataModel
import com.tokopedia.product.estimasiongkir.view.adapter.viewholder.ProductShippingHeaderViewHolder
import com.tokopedia.product.estimasiongkir.view.adapter.viewholder.ProductShippingServiceViewHolder

/**
 * Created by Yehezkiel on 25/01/21
 */
class ProductShippingFactoryImpl : BaseAdapterTypeFactory(), ProductShippingFactory {
    override fun type(dataProduct: ProductShippingHeaderDataModel): Int {
        return ProductShippingHeaderViewHolder.LAYOUT
    }

    override fun type(dataProduct: ProductShippingServiceDataModel): Int {
        return ProductShippingServiceViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ProductShippingHeaderViewHolder.LAYOUT -> ProductShippingHeaderViewHolder(view)
            ProductShippingServiceViewHolder.LAYOUT -> ProductShippingServiceViewHolder(view)
            else -> return super.createViewHolder(view, type)
        }
    }
}