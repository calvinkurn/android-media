package com.tokopedia.minicart.common.widget.shoppingsummary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryHeaderUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryProductUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummarySeparatorUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.uimodel.ShoppingSummaryTotalTransactionUiModel
import com.tokopedia.minicart.common.widget.shoppingsummary.viewholder.ShoppingSummaryHeaderViewHolder
import com.tokopedia.minicart.common.widget.shoppingsummary.viewholder.ShoppingSummaryProductViewHolder
import com.tokopedia.minicart.common.widget.shoppingsummary.viewholder.ShoppingSummarySeparatorViewHolder
import com.tokopedia.minicart.common.widget.shoppingsummary.viewholder.ShoppingSummaryTotalTransactionViewHolder
import com.tokopedia.minicart.databinding.ItemShoppingSummaryHeaderBinding
import com.tokopedia.minicart.databinding.ItemShoppingSummaryProductBinding
import com.tokopedia.minicart.databinding.ItemShoppingSummarySeparatorBinding
import com.tokopedia.minicart.databinding.ItemShoppingSummaryTotalTransactionBinding

class ShoppingSummaryAdapterTypeFactory : BaseAdapterTypeFactory(), ShoppingSummaryTypeFactory {

    override fun type(uiModel: ShoppingSummaryHeaderUiModel): Int {
        return ShoppingSummaryHeaderViewHolder.LAYOUT
    }

    override fun type(uiModel: ShoppingSummaryProductUiModel): Int {
        return ShoppingSummaryProductViewHolder.LAYOUT
    }

    override fun type(uiModel: ShoppingSummarySeparatorUiModel): Int {
        return ShoppingSummarySeparatorViewHolder.LAYOUT
    }

    override fun type(uiModel: ShoppingSummaryTotalTransactionUiModel): Int {
        return ShoppingSummaryTotalTransactionViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {
            ShoppingSummaryHeaderViewHolder.LAYOUT -> {
                val viewBinding = ItemShoppingSummaryHeaderBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                ShoppingSummaryHeaderViewHolder(viewBinding)
            }
            ShoppingSummaryProductViewHolder.LAYOUT -> {
                val viewBinding = ItemShoppingSummaryProductBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                ShoppingSummaryProductViewHolder(viewBinding)
            }
            ShoppingSummarySeparatorViewHolder.LAYOUT -> {
                val viewBinding = ItemShoppingSummarySeparatorBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                ShoppingSummarySeparatorViewHolder(viewBinding)
            }
            ShoppingSummaryTotalTransactionViewHolder.LAYOUT -> {
                val viewBinding = ItemShoppingSummaryTotalTransactionBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                ShoppingSummaryTotalTransactionViewHolder(viewBinding)
            }
            else -> super.createViewHolder(parent, viewType)
        }
    }
}
