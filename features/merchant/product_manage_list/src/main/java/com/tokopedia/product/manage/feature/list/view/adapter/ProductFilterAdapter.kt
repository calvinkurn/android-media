package com.tokopedia.product.manage.feature.list.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductFilterAdapterFactoryImpl
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.FilterTabViewHolder.*

class ProductFilterAdapter(
    listener: ProductFilterListener
) : BaseAdapter<ProductFilterAdapterFactoryImpl>(ProductFilterAdapterFactoryImpl(listener))