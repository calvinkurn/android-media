package com.tokopedia.affiliate.adapter.bottomSheetsAdapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.affiliate.interfaces.AffiliateDatePickerInterface
import com.tokopedia.affiliate.ui.viewholder.*
import com.tokopedia.affiliate.ui.viewholder.viewmodel.*

class AffiliateBottomSheetAdapterFactory(
    private var dateClickedInterface: AffiliateDatePickerInterface? = null,
) : BaseAdapterTypeFactory(), AffiliateBottomSheetTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            AffiliateDatePickerItemVH.LAYOUT -> AffiliateDatePickerItemVH(parent,dateClickedInterface)
            AffiliateTrafficAttributionItemVH.LAYOUT -> AffiliateTrafficAttributionItemVH(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(viewModelShared: AffiliateDateRangePickerModel): Int {
        return AffiliateDatePickerItemVH.LAYOUT
    }

    override fun type(viewModelShared: AffiliateTrafficAttributionModel): Int {
       return AffiliateTrafficAttributionItemVH.LAYOUT
    }
}
