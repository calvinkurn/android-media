package com.tokopedia.topads.view.adapter.bidinfo.viewModel

import com.tokopedia.topads.view.adapter.bidinfo.BindInfoAdapterTypeFactory

abstract class BidInfoViewModel {
    abstract fun type(typesFactory: BindInfoAdapterTypeFactory): Int
}