package com.tokopedia.affiliate.adapter.dateRangePicker

import com.tokopedia.affiliate.ui.viewholder.viewmodel.*

interface AffiliateDateRangeTypeFactory {
    fun type(viewModelShared: AffiliateDateRangePickerModel): Int
}
