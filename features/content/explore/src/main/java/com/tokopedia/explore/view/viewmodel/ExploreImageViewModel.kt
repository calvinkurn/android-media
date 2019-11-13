package com.tokopedia.explore.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.explore.view.adapter.factory.ExploreImageTypeFactory
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
        val cardType: ExploreImageCardType,
        val trackingViewModelList: List<TrackingViewModel>
) : Visitable<ExploreImageTypeFactory> {

    val impressHolder = ImpressHolder()

    override fun type(typeFactory: ExploreImageTypeFactory): Int {
        return typeFactory.type(this)
    }

    enum class ExploreImageCardType(val typeString: String) {

        Multi("multi"),
        Video("video"),
        Youtube("youtube"),
        Unknown("");

        companion object {
            val values = values()

            @JvmStatic
            fun getCardTypeByString(typeString: String): ExploreImageCardType {
                for (type in values) {
                    if (type.typeString == typeString) return type
                }
                return Unknown
            }
        }
    }
}
