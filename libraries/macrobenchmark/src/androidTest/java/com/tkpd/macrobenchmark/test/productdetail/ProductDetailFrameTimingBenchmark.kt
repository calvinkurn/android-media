package com.tkpd.macrobenchmark.test.productdetail

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import com.tkpd.macrobenchmark.base.BaseFrameTimingBenchmark
import com.tkpd.macrobenchmark.util.MacroDevOps
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
        MacroDevOps.setupEnvironment(MacroIntent.Mock.getProductDetailMockIntent())
    }

    override fun setupEnvironment() {
    }

    override fun pageInteractionTest(currentIteration: Int) {
        val density = InstrumentationRegistry.getInstrumentation()
            .context.resources.displayMetrics.density
        val defaultFeelingSpeed = 7500
        val speed = (defaultFeelingSpeed * density).toInt()

        MacroInteration.basicFlingInteraction(
            MacroIntent.ProductDetail.PACKAGE_NAME,
            MacroIntent.ProductDetail.RECYCLER_VIEW_ID,
            flingSpeed = speed
        )
    }

    override fun getIntent(): Intent = MacroIntent.ProductDetail.getIntent()
}
