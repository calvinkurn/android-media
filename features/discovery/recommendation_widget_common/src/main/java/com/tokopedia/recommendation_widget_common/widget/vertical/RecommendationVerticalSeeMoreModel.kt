package com.tokopedia.recommendation_widget_common.widget.vertical

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class RecommendationVerticalSeeMoreModel(
    val appLink: String = "",
    val recomWidget: RecommendationWidget,
    val componentName: String = ""
) : RecommendationVerticalVisitable {
    override fun type(typeFactory: RecommendationVerticalTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(toCompare: RecommendationVerticalVisitable): Boolean {
        return toCompare is RecommendationVerticalSeeMoreModel
    }

    override fun areContentsTheSame(toCompare: RecommendationVerticalVisitable): Boolean {
        if (toCompare !is RecommendationVerticalSeeMoreModel) return false

        return appLink == toCompare.appLink && recomWidget == toCompare.recomWidget
    }

    override fun getChangePayload(toCompare: RecommendationVerticalVisitable): Map<String, Any> {
        if (toCompare !is RecommendationVerticalSeeMoreModel) return emptyMap()

        return mutableMapOf<String, Any>().apply {
            if (appLink != toCompare.appLink || recomWidget != toCompare.recomWidget) {
                put(PAYLOAD_SHOULD_RECREATE_LISTENERS, Unit)
            }
        }
    }

    companion object {
        const val PAYLOAD_SHOULD_RECREATE_LISTENERS = "recreateListeners"
    }
}
