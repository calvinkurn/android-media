package com.tokopedia.buy_more_get_more.olp.domain.entity

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.OlpAdapterTypeFactory

data class OfferProductSortingUiModel(
    var productCount: Int = 0,
    var selectedSortId: Int = 0,
    var selectedSortName: String = "Urutkan"
) : Visitable<OlpAdapterTypeFactory> {

    override fun type(typeFactory: OlpAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
