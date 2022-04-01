package com.tokopedia.affiliate.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.dateRangePicker.AffiliateDateRangeTypeFactory
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData

class AffiliateDateRangePickerModel(val dateRange: AffiliateDatePickerData) : Visitable<AffiliateDateRangeTypeFactory> {

    override fun type(typeFactory: AffiliateDateRangeTypeFactory): Int {
        return typeFactory.type(this)
    }
}
