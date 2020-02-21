package com.tokopedia.purchase_platform.features.promo.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.*

interface PromoCheckoutTypeFactory {

    fun type(uiModel: PromoRecommendationUiModel): Int

    fun type(uiModel: PromoInputUiModel): Int

    fun type(uiModel: PromoEligibleHeaderUiModel): Int

    fun type(uiModel: PromoIneligibleHeaderUiModel): Int

    fun type(uiModel: PromoListHeaderUiModel): Int

    fun type(uiModel: PromoListItemUiModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}