package com.tokopedia.topads.view.adapter.adgrouplist.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.view.adapter.adgrouplist.typefactory.AdGroupTypeFactory

data class ErrorUiModel(val errorType:Int) : Visitable<AdGroupTypeFactory> {
    override fun type(typeFactory: AdGroupTypeFactory) = typeFactory.type(this)
}
