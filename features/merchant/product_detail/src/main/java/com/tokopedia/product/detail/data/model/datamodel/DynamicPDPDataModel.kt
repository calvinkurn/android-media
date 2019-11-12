package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

interface DynamicPDPDataModel : Visitable<DynamicProductDetailAdapterFactory> {
    fun type(): String
}