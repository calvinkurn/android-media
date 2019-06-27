package com.tokopedia.affiliate.feature.explore.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.common.viewmodel.ExploreTitleViewModel
import com.tokopedia.affiliate.feature.explore.view.adapter.typefactory.ExploreTypeFactory

/**
 * @author by milhamj on 18/03/19.
 */
class ProductTitleViewModel(
        val title: ExploreTitleViewModel = ExploreTitleViewModel()
): Visitable<ExploreTypeFactory> {
    override fun type(typeFactory: ExploreTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}