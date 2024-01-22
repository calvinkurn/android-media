package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofDescriptionUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofErrorStateUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofFullyFulfilledProductListHeaderUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofLoadingUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofPriceBreakdownTotalUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofPriceBreakdownUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofProductEditableUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofProductListHeaderUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofProductStaticUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofSummaryDescriptionUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofThickDividerUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofThinDividerUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofTickerUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder.PofDescriptionViewHolder
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder.PofErrorStateViewHolder
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder.PofFullyFulfilledProductListHeaderViewHolder
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder.PofLoadingViewHolder
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder.PofPriceBreakdownTotalViewHolder
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder.PofPriceBreakdownViewHolder
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder.PofProductEditableViewHolder
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder.PofProductListHeaderViewHolder
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder.PofProductStaticViewHolder
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder.PofSummaryDescriptionViewHolder
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder.PofThickDividerViewHolder
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder.PofThinDividerViewHolder
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder.PofTickerViewHolder

class PofAdapterTypeFactoryImpl(
    private val listener: PofAdapterTypeFactory.Listener
) : PofAdapterTypeFactory, BaseAdapterTypeFactory() {
    override fun type(pofLoadingUiModel: PofLoadingUiModel): Int {
        return PofLoadingViewHolder.LAYOUT
    }

    override fun type(pofProductEditableUiModel: PofProductEditableUiModel): Int {
        return PofProductEditableViewHolder.LAYOUT
    }

    override fun type(pofThinDividerUiModel: PofThinDividerUiModel): Int {
        return PofThinDividerViewHolder.LAYOUT
    }

    override fun type(pofThickDividerUiModel: PofThickDividerUiModel): Int {
        return PofThickDividerViewHolder.LAYOUT
    }

    override fun type(pofTickerUiModel: PofTickerUiModel): Int {
        return PofTickerViewHolder.LAYOUT
    }

    override fun type(pofDescriptionUiModel: PofDescriptionUiModel): Int {
        return PofDescriptionViewHolder.LAYOUT
    }

    override fun type(pofProductListHeaderUiModel: PofProductListHeaderUiModel): Int {
        return PofProductListHeaderViewHolder.LAYOUT
    }

    override fun type(pofErrorStateUiModel: PofErrorStateUiModel): Int {
        return PofErrorStateViewHolder.LAYOUT
    }

    override fun type(pofPriceBreakdownUiModel: PofPriceBreakdownUiModel): Int {
        return PofPriceBreakdownViewHolder.LAYOUT
    }

    override fun type(pofPriceBreakdownTotalUiModel: PofPriceBreakdownTotalUiModel): Int {
        return PofPriceBreakdownTotalViewHolder.LAYOUT
    }

    override fun type(pofSummaryDescriptionUiModel: PofSummaryDescriptionUiModel): Int {
        return PofSummaryDescriptionViewHolder.LAYOUT
    }

    override fun type(pofProductStaticUiModel: PofProductStaticUiModel): Int {
        return PofProductStaticViewHolder.LAYOUT
    }

    override fun type(pofFullyFulfilledProductListHeaderUiModel: PofFullyFulfilledProductListHeaderUiModel): Int {
        return PofFullyFulfilledProductListHeaderViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            PofLoadingViewHolder.LAYOUT -> PofLoadingViewHolder(parent)
            PofProductEditableViewHolder.LAYOUT -> PofProductEditableViewHolder(parent, listener)
            PofThinDividerViewHolder.LAYOUT -> PofThinDividerViewHolder(parent)
            PofThickDividerViewHolder.LAYOUT -> PofThickDividerViewHolder(parent)
            PofTickerViewHolder.LAYOUT -> PofTickerViewHolder(parent)
            PofDescriptionViewHolder.LAYOUT -> PofDescriptionViewHolder(parent, listener)
            PofProductListHeaderViewHolder.LAYOUT -> PofProductListHeaderViewHolder(parent)
            PofErrorStateViewHolder.LAYOUT -> PofErrorStateViewHolder(parent, listener)
            PofPriceBreakdownViewHolder.LAYOUT -> PofPriceBreakdownViewHolder(parent)
            PofPriceBreakdownTotalViewHolder.LAYOUT -> PofPriceBreakdownTotalViewHolder(parent)
            PofSummaryDescriptionViewHolder.LAYOUT -> PofSummaryDescriptionViewHolder(parent)
            PofProductStaticViewHolder.LAYOUT -> PofProductStaticViewHolder(parent)
            PofFullyFulfilledProductListHeaderViewHolder.LAYOUT -> PofFullyFulfilledProductListHeaderViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
