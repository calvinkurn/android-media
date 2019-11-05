package com.tokopedia.product.detail.data.model.dynamicpdp

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class DynamicProductDetailDataModel(
        val type: String = ""
): Visitable<DynamicProductDetailAdapterFactory> {
    override fun type(typeFactory: DynamicProductDetailAdapterFactory?): Int {
        return 1
    }
}