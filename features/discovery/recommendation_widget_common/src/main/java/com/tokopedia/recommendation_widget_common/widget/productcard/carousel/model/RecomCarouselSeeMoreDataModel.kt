package com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.CommonRecomCarouselCardTypeFactory
import com.tokopedia.recommendation_widget_common.widget.productcard.common.RecomCarouselDiffUtilComparable
import com.tokopedia.recommendation_widget_common.widget.productcard.common.RecomCommonProductCardListener

/**
 * Created by yfsx on 5/3/21.
 */
data class RecomCarouselSeeMoreDataModel(
        val applink: String = "",
        val backgroundImage: String = "",
        val componentName: String = "",
        val listener: RecomCommonProductCardListener? = null
): Visitable<CommonRecomCarouselCardTypeFactory>, RecomCarouselDiffUtilComparable {
    override fun type(typeFactory: CommonRecomCarouselCardTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(toCompare: RecomCarouselDiffUtilComparable): Boolean {
        return toCompare is RecomCarouselSeeMoreDataModel
    }

    override fun areContentsTheSame(toCompare: RecomCarouselDiffUtilComparable): Boolean {
        if (toCompare !is RecomCarouselSeeMoreDataModel) return false

        return applink == toCompare.applink && backgroundImage == toCompare.backgroundImage &&
                listener === toCompare.listener
    }

    override fun getChangePayload(toCompare: RecomCarouselDiffUtilComparable): Map<String, Any> {
        if (toCompare !is RecomCarouselSeeMoreDataModel) return emptyMap()

        return mutableMapOf<String, Any>().apply {
            if (backgroundImage != toCompare.backgroundImage) {
                put(PAYLOAD_BACKGROUND_IMAGE, Unit)
            }
            if (applink != toCompare.applink || listener !== toCompare.listener) {
                put(PAYLOAD_SHOULD_RECREATE_LISTENERS, Unit)
            }
        }
    }

    companion object {
        const val PAYLOAD_BACKGROUND_IMAGE = "bannerImage"
        const val PAYLOAD_SHOULD_RECREATE_LISTENERS = "recreateListeners"
    }
}