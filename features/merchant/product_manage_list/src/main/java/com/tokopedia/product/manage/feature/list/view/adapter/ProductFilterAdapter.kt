package com.tokopedia.product.manage.feature.list.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductFilterAdapterFactoryImpl
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.FilterTabViewHolder.*
import com.tokopedia.product.manage.feature.list.view.model.FilterTabViewModel

class ProductFilterAdapter(
    listener: ProductFilterListener
) : BaseListAdapter<FilterTabViewModel, ProductFilterAdapterFactoryImpl>(ProductFilterAdapterFactoryImpl(listener))