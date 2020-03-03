package com.tokopedia.centralized_promo.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.centralized_promo.view.model.PostUiModel
import com.tokopedia.centralized_promo.view.model.OnGoingPromoUiModel
import com.tokopedia.centralized_promo.view.model.RecommendedPromotionUiModel
import com.tokopedia.centralized_promo.view.viewholder.OnGoingPromoViewHolder
import com.tokopedia.centralized_promo.view.viewholder.PostViewHolder
import com.tokopedia.centralized_promo.view.viewholder.RecommendedPromotionViewHolder

class CentralizedPromoAdapterTypeFactory : BaseAdapterTypeFactory() {
    fun type(onGoingPromoUiModel: OnGoingPromoUiModel): Int {
        return OnGoingPromoViewHolder.RES_LAYOUT
    }

    fun type(recommendedPromotionUiModel: RecommendedPromotionUiModel): Int {
        return RecommendedPromotionViewHolder.RES_LAYOUT
    }

    fun type(postUiModel: PostUiModel): Int {
        return PostViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            RecommendedPromotionViewHolder.RES_LAYOUT -> RecommendedPromotionViewHolder(parent)
            PostViewHolder.RES_LAYOUT -> PostViewHolder(parent)
            OnGoingPromoViewHolder.RES_LAYOUT -> OnGoingPromoViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}