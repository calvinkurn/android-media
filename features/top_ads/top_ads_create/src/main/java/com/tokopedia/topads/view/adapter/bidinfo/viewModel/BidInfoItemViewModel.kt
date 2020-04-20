package com.tokopedia.topads.view.adapter.bidinfo.viewModel

import com.tokopedia.topads.data.response.ResponseBidInfo
import com.tokopedia.topads.view.adapter.bidinfo.BindInfoAdapterTypeFactory

class BidInfoItemViewModel(var data: ResponseBidInfo.Result.TopadsBidInfo.DataItem) : BidInfoViewModel() {

    var isError: Boolean = false
    override fun type(typesFactory: BindInfoAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}