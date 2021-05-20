package com.tokopedia.minicart.cartlist.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.*
import com.tokopedia.minicart.cartlist.viewholder.*

class MiniCartListAdapterTypeFactory(private val listener: MiniCartListActionListener)
    : BaseAdapterTypeFactory(), MiniCartListTypeFactory {

    override fun type(uiModel: MiniCartAccordionUiModel): Int {
        return MiniCartAccordionViewHolder.LAYOUT
    }

    override fun type(uiModel: MiniCartProductUiModel): Int {
        return MiniCartProductViewHolder.LAYOUT
    }

    override fun type(uiModel: MiniCartSeparatorUiModel): Int {
        return MiniCartSeparatorViewHolder.LAYOUT
    }

    override fun type(uiModel: MiniCartShopUiModel): Int {
        return MiniCartShopViewHolder.LAYOUT
    }

    override fun type(uiModel: MiniCartTickerErrorUiModel): Int {
        return MiniCartTickerErrorViewHolder.LAYOUT
    }

    override fun type(uiModel: MiniCartTickerWarningUiModel): Int {
        return MiniCartTickerWarningViewHolder.LAYOUT
    }

    override fun type(uiModel: MiniCartUnavailableHeaderUiModel): Int {
        return MiniCartUnavailableHeaderViewHolder.LAYOUT
    }

    override fun type(uiModel: MiniCartUnavailableReasonUiModel): Int {
        return MiniCartUnavailableReasonViewHolder.LAYOUT
    }

    override fun type(uiModel: LoadingModel): Int {
        return MiniCartLoadingViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {
            MiniCartAccordionViewHolder.LAYOUT -> MiniCartAccordionViewHolder(view, listener)
            MiniCartProductViewHolder.LAYOUT -> MiniCartProductViewHolder(view, listener)
            MiniCartSeparatorViewHolder.LAYOUT -> MiniCartSeparatorViewHolder(view, listener)
            MiniCartShopViewHolder.LAYOUT -> MiniCartShopViewHolder(view, listener)
            MiniCartTickerErrorViewHolder.LAYOUT -> MiniCartTickerErrorViewHolder(view, listener)
            MiniCartTickerWarningViewHolder.LAYOUT -> MiniCartTickerWarningViewHolder(view, listener)
            MiniCartUnavailableHeaderViewHolder.LAYOUT -> MiniCartUnavailableHeaderViewHolder(view, listener)
            MiniCartUnavailableReasonViewHolder.LAYOUT -> MiniCartUnavailableReasonViewHolder(view, listener)
            MiniCartLoadingViewHolder.LAYOUT -> MiniCartLoadingViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        }

    }
}