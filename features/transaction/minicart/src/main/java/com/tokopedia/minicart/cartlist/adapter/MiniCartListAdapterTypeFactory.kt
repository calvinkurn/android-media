package com.tokopedia.minicart.cartlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.uimodel.MiniCartAccordionUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductBundleRecomShimmeringUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductBundleRecomUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartSeparatorUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartShopUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartTickerErrorUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartTickerWarningUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartUnavailableHeaderUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartUnavailableReasonUiModel
import com.tokopedia.minicart.cartlist.viewholder.MiniCartAccordionViewHolder
import com.tokopedia.minicart.cartlist.viewholder.MiniCartLoadingViewHolder
import com.tokopedia.minicart.cartlist.viewholder.MiniCartProductBundleRecomShimmeringViewHolder
import com.tokopedia.minicart.cartlist.viewholder.MiniCartProductBundleRecomViewHolder
import com.tokopedia.minicart.cartlist.viewholder.MiniCartProductViewHolder
import com.tokopedia.minicart.cartlist.viewholder.MiniCartSeparatorViewHolder
import com.tokopedia.minicart.cartlist.viewholder.MiniCartShopViewHolder
import com.tokopedia.minicart.cartlist.viewholder.MiniCartTickerErrorViewHolder
import com.tokopedia.minicart.cartlist.viewholder.MiniCartTickerWarningViewHolder
import com.tokopedia.minicart.cartlist.viewholder.MiniCartUnavailableHeaderViewHolder
import com.tokopedia.minicart.cartlist.viewholder.MiniCartUnavailableReasonViewHolder
import com.tokopedia.minicart.databinding.ItemMiniCartAccordionBinding
import com.tokopedia.minicart.databinding.ItemMiniCartLoadingBinding
import com.tokopedia.minicart.databinding.ItemMiniCartProductBinding
import com.tokopedia.minicart.databinding.ItemMiniCartProductBundleRecomBinding
import com.tokopedia.minicart.databinding.ItemMiniCartProductBundleRecomShimmeringBinding
import com.tokopedia.minicart.databinding.ItemMiniCartSeparatorBinding
import com.tokopedia.minicart.databinding.ItemMiniCartShopBinding
import com.tokopedia.minicart.databinding.ItemMiniCartTickerErrorBinding
import com.tokopedia.minicart.databinding.ItemMiniCartTickerWarningBinding
import com.tokopedia.minicart.databinding.ItemMiniCartUnavailableHeaderBinding
import com.tokopedia.minicart.databinding.ItemMiniCartUnavailableReasonBinding
import com.tokopedia.shop.common.widget.bundle.viewholder.MultipleProductBundleListener
import com.tokopedia.shop.common.widget.bundle.viewholder.SingleProductBundleListener

class MiniCartListAdapterTypeFactory(
    private val listener: MiniCartListActionListener,
    private val multipleProductBundleListener: MultipleProductBundleListener? = null,
    private val singleProductBundleListener: SingleProductBundleListener? = null
) : BaseAdapterTypeFactory(), MiniCartListTypeFactory {

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

    override fun type(uiModel: MiniCartProductBundleRecomUiModel): Int {
        return MiniCartProductBundleRecomViewHolder.LAYOUT
    }

    override fun type(uiModel: MiniCartProductBundleRecomShimmeringUiModel): Int {
        return MiniCartProductBundleRecomShimmeringViewHolder.LAYOUT
    }

    override fun type(uiModel: LoadingModel): Int {
        return MiniCartLoadingViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {
            MiniCartAccordionViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartAccordionBinding.inflate(
                    LayoutInflater.from(view.context),
                    view as ViewGroup,
                    false
                )
                MiniCartAccordionViewHolder(viewBinding, listener)
            }

            MiniCartProductViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartProductBinding.inflate(
                    LayoutInflater.from(view.context),
                    view as ViewGroup,
                    false
                )
                MiniCartProductViewHolder(viewBinding, listener)
            }

            MiniCartSeparatorViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartSeparatorBinding.inflate(
                    LayoutInflater.from(view.context),
                    view as ViewGroup,
                    false
                )
                MiniCartSeparatorViewHolder(viewBinding)
            }

            MiniCartShopViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartShopBinding.inflate(
                    LayoutInflater.from(view.context),
                    view as ViewGroup,
                    false
                )
                MiniCartShopViewHolder(viewBinding)
            }

            MiniCartTickerErrorViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartTickerErrorBinding.inflate(
                    LayoutInflater.from(view.context),
                    view as ViewGroup,
                    false
                )
                MiniCartTickerErrorViewHolder(viewBinding, listener)
            }

            MiniCartTickerWarningViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartTickerWarningBinding.inflate(
                    LayoutInflater.from(view.context),
                    view as ViewGroup,
                    false
                )
                MiniCartTickerWarningViewHolder(viewBinding)
            }

            MiniCartUnavailableHeaderViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartUnavailableHeaderBinding.inflate(
                    LayoutInflater.from(view.context),
                    view as ViewGroup,
                    false
                )
                MiniCartUnavailableHeaderViewHolder(viewBinding, listener)
            }

            MiniCartUnavailableReasonViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartUnavailableReasonBinding.inflate(
                    LayoutInflater.from(view.context),
                    view as ViewGroup,
                    false
                )
                MiniCartUnavailableReasonViewHolder(viewBinding)
            }

            MiniCartLoadingViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartLoadingBinding.inflate(
                    LayoutInflater.from(view.context),
                    view as ViewGroup,
                    false
                )
                MiniCartLoadingViewHolder(viewBinding)
            }

            MiniCartProductBundleRecomViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartProductBundleRecomBinding.inflate(
                    LayoutInflater.from(view.context),
                    view as ViewGroup,
                    false
                )
                MiniCartProductBundleRecomViewHolder(
                    viewBinding,
                    multipleProductBundleListener,
                    singleProductBundleListener
                )
            }

            MiniCartProductBundleRecomShimmeringViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartProductBundleRecomShimmeringBinding.inflate(
                    LayoutInflater.from(view.context),
                    view as ViewGroup,
                    false
                )
                MiniCartProductBundleRecomShimmeringViewHolder(viewBinding)
            }

            else -> super.createViewHolder(view, viewType)
        }
    }
}
