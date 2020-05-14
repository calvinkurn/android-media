package com.tokopedia.home.benchmark.render_page

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.ActivityTestRule
import com.tokopedia.home.R
import com.tokopedia.home.benchmark.DynamicChannelGenerator.createLego6
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicLegoBannerViewHolder
import com.tokopedia.home.environment.InstrumentationHomeTestActivity
import org.junit.Rule
import org.junit.Test

class HomeBenchmarkTestRenderPage {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Test
    fun benchmark_DynamicLegoBannerViewHolder_bind() {
        // Context of the app under test.
        val lego6ChannelDataModel = DynamicChannelDataModel()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val root = FrameLayout(context)
        val view = LayoutInflater.from(context).inflate(R.layout.home_dc_lego_banner, root, false)
        val dynamicLegoBannerViewHolder = DynamicLegoBannerViewHolder(
                view, null, null)
        lego6ChannelDataModel.channel = createLego6()

        benchmarkRule.measureRepeated {
            dynamicLegoBannerViewHolder.bind(lego6ChannelDataModel)
        }
    }
}