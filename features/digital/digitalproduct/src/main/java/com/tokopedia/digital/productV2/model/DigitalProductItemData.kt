package com.tokopedia.digital.productV2.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.digital.productV2.presentation.adapter.DigitalProductAdapterFactory

/**
 * Created by resakemal on 28/11/19.
 */
class DigitalProductItemData: CatalogProduct(), Visitable<DigitalProductAdapterFactory>{
        override fun type(typeFactory: DigitalProductAdapterFactory) = typeFactory.type(this)
}