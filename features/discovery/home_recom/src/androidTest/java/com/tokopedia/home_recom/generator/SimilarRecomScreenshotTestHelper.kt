package com.tokopedia.home_recom.generator

/**
 * Created by dhaba
 */
object SimilarRecomScreenshotTestHelper {
    fun getWidgetScreenshotList(): List<ScreenshotModel> {
        return listOf(
            ScreenshotModel(name = "ProductRecom1"),
            ScreenshotModel(name = "ProductRecom2"),
            ScreenshotModel(name = "ProductRecom3"),
        )
    }
}
