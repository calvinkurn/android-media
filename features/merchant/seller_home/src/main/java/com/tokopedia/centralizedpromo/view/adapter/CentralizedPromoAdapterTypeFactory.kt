package com.tokopedia.centralizedpromo.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.centralizedpromo.view.viewholder.OnGoingPromoViewHolder
import com.tokopedia.centralizedpromo.view.viewholder.PromoCreationViewHolder

class CentralizedPromoAdapterTypeFactory(
    private val onClickItemPromo: (PromoCreationUiModel) -> Unit,
    private val onImpressionPromo: (String) -> Unit,
) : BaseAdapterTypeFactory() {
    fun type(onGoingPromoUiModel: OnGoingPromoUiModel): Int {
        return OnGoingPromoViewHolder.RES_LAYOUT
    }

    fun type(promoCreationUiModel: PromoCreationUiModel): Int {
        return PromoCreationViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            PromoCreationViewHolder.RES_LAYOUT -> PromoCreationViewHolder(parent,onImpressionPromo).apply {
                onClickItemPromo = this@CentralizedPromoAdapterTypeFactory.onClickItemPromo
            }
            OnGoingPromoViewHolder.RES_LAYOUT -> OnGoingPromoViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}