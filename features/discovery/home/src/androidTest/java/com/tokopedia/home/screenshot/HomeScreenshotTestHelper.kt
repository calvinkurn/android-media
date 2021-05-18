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
        resetBanner((homeRv.findViewHolderForAdapterPosition(3) as? BannerComponentViewHolder)!!)
    }

    private fun resetBanner(bannerComponentViewholder: BannerComponentViewHolder) {
        //turn off slider banner
        bannerComponentViewholder.coroutineContext.cancelChildren()
        bannerComponentViewholder.scrollTo(0)

        Thread.sleep(5000)
    }
}