package com.tokopedia.autocomplete.initialstate.recentsearch

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.InitialStateTypeFactory

class RecentSearchSeeMoreViewModel: Visitable<InitialStateTypeFactory> {

    override fun type(typeFactory: InitialStateTypeFactory): Int {
        return typeFactory.type(this)
    }
}
