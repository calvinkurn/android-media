package com.tokopedia.oldminicart.cartlist.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.cartlist.uimodel.MiniCartAccordionUiModel
import com.tokopedia.oldminicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartSeparatorUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartShopUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartTickerErrorUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartTickerWarningUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartUnavailableHeaderUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartUnavailableReasonUiModel

interface MiniCartListTypeFactory {

    fun type(uiModel: MiniCartAccordionUiModel): Int

    fun type(uiModel: MiniCartProductUiModel): Int

    fun type(uiModel: MiniCartSeparatorUiModel): Int

    fun type(uiModel: MiniCartShopUiModel): Int

    fun type(uiModel: MiniCartTickerErrorUiModel): Int

    fun type(uiModel: MiniCartTickerWarningUiModel): Int

    fun type(uiModel: MiniCartUnavailableHeaderUiModel): Int

    fun type(uiModel: MiniCartUnavailableReasonUiModel): Int

    fun createViewHolder(parent: View, viewType: Int): AbstractViewHolder<*>

}