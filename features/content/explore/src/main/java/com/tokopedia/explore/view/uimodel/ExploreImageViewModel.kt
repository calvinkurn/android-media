package com.tokopedia.explore.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.explore.view.adapter.factory.ExploreImageTypeFactory
import com.tokopedia.explore.view.type.ExploreCardType
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * @author by milhamj on 24/07/18.
 */

data class ExploreImageViewModel(
        val postId: Int = 0,
        val userName: String = "",
        val imageUrl: String = "",
        val itemPos: Int = 0,
        val cardType: ExploreCardType,
        val trackingViewModelList: List<TrackingViewModel> = mutableListOf()
) : Visitable<ExploreImageTypeFactory> {

    val impressHolder = ImpressHolder()

    override fun type(typeFactory: ExploreImageTypeFactory): Int {
        return typeFactory.type(this)
    }


}
