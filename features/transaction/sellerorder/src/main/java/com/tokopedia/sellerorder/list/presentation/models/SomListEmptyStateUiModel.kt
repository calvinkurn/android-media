package com.tokopedia.sellerorder.list.presentation.models

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListAdapterTypeFactory

data class SomListEmptyStateUiModel(
        var imageUrl: String = "",
        var title: String = "",
        var description: String = "",
        var buttonText: String = "",
        var buttonAppLink: String = "",
        var showButton: Boolean = false
): Visitable<SomListAdapterTypeFactory> {
    override fun type(typeFactory: SomListAdapterTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }
}