package com.tokopedia.tokopedianow.search.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.search.presentation.typefactory.SearchTypeFactory

class CTATokopediaNowHomeDataView: Visitable<SearchTypeFactory> {

    override fun type(typeFactory: SearchTypeFactory?) =
            typeFactory?.type(this) ?: 0
}