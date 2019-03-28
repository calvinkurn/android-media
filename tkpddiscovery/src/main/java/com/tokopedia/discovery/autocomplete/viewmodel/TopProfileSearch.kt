package com.tokopedia.discovery.autocomplete.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.search.view.adapter.factory.SearchTypeFactory

class TopProfileSearch(
    var peopleId: String = "",
    var affiliateUserName: String = "",
    var isKOL: Boolean = false,
    var postCount: Int = 0
) : BaseItemAutoCompleteSearch(), Visitable<SearchTypeFactory> {

    override fun type(typeFactory: SearchTypeFactory): Int {
        return typeFactory.type(this)
    }
}