package com.tokopedia.product.manage.feature.quickedit.variant.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory

class ProductVariantAdapter(
    adapterFactory: BaseAdapterTypeFactory
): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(adapterFactory)