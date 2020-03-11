package com.tokopedia.centralizedpromo.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.centralizedpromo.view.adapter.CentralizedPromoAdapterTypeFactory

data class OnGoingPromoListUiModel(
        val title: String,
        override val items: List<OnGoingPromoUiModel>,
        override val errorMessage: String
) : BaseUiListModel<OnGoingPromoUiModel>

data class OnGoingPromoUiModel(
        val title: String,
        val status: Status,
        val footer: Footer
) : Visitable<CentralizedPromoAdapterTypeFactory> {
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