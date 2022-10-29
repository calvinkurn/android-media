package com.tkpd.macrobenchmark.test.product_report
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SdkSuppress
import com.tkpd.macrobenchmark.base.BaseFrameTimingBenchmark
import com.tkpd.macrobenchmark.util.MacroIntent
import com.tkpd.macrobenchmark.util.MacroInteration
import org.junit.runner.RunWith

/**
 * Created by DevAra
 * This test will measure MainParentActivity janky frames with macro benchmark
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

@LargeTest
@SdkSuppress(minSdkVersion = 29)
@RunWith(AndroidJUnit4::class)
class ProductReportFrameTimingBenchmark: BaseFrameTimingBenchmark() {

    override fun pageInteractionTest(currentIteration: Int) {
        MacroInteration.interactColumnInCompose(MacroIntent.ProductReport.COLUMN_TAG)
    }

    override fun getIntent() = MacroIntent.ProductReport.getFrameTimingIntent()
}
