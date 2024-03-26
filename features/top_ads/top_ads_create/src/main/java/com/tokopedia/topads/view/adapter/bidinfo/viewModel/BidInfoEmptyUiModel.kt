package com.tokopedia.topads.view.adapter.bidinfo.viewModel

import com.tokopedia.topads.view.adapter.bidinfo.BindInfoAdapterTypeFactory

class BidInfoEmptyUiModel : BidInfoUiModel() {
    override fun type(typesFactory: BindInfoAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}
