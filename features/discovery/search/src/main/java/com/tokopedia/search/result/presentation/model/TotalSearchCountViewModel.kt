package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProfileListTypeFactory

class TotalSearchCountViewModel(var searchCountText: String? = "") : Visitable<ProfileListTypeFactory> {

    override fun type(typeFactory: ProfileListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
