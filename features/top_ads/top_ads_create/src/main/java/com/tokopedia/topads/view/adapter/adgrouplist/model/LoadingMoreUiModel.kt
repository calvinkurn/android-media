package com.tokopedia.topads.view.adapter.adgrouplist.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.view.adapter.adgrouplist.typefactory.AdGroupTypeFactory

class LoadingMoreUiModel : Visitable<AdGroupTypeFactory> {
    override fun type(typeFactory: AdGroupTypeFactory) = typeFactory.type(this)

    override fun toString(): String {
        return "{}"
    }
}
