package com.tokopedia.product.manage.feature.list.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductMenuAdapterFactoryImpl
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductMenuViewHolder.ProductMenuListener

class ProductMenuAdapter(
    listener: ProductMenuListener
): BaseAdapter<ProductMenuAdapterFactoryImpl>(ProductMenuAdapterFactoryImpl(listener))