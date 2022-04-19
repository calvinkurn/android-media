package com.tokopedia.product.estimasiongkir.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by Yehezkiel on 25/01/21
 */
class ProductDetailShippingDIffutil : DiffUtil.ItemCallback<Visitable<*>>() {
    override fun areItemsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return (oldItem as ProductShippingVisitable).uniqueId() == (newItem as ProductShippingVisitable).uniqueId()
    }

    override fun areContentsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return (oldItem as ProductShippingVisitable).isEqual(newItem as ProductShippingVisitable)
    }
}