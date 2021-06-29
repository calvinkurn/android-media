package com.tokopedia.minicart.cartlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.*
import com.tokopedia.minicart.cartlist.viewholder.*
import com.tokopedia.minicart.databinding.*

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
            MiniCartAccordionViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartAccordionBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                MiniCartAccordionViewHolder(viewBinding, listener)
            }
            MiniCartProductViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartProductBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                MiniCartProductViewHolder(viewBinding, listener)
            }
            MiniCartSeparatorViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartSeparatorBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                MiniCartSeparatorViewHolder(viewBinding)
            }
            MiniCartShopViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartShopBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                MiniCartShopViewHolder(viewBinding)
            }
            MiniCartTickerErrorViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartTickerErrorBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                MiniCartTickerErrorViewHolder(viewBinding, listener)
            }
            MiniCartTickerWarningViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartTickerWarningBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                MiniCartTickerWarningViewHolder(viewBinding)
            }
            MiniCartUnavailableHeaderViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartUnavailableHeaderBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                MiniCartUnavailableHeaderViewHolder(viewBinding, listener)
            }
            MiniCartUnavailableReasonViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartUnavailableReasonBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                MiniCartUnavailableReasonViewHolder(viewBinding)
            }
            MiniCartLoadingViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartLoadingBinding.inflate(LayoutInflater.from(view.context), view as ViewGroup, false)
                MiniCartLoadingViewHolder(viewBinding)
            }
            else -> super.createViewHolder(view, viewType)
        }

    }
}