package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.topads.sdk.domain.model.CpmModel

class CpmDataView(val cpmModel: CpmModel = CpmModel()): Visitable<ProductListTypeFactory> {

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}
