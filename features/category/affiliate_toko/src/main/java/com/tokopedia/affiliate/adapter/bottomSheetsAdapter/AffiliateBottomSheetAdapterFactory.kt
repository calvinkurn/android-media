package com.tokopedia.affiliate.adapter.bottomSheetsAdapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.AffiliateDatePickerInterface
import com.tokopedia.affiliate.ui.viewholder.AffiliateBottomSheetDivderItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateDatePickerItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateDatePickerShimmerItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateTrafficAttributionItemVH
import com.tokopedia.affiliate.ui.viewholder.AffiliateWithdrawalInfoItemVH
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateBottomDividerItemModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDateRangePickerModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShimmerViewModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTrafficAttributionModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateWithrawalInfoAttributionModel

class AffiliateBottomSheetAdapterFactory(
    private var dateClickedInterface: AffiliateDatePickerInterface? = null,
) : BaseAdapterTypeFactory(), AffiliateBottomSheetTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            AffiliateDatePickerItemVH.LAYOUT -> AffiliateDatePickerItemVH(parent,dateClickedInterface)
            AffiliateTrafficAttributionItemVH.LAYOUT -> AffiliateTrafficAttributionItemVH(parent)
            AffiliateWithdrawalInfoItemVH.LAYOUT -> AffiliateWithdrawalInfoItemVH(parent)
            AffiliateBottomSheetDivderItemVH.LAYOUT -> AffiliateBottomSheetDivderItemVH(parent)
            AffiliateDatePickerShimmerItemVH.LAYOUT -> AffiliateDatePickerShimmerItemVH(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(viewModelShared: AffiliateDateRangePickerModel): Int {
        return AffiliateDatePickerItemVH.LAYOUT
    }

    override fun type(viewModelShared: AffiliateTrafficAttributionModel): Int {
       return AffiliateTrafficAttributionItemVH.LAYOUT
    }

    override fun type(viewModelShared: AffiliateWithrawalInfoAttributionModel): Int {
        return AffiliateWithdrawalInfoItemVH.LAYOUT
    }

    override fun type(viewModel: AffiliateBottomDividerItemModel): Int {
        return AffiliateBottomSheetDivderItemVH.LAYOUT
    }

    override fun type(viewModelShared: AffiliateShimmerViewModel): Int {
        return AffiliateDatePickerShimmerItemVH.LAYOUT
    }
}
