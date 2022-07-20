package com.tokopedia.recommendation_widget_common.widget.productcard.carousel.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.widget.productcard.carousel.CommonRecomCarouselCardTypeFactory
import com.tokopedia.recommendation_widget_common.widget.productcard.common.RecomCarouselDiffUtilComparable
import com.tokopedia.recommendation_widget_common.widget.productcard.common.RecomCommonProductCardListener

/**
 * Created by yfsx on 5/3/21.
 */
data class RecomCarouselBannerDataModel(
        val bannerImage: String = "",
        val bannerBackgorundColor: String = "",
        val impressHolder: ImpressHolder = ImpressHolder(),
        val applink: String = "",
        val componentName: String = "",
        val listener: RecomCommonProductCardListener? = null
): Visitable<CommonRecomCarouselCardTypeFactory>, RecomCarouselDiffUtilComparable {
    override fun type(typeFactory: CommonRecomCarouselCardTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(toCompare: RecomCarouselDiffUtilComparable): Boolean {
        return toCompare is RecomCarouselBannerDataModel
    }

    override fun areContentsTheSame(toCompare: RecomCarouselDiffUtilComparable): Boolean {
        if (toCompare !is RecomCarouselBannerDataModel) return false

        return bannerImage == toCompare.bannerImage && bannerBackgorundColor == toCompare.bannerBackgorundColor &&
                impressHolder === toCompare.impressHolder && applink == toCompare.applink &&
                listener === toCompare.listener
    }

    override fun getChangePayload(toCompare: RecomCarouselDiffUtilComparable): Map<String, Any> {
        if (toCompare !is RecomCarouselBannerDataModel) return emptyMap()

        return mutableMapOf<String, Any>().apply {
            if (bannerImage != toCompare.bannerImage) {
                put(PAYLOAD_BANNER_IMAGE, Unit)
            }
            if (bannerBackgorundColor != toCompare.bannerBackgorundColor) {
                put(PAYLOAD_BACKGROUND_COLOR, Unit)
            }
            if (
                applink != toCompare.applink ||
                impressHolder !== toCompare.impressHolder ||
                listener !== toCompare.listener
            ) {
                put(PAYLOAD_SHOULD_RECREATE_LISTENERS, Unit)
            }
        }
    }

    companion object {
        const val PAYLOAD_BANNER_IMAGE = "bannerImage"
        const val PAYLOAD_BACKGROUND_COLOR = "backgroundColor"
        const val PAYLOAD_SHOULD_RECREATE_LISTENERS = "recreateListeners"
    }
}