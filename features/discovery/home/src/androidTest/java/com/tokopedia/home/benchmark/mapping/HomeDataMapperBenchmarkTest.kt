package com.tokopedia.home.benchmark.mapping

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.ActivityTestRule
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.home.R
import com.tokopedia.home.benchmark.DynamicChannelGenerator.createLego6
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicLegoBannerViewHolder
import com.tokopedia.home.environment.InstrumentationHomeTestActivity
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import org.junit.Rule
import org.junit.Test

class HomeDataMapperBenchmarkTest {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Test
    fun benchmark_homedatamapper() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val userSessionInterface: UserSessionInterface = UserSession(context)
        val visitableFactory: HomeVisitableFactory = HomeVisitableFactoryImpl(userSessionInterface)
        val trackingQueue = TrackingQueue(context)
        val dataMapper = HomeDataMapper(context, visitableFactory, trackingQueue)
        val json = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.full_json_home)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)

        benchmarkRule.measureRepeated {
            dataMapper.mapToHomeViewModel(homeData, false)
        }
    }
}