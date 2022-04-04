package com.tokopedia.affiliate.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.dateRangePicker.AffiliateDateRangeTypeFactory

class AffiliateShimmerViewModel : Visitable<AffiliateDateRangeTypeFactory> {

    override fun type(typeFactory: AffiliateDateRangeTypeFactory): Int {
        return typeFactory.type(this)
    }
}
