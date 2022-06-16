package com.tokopedia.affiliate.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.bottomSheetsAdapter.AffiliateBottomSheetTypeFactory

class AffiliateBottomDividerItemModel : Visitable<AffiliateBottomSheetTypeFactory> {

    override fun type(typeFactory: AffiliateBottomSheetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
