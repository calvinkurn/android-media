package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerorder.detail.data.model.AddOnSummary
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.AddOnAdapterFactory

data class AddOnUiModel(
    val addOn: AddOnSummary.Addon,
    var descriptionExpanded: Boolean = false
) : Visitable<AddOnAdapterFactory> {
    override fun type(typeFactory: AddOnAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
