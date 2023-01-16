package com.tokopedia.buyerorderdetail.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.PartialOrderFulfillmentListener
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofAvailableLabelViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofDetailViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofFulfilledToggleViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofHeaderInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofProductFulfilledViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofProductUnfulfilledViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofRefundEstimateViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofThickDividerViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofThinDividerViewHolder
import com.tokopedia.buyerorderdetail.presentation.model.PofAvailableLabelUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofDetailRefundedUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofDetailUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofFulfilledToggleUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofHeaderInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofProductFulfilledUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofProductUnfulfilledUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofRefundEstimateUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofThinDividerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ThickDividerUiModel

class PartialOrderFulfillmentTypeFactoryImpl(
    private val partialOrderFulfillmentListener: PartialOrderFulfillmentListener
) : BaseAdapterTypeFactory(),
    PartialOrderFulfillmentTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            PofAvailableLabelViewHolder.LAYOUT -> PofAvailableLabelViewHolder(parent)
            PofDetailViewHolder.LAYOUT -> PofDetailViewHolder(parent)
            PofFulfilledToggleViewHolder.LAYOUT -> PofFulfilledToggleViewHolder(parent, partialOrderFulfillmentListener)
            PofHeaderInfoViewHolder.LAYOUT -> PofHeaderInfoViewHolder(parent)
            PofProductFulfilledViewHolder.LAYOUT -> PofProductFulfilledViewHolder(parent)
            PofProductUnfulfilledViewHolder.LAYOUT -> PofProductUnfulfilledViewHolder(parent)
            PofRefundEstimateViewHolder.LAYOUT -> PofRefundEstimateViewHolder(parent, partialOrderFulfillmentListener)
            PofThickDividerViewHolder.LAYOUT -> PofThickDividerViewHolder(parent)
            PofThinDividerViewHolder.LAYOUT -> PofThinDividerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(pofAvailableLabelUiModel: PofAvailableLabelUiModel): Int {
        return PofAvailableLabelViewHolder.LAYOUT
    }

    override fun type(pofDetailUiModel: PofDetailUiModel): Int {
        return PofDetailViewHolder.LAYOUT
    }

    override fun type(pofFulfilledToggleUiModel: PofFulfilledToggleUiModel): Int {
        return PofFulfilledToggleViewHolder.LAYOUT
    }

    override fun type(pofHeaderInfoUiModel: PofHeaderInfoUiModel): Int {
        return PofHeaderInfoViewHolder.LAYOUT
    }

    override fun type(pofProductFulfilledUiModel: PofProductFulfilledUiModel): Int {
        return PofProductFulfilledViewHolder.LAYOUT
    }

    override fun type(pofProductUnfulfilledUiModel: PofProductUnfulfilledUiModel): Int {
        return PofProductUnfulfilledViewHolder.LAYOUT
    }

    override fun type(pofRefundEstimateUiModel: PofRefundEstimateUiModel): Int {
        return PofRefundEstimateViewHolder.LAYOUT
    }

    override fun type(pofThickDividerUiModel: ThickDividerUiModel): Int {
        return PofThickDividerViewHolder.LAYOUT
    }

    override fun type(pofThinDividerUiModel: PofThinDividerUiModel): Int {
        return PofThinDividerViewHolder.LAYOUT
    }

}
