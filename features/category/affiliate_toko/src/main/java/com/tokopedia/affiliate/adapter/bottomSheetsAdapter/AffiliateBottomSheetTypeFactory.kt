package com.tokopedia.affiliate.adapter.bottomSheetsAdapter

import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateBottomDividerItemModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDateRangePickerModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShimmerViewModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTrafficAttributionModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateWithrawalInfoAttributionModel


interface AffiliateBottomSheetTypeFactory {
    fun type(viewModelShared: AffiliateDateRangePickerModel): Int
    fun type(viewModelShared: AffiliateTrafficAttributionModel): Int
    fun type(viewModelShared: AffiliateWithrawalInfoAttributionModel): Int
    fun type(viewModel: AffiliateBottomDividerItemModel): Int
    fun type(viewModelShared: AffiliateShimmerViewModel): Int

}
