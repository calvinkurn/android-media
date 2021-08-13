package com.tokopedia.topads.view.adapter.bidinfo.viewModel

import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.view.adapter.bidinfo.BindInfoAdapterTypeFactory

class BidInfoItemViewModel(var data: KeywordDataItem) : BidInfoViewModel() {

    override fun type(typesFactory: BindInfoAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}