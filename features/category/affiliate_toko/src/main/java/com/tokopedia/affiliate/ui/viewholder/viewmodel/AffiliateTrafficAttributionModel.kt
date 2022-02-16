package com.tokopedia.affiliate.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.bottomSheetsAdapter.AffiliateBottomSheetTypeFactory
import com.tokopedia.affiliate.model.pojo.AffiliateDatePickerData

class AffiliateTrafficAttributionModel(val dateRange: AffiliateDatePickerData) : Visitable<AffiliateBottomSheetTypeFactory> {

    override fun type(typeFactory: AffiliateBottomSheetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
