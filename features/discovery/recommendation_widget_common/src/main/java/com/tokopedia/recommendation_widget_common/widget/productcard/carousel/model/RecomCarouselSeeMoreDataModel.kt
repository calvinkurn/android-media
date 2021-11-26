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
        return toCompare !is RecomCarouselSeeMoreDataModel
    }

    override fun areContentsTheSame(toCompare: RecomCarouselDiffUtilComparable): Boolean {
        if (toCompare !is RecomCarouselSeeMoreDataModel) return false

        return applink == toCompare.applink && componentName == toCompare.componentName &&
                backgroundImage == toCompare.backgroundImage && listener === toCompare.listener
    }

    override fun getChangePayload(toCompare: RecomCarouselDiffUtilComparable): Map<String, Any> {
        if (toCompare !is RecomCarouselSeeMoreDataModel) return emptyMap()

        return mutableMapOf<String, Any>().apply {
            if (applink != toCompare.applink) {
                put(PAYLOAD_APPLINK, Unit)
            }
            if (backgroundImage != toCompare.backgroundImage) {
                put(PAYLOAD_BACKGROUND_IMAGE, Unit)
            }
            if (componentName != toCompare.componentName) {
                put(PAYLOAD_COMPONENT_NAME, Unit)
            }
            if (listener !== toCompare.listener) {
                put(PAYLOAD_IS_LISTENER_CHANGED, Unit)
            }
        }
    }

    companion object {
        const val PAYLOAD_BACKGROUND_IMAGE = "bannerImage"
        const val PAYLOAD_APPLINK = "appLink"
        const val PAYLOAD_COMPONENT_NAME = "componentName"
        const val PAYLOAD_IS_LISTENER_CHANGED = "isListenerChanged"
    }
}