package com.tokopedia.explore.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.explore.view.adapter.factory.ExploreImageTypeFactory
import com.tokopedia.explore.view.type.ExploreCardType
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * @author by milhamj on 24/07/18.
 */

class ExploreImageViewModel(
        val postId: Int,
        val userName: String,
        val imageUrl: String?,
        val itemPos: Int,
        val cardType: ExploreCardType,
        val trackingViewModelList: List<TrackingViewModel>
) : Visitable<ExploreImageTypeFactory> {

    val impressHolder = ImpressHolder()

    override fun type(typeFactory: ExploreImageTypeFactory): Int {
        return typeFactory.type(this)
    }


}
