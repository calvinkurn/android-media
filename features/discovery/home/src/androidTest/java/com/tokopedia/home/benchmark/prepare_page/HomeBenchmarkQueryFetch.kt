package com.tokopedia.home.benchmark.prepare_page

import android.content.Context
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.home.R
import org.junit.Rule
import org.junit.Test

class HomeBenchmarkQueryFetch  {
    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Test
    fun benchmark_fetchQuery_fromRaw() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        benchmarkRule.measureRepeated {
            val query = GraphqlHelper.loadRawString(context.resources, R.raw.dynamic_channel_query)
            val setThisQuery = query
        }
    }

    @Test
    fun benchmark_fetchQuery_fromLocalVal() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        benchmarkRule.measureRepeated {
            val query = TestQuery.dynamicChannelQuery
            val setThisQuery = query
        }
    }
}