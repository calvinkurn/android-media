package com.tokopedia.autocompletecomponent.initialstate.curatedcampaign

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.initialstate.InitialStateTypeFactory

data class CuratedCampaignDataView(
    val baseItemInitialState: BaseItemInitialStateSearch = BaseItemInitialStateSearch(),
): Visitable<InitialStateTypeFactory>{

    override fun type(typeFactory: InitialStateTypeFactory): Int {
        return typeFactory.type(this)
    }
}