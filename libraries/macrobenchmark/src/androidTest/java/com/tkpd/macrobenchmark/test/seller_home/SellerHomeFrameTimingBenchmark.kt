package com.tkpd.macrobenchmark.test.seller_home

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseSellerAppFrameTimingBenchmark
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.runner.RunWith

/**
 * Created by @ilhamsuaib
 */

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(AndroidJUnit4::class)
class SellerHomeFrameTimingBenchmark : BaseSellerAppFrameTimingBenchmark() {

    override fun pageInteractionTest(currentIteration: Int) {
        Thread.sleep(10000)
        MacroInteration.basicFlingInteraction(
            packageName = packageName(),
            MacroIntent.SellerHome.RV_RESOURCE_ID,
            flingSpeed = 3000
        )
    }

    override fun getIntent(): Intent = MacroIntent.SellerHome.getIntent()
}
