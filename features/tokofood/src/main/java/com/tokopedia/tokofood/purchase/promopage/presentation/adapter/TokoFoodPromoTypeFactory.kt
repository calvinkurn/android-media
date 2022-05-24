package com.tokopedia.tokofood.purchase.promopage.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.purchase.promopage.presentation.uimodel.TokoFoodPromoEligibilityHeaderUiModel
import com.tokopedia.tokofood.purchase.promopage.presentation.uimodel.TokoFoodPromoHeaderUiModel
import com.tokopedia.tokofood.purchase.promopage.presentation.uimodel.TokoFoodPromoItemUiModel
import com.tokopedia.tokofood.purchase.promopage.presentation.uimodel.TokoFoodPromoTickerUiModel

interface TokoFoodPromoTypeFactory {

    fun type(uiModel: TokoFoodPromoEligibilityHeaderUiModel): Int

    fun type(uiModel: TokoFoodPromoHeaderUiModel): Int

    fun type(uiModel: TokoFoodPromoItemUiModel): Int

    fun type(uiModel: TokoFoodPromoTickerUiModel): Int

    fun createViewHolder(parent: View, viewType: Int): AbstractViewHolder<*>

}