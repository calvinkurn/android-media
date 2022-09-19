package com.tokopedia.centralizedpromoold.view.model

import com.tokopedia.centralizedpromo.view.model.BaseUiListItemModel
import com.tokopedia.centralizedpromo.view.model.BaseUiListModel
import com.tokopedia.centralizedpromo.view.model.Footer
import com.tokopedia.centralizedpromo.view.model.Status
import com.tokopedia.centralizedpromoold.view.adapter.CentralizedPromoAdapterTypeFactoryOld
import com.tokopedia.kotlin.model.ImpressHolder

data class OnGoingPromoListUiModelOld(
        val title: String,
        override val items: List<OnGoingPromoUiModelOld>,
        override val errorMessage: String
) : BaseUiListModel<OnGoingPromoUiModelOld>

data class OnGoingPromoUiModelOld(
        val title: String,
        val status: Status,
        val footer: Footer
) : BaseUiListItemModel<CentralizedPromoAdapterTypeFactoryOld> {
    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(typeFactory: CentralizedPromoAdapterTypeFactoryOld): Int {
        return typeFactory.type(this)
    }
}