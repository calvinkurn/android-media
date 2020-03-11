package com.tokopedia.centralizedpromo.view.model

import androidx.annotation.DrawableRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.centralizedpromo.view.adapter.CentralizedPromoAdapterTypeFactory

data class RecommendedPromotionListUiModel(
        override val items: List<Visitable<*>>,
        override val errorMessage: String
): BaseUiListModel

data class RecommendedPromotionUiModel(
        @DrawableRes val imageDrawable: Int,
        val title: String,
        val description: String,
        val extra: String,
        val applink: String
): Visitable<CentralizedPromoAdapterTypeFactory> {
    override fun type(typeFactory: CentralizedPromoAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}