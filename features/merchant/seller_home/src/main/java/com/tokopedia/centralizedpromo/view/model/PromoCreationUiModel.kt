package com.tokopedia.centralizedpromo.view.model

import androidx.annotation.DrawableRes
import com.tokopedia.centralizedpromo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

data class PromoCreationListUiModel(
        override val items: List<PromoCreationUiModel>,
        override val errorMessage: String
): BaseUiListModel<PromoCreationUiModel>

data class PromoCreationUiModel(
        @DrawableRes val imageDrawable: Int,
        val title: String,
        val description: String,
        val extra: String,
        val applink: String
): BaseUiListItemModel<CentralizedPromoAdapterTypeFactory> {
    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(typeFactory: CentralizedPromoAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}