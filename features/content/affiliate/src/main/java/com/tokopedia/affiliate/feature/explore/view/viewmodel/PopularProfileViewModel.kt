package com.tokopedia.affiliate.feature.explore.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.feature.explore.view.adapter.typefactory.ExploreTypeFactory

/**
 * @author by milhamj on 12/03/19.
 */
data class PopularProfileViewModel(
        val popularProfiles: MutableList<PopularProfileChildViewModel> = arrayListOf()
) : Visitable<ExploreTypeFactory> {
    override fun type(typeFactory: ExploreTypeFactory): Int {
        return typeFactory.type(this)
    }
}