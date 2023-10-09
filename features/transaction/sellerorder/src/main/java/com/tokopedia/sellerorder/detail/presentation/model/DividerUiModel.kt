package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactoryImpl

data class DividerUiModel(
    val height: Int,
    val marginTop: Int = 0,
    val marginBottom: Int = 0
): Visitable<SomDetailAdapterFactoryImpl> {
    override fun type(typeFactory: SomDetailAdapterFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
