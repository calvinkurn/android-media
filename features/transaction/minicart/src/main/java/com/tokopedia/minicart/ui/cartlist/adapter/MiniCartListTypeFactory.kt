package com.tokopedia.minicart.ui.cartlist.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.ui.cartlist.uimodel.*

interface MiniCartListTypeFactory {

    fun type(uiModel: MiniCartAccordionUiModel): Int

    fun type(uiModel: MiniCartProductUiModel): Int

    fun type(uiModel: MiniCartSeparatorUiModel): Int

    fun type(uiModel: MiniCartShopUiModel): Int

    fun type(uiModel: MiniCartTickerErrorUiModel): Int

    fun type(uiModel: MiniCartTickerInformationUiModel): Int

    fun type(uiModel: MiniCartUnavailableHeaderUiModel): Int

    fun type(uiModel: MiniCartUnavailableReasonUiModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}