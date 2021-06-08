package com.tokopedia.home.screenshot

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import kotlinx.coroutines.cancelChildren

object HomeScreenshotTestHelper {
    fun turnOffAnimation(activity: Activity) {
        val homeRv = activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)

        //turn off slider banner
        (homeRv.findViewHolderForAdapterPosition(3) as? BannerComponentViewHolder)?.let {
            resetBanner(it)
        }
    }

     fun getWidgetScreenshotList(): List<ScreenshotModel> {
        return listOf(
                ScreenshotModel(name = "Header"),
                ScreenshotModel(name = "Ticker"),
                ScreenshotModel(name = "ATF1-Icon"),
                ScreenshotModel(name = "ATF2-Banner Carousel"),
                ScreenshotModel(name = "ATF3-Icon"),
                ScreenshotModel(name = "ATF4-Lego4Image"),
                ScreenshotModel(name = "Lego6Image"),
                ScreenshotModel(name = "Lego4Image"),
                ScreenshotModel(name = "Lego3Image"),
                ScreenshotModel(name = "1x2Banner"),
                ScreenshotModel(name = "4BannerAuto"),
                ScreenshotModel(name = "6ImageAuto"),
                ScreenshotModel(name = "RecommendationListCarousel"),
                ScreenshotModel(name = "ProductHighlight"),
                ScreenshotModel(name = "CategoryWidget"),
                ScreenshotModel(name = "LeftCarousel"),
                ScreenshotModel(name = "TopCarousel"),
                ScreenshotModel(name = "PopularKeyword"),
                ScreenshotModel(name = "HomeWidget"),
                ScreenshotModel(name = "ProductReview"),
                ScreenshotModel(name = "BestSelling"),
                ScreenshotModel(name = "RechargeReminder"),
                ScreenshotModel(name = "SalamReminder"),
                ScreenshotModel(name = "HomeWidget2"),
                ScreenshotModel(name = "HomeFeaturedShop")
        )
    }

    data class ScreenshotModel(val name: String)

    private fun resetBanner(bannerComponentViewholder: BannerComponentViewHolder) {
        //turn off slider banner
        bannerComponentViewholder.coroutineContext.cancelChildren()
        bannerComponentViewholder.scrollTo(0)

        Thread.sleep(5000)
    }
}