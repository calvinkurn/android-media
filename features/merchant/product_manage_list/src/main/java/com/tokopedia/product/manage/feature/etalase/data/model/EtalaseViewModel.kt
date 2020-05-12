package com.tokopedia.product.manage.feature.etalase.data.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.etalase.view.adapter.factory.EtalaseListTypeFactory

data class EtalaseViewModel(
    val id: String,
    val name: String,
    val position: Int
): Visitable<EtalaseListTypeFactory> {

    override fun type(factory: EtalaseListTypeFactory): Int {
        return factory.type(this)
    }
}