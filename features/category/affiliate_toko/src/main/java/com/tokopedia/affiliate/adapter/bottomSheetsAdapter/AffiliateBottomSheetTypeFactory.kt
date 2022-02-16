package com.tokopedia.affiliate.adapter.bottomSheetsAdapter

import com.tokopedia.affiliate.ui.viewholder.viewmodel.*

interface AffiliateBottomSheetTypeFactory {
    fun type(viewModelShared: AffiliateDateRangePickerModel): Int
    fun type(viewModelShared: AffiliateTrafficAttributionModel): Int

}
