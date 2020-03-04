package com.tokopedia.centralized_promo.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.centralized_promo.view.adapter.CentralizedPromoAdapterTypeFactory

data class OnGoingPromoListUiModel(
        val title: String,
        val promotions: ArrayList<OnGoingPromoUiModel>,
        val errorMessage: String
): BaseUiModel()

data class OnGoingPromoUiModel(
        val title: String,
        val status: Status,
        val footer: Footer
): Visitable<CentralizedPromoAdapterTypeFactory> {
    override fun type(typeFactory: CentralizedPromoAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class Status(
        val text: String,
        val count: Int,
        val url: String
)

data class Footer(
        val text: String,
        val url: String
)