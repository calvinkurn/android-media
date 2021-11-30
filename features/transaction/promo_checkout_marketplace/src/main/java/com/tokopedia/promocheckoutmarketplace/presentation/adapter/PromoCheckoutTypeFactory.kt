package com.tokopedia.promocheckoutmarketplace.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.*

interface PromoCheckoutTypeFactory {

    fun type(uiModel: PromoRecommendationUiModel): Int

    fun type(uiModel: PromoInputUiModel): Int

    fun type(uiModel: PromoEligibilityHeaderUiModel): Int

    fun type(uiModel: PromoListHeaderUiModel): Int

    fun type(uiModel: PromoListItemUiModel): Int

    fun type(uiModel: PromoEmptyStateUiModel): Int

    fun type(uiModel: PromoErrorStateUiModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}