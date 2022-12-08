package com.tokopedia.home_recom.generator

/**
 * Created by dhaba
 */
object HomeRecomScreenshotTestHelper {
    fun getWidgetScreenshotList(): List<ScreenshotModel> {
        return listOf(
            ScreenshotModel(name = "ProductInfo"),
            ScreenshotModel(name = "TitleCarouselVertical"),
            ScreenshotModel(name = "ProductRecommendationVertical"),
            ScreenshotModel(name = "RecommendationCarousel"),
        )
    }
}
