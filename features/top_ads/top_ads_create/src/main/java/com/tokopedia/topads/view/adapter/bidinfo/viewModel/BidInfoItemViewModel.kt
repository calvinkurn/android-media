package com.tokopedia.topads.view.adapter.bidinfo.viewModel

import com.tokopedia.topads.data.response.BidInfoDataItem
import com.tokopedia.topads.view.adapter.bidinfo.BindInfoAdapterTypeFactory

class BidInfoItemViewModel(var data: BidInfoDataItem) : BidInfoViewModel() {

    override fun type(typesFactory: BindInfoAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}