package com.tokopedia.affiliate.feature.explore.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.common.viewmodel.ExploreCardViewModel
import com.tokopedia.affiliate.common.viewmodel.ExploreTitleViewModel
import com.tokopedia.affiliate.feature.explore.view.adapter.typefactory.ExploreTypeFactory

/**
 * @author by milhamj on 14/03/19.
 */
data class RecommendationViewModel(
        val type: String = "",
        val cards: List<ExploreCardViewModel> = listOf(),
        val titleViewModel: ExploreTitleViewModel = ExploreTitleViewModel()
) : Visitable<ExploreTypeFactory> {
    override fun type(typeFactory: ExploreTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}