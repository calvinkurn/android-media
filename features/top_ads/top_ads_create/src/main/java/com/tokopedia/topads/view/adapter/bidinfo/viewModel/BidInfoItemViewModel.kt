package com.tokopedia.topads.view.adapter.bidinfo.viewModel

import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.view.adapter.bidinfo.BindInfoAdapterTypeFactory

class BidInfoItemViewModel(var data: TopadsBidInfo.DataItem) : BidInfoViewModel() {

    override fun type(typesFactory: BindInfoAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}