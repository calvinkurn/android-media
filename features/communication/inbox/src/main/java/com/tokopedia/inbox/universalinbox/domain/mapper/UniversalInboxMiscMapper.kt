package com.tokopedia.inbox.universalinbox.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil
import com.tokopedia.inbox.universalinbox.view.adapter.typefactory.UniversalInboxTypeFactory
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuSeparatorUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationWidgetUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsBannerUiModel
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselTrackingConst
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetMetadata
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel
import javax.inject.Inject

open class UniversalInboxMiscMapper @Inject constructor() {

    fun generateMiscMenu(): List<Visitable<in UniversalInboxTypeFactory>> {
        return listOf(
            UniversalInboxMenuSeparatorUiModel(), // Separator line
            getTopAdsUiModel(), // Top Ads banner
            generateRecommendationWidgetModel() // Recommendation Widget (pre & post purchase)
        )
    }

    protected open fun getTopAdsUiModel(): UniversalInboxTopAdsBannerUiModel {
        return UniversalInboxTopAdsBannerUiModel()
    }

    private fun generateRecommendationWidgetModel(): UniversalInboxRecommendationWidgetUiModel {
        return UniversalInboxRecommendationWidgetUiModel(
            RecommendationWidgetModel(
                metadata = RecommendationWidgetMetadata(
                    pageName = UniversalInboxValueUtil.WIDGET_PAGE_NAME
                ),
                trackingModel = RecommendationWidgetTrackingModel(
                    androidPageName = RecommendationCarouselTrackingConst.Category.INBOX_PAGE,
                    eventActionImpression = RecommendationCarouselTrackingConst.Action.IMPRESSION_ON_PRODUCT_RECOMMENDATION_INBOX,
                    eventActionClick = RecommendationCarouselTrackingConst.Action.CLICK_ON_PRODUCT_RECOMMENDATION_INBOX,
                    listPageName = RecommendationCarouselTrackingConst.List.INBOX
                )
            )
        )
    }
}
