package com.tokopedia.home.benchmark.mapping

import android.content.Context
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
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
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        val visitableFactory: HomeVisitableFactory = HomeVisitableFactoryImpl(userSessionInterface, remoteConfig, HomeDefaultDataSource())
        val trackingQueue = TrackingQueue(context)
        val dataMapper = HomeDataMapper(context, visitableFactory, trackingQueue)
        val json = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.full_json_home)
        val homeData = Gson().fromJson(json, HomeData::class.java)
        benchmarkRule.measureRepeated {
            dataMapper.mapToHomeViewModel(homeData, true)
        }
    }

}