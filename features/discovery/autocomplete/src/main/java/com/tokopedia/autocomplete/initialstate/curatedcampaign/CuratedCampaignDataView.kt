package com.tokopedia.autocomplete.initialstate.curatedcampaign

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.initialstate.InitialStateTypeFactory

data class CuratedCampaignDataView(
        val template: String = "",
        val imageUrl: String = "",
        val applink: String = "",
        val url: String = "",
        val title: String = "",
        val subtitle: String = "",
        val iconTitle: String = "",
        val iconSubtitle: String = "",
        val label: String = "",
        val labelType: String = "",
        val shortcutImage: String = "",
        val productId: String = "",
        val type: String = "",
        val featureId: String = "",
        val header: String = ""
): Visitable<InitialStateTypeFactory>{

    override fun type(typeFactory: InitialStateTypeFactory): Int {
        return typeFactory.type(this)
    }
}