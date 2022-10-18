package com.tokopedia.home_recom.generator

/**
 * Created by dhaba
 */
object HomeRecomScreenshotTestHelper {
    data class ScreenshotModel(val name: String)

    fun getWidgetScreenshotList(): List<ScreenshotModel> {
        return listOf(
            ScreenshotModel(name = "Header"),
            ScreenshotModel(name = "Ticker"),
            ScreenshotModel(name = "ATF1-Icon"),
            ScreenshotModel(name = "ATF2-Icon"),
            ScreenshotModel(name = "ATF3-Icon"),
            ScreenshotModel(name = "ATF4-Icon"),
            ScreenshotModel(name = "ATF5-Icon"),
        )
    }
}
