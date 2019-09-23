package com.tokopedia.autocomplete.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.adapter.SearchTypeFactory
import com.tokopedia.autocomplete.viewmodel.BaseItemAutoCompleteSearch

class ProfileSearch(
    var peopleId: String = "",
    var affiliateUserName: String = "",
    var isKOL: Boolean = false,
    var postCount: Int = 0
) : BaseItemAutoCompleteSearch(), Visitable<SearchTypeFactory> {

    override fun type(typeFactory: SearchTypeFactory): Int {
        return typeFactory.type(this)
    }
}