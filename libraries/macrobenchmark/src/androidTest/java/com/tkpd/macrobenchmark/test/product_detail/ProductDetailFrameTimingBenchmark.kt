package com.tkpd.macrobenchmark.test.product_detail
import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import androidx.test.uiautomator.Direction
import com.tkpd.macrobenchmark.base.BaseFrameTimingBenchmark
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.runner.RunWith

/**
 * Created by yovi.putra on 01/11/22"
 * Project name: android-tokopedia-core
 **/

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(AndroidJUnit4::class)
class ProductDetailFrameTimingBenchmark : BaseFrameTimingBenchmark() {

    override fun setupMock() {
    }

    override fun setupEnvironment() {
    }

    override fun pageInteractionTest(currentIteration: Int) {
        MacroInteration.recyclerViewViewHolderInteraction(
            MacroIntent.TKPD_PACKAGE_NAME,
            MacroIntent.ProductDetail.RECYCLER_VIEW_ID,
            MacroIntent.ProductDetail.MEDIA_VIEW_HOLDER
        )
        MacroInteration.recyclerViewScrollingInteraction(
            MacroIntent.TKPD_PACKAGE_NAME,
            MacroIntent.ProductDetail.RECYCLER_VIEW_ID,
            Direction.DOWN
        )
        MacroInteration.recyclerViewViewHolderInteraction(
            MacroIntent.TKPD_PACKAGE_NAME,
            MacroIntent.ProductDetail.RECYCLER_VIEW_ID,
            MacroIntent.ProductDetail.RECOMMENDATION_VIEW_HOLDER
        )
    }

    override fun getIntent(): Intent = MacroIntent.ProductDetail.getFrameTimingIntent()
}
