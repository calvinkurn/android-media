package com.tokopedia.centralizedpromoold.view.model

import androidx.annotation.DrawableRes
import com.tokopedia.centralizedpromo.view.model.BaseUiListItemModel
import com.tokopedia.centralizedpromo.view.model.BaseUiListModel
import com.tokopedia.centralizedpromoold.view.adapter.CentralizedPromoAdapterTypeFactoryOld
import com.tokopedia.kotlin.model.ImpressHolder

data class PromoCreationListUiModelOld(
        override val items: List<PromoCreationUiModelOld>,
        override val errorMessage: String
): BaseUiListModel<PromoCreationUiModelOld>

data class PromoCreationUiModelOld(
        @DrawableRes val imageDrawable: Int,
        val title: String,
        val description: String,
        val extra: String,
        val applink: String,
        val tagLabel: String = ""
): BaseUiListItemModel<CentralizedPromoAdapterTypeFactoryOld> {
    override val impressHolder: ImpressHolder = ImpressHolder()

    override fun type(typeFactory: CentralizedPromoAdapterTypeFactoryOld): Int {
        return typeFactory.type(this)
    }
}