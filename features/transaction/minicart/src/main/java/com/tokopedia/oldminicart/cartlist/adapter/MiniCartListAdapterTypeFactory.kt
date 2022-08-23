package com.tokopedia.oldminicart.cartlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.databinding.ItemMiniCartAccordionBinding
import com.tokopedia.minicart.databinding.ItemMiniCartLoadingBinding
import com.tokopedia.minicart.databinding.ItemMiniCartSeparatorBinding
import com.tokopedia.minicart.databinding.ItemMiniCartShopBinding
import com.tokopedia.minicart.databinding.ItemMiniCartTickerErrorBinding
import com.tokopedia.minicart.databinding.ItemMiniCartTickerWarningBinding
import com.tokopedia.minicart.databinding.ItemMiniCartUnavailableHeaderBinding
import com.tokopedia.minicart.databinding.ItemMiniCartUnavailableReasonBinding
import com.tokopedia.minicart.databinding.ItemOldMiniCartProductBinding
import com.tokopedia.oldminicart.cartlist.MiniCartListActionListener
import com.tokopedia.oldminicart.cartlist.uimodel.MiniCartAccordionUiModel
import com.tokopedia.oldminicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.oldminicart.cartlist.uimodel.MiniCartSeparatorUiModel
import com.tokopedia.oldminicart.cartlist.uimodel.MiniCartShopUiModel
import com.tokopedia.oldminicart.cartlist.uimodel.MiniCartTickerErrorUiModel
import com.tokopedia.oldminicart.cartlist.uimodel.MiniCartTickerWarningUiModel
import com.tokopedia.oldminicart.cartlist.uimodel.MiniCartUnavailableHeaderUiModel
import com.tokopedia.oldminicart.cartlist.uimodel.MiniCartUnavailableReasonUiModel
import com.tokopedia.oldminicart.cartlist.viewholder.MiniCartAccordionViewHolder
import com.tokopedia.oldminicart.cartlist.viewholder.MiniCartLoadingViewHolder
import com.tokopedia.oldminicart.cartlist.viewholder.MiniCartProductViewHolder
import com.tokopedia.oldminicart.cartlist.viewholder.MiniCartSeparatorViewHolder
import com.tokopedia.oldminicart.cartlist.viewholder.MiniCartShopViewHolder
import com.tokopedia.oldminicart.cartlist.viewholder.MiniCartTickerErrorViewHolder
import com.tokopedia.oldminicart.cartlist.viewholder.MiniCartTickerWarningViewHolder
import com.tokopedia.oldminicart.cartlist.viewholder.MiniCartUnavailableHeaderViewHolder
import com.tokopedia.oldminicart.cartlist.viewholder.MiniCartUnavailableReasonViewHolder

class MiniCartListAdapterTypeFactory(private val listener: MiniCartListActionListener) :
    BaseAdapterTypeFactory(), MiniCartListTypeFactory {

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

    override fun createViewHolder(parent: View, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {
            MiniCartAccordionViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartAccordionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                MiniCartAccordionViewHolder(viewBinding, listener)
            }
            MiniCartProductViewHolder.LAYOUT -> {
                val viewBinding = ItemOldMiniCartProductBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                MiniCartProductViewHolder(viewBinding, listener)
            }
            MiniCartSeparatorViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartSeparatorBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                MiniCartSeparatorViewHolder(viewBinding)
            }
            MiniCartShopViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartShopBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                MiniCartShopViewHolder(viewBinding)
            }
            MiniCartTickerErrorViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartTickerErrorBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                MiniCartTickerErrorViewHolder(viewBinding, listener)
            }
            MiniCartTickerWarningViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartTickerWarningBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                MiniCartTickerWarningViewHolder(viewBinding)
            }
            MiniCartUnavailableHeaderViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartUnavailableHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                MiniCartUnavailableHeaderViewHolder(viewBinding, listener)
            }
            MiniCartUnavailableReasonViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartUnavailableReasonBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                MiniCartUnavailableReasonViewHolder(viewBinding)
            }
            MiniCartLoadingViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                )
                MiniCartLoadingViewHolder(viewBinding)
            }
            else -> super.createViewHolder(parent, viewType)
        }

    }
}