package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactory

data class DividerUiModel(
    val height: Int,
    val marginTop: Int = 0,
    val marginBottom: Int = 0
): Visitable<SomDetailAdapterFactory> {
    override fun type(typeFactory: SomDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
