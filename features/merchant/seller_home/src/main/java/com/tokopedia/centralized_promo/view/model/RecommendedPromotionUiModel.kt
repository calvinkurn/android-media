package com.tokopedia.centralized_promo.view.model

import androidx.annotation.DrawableRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.centralized_promo.view.adapter.CentralizedPromoAdapterTypeFactory

data class RecommendedPromotionListUiModel(
        val promotions: List<RecommendedPromotionUiModel>
): BaseUiModel()

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