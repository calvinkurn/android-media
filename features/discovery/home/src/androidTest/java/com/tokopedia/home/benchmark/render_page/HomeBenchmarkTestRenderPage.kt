package com.tokopedia.home.benchmark.render_page

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.home.R
import com.tokopedia.home.benchmark.DynamicChannelGenerator.createLego6Component
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import org.junit.Rule
import org.junit.Test

class HomeBenchmarkTestRenderPage {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Test
    fun benchmark_DynamicLegoBannerViewHolder_bind() {
        // Context of the app under test.
        val lego6ChannelDataModel = DynamicLegoBannerDataModel(createLego6Component())
        val context = ApplicationProvider.getApplicationContext<Context>()
        val root = FrameLayout(context)
        val view = LayoutInflater.from(context).inflate(R.layout.home_component_lego_banner, root, false)
        val dynamicLegoBannerViewHolder = DynamicLegoBannerViewHolder(
                view, null, null)
        benchmarkRule.measureRepeated {
            dynamicLegoBannerViewHolder.bind(lego6ChannelDataModel)
        }
    }
}