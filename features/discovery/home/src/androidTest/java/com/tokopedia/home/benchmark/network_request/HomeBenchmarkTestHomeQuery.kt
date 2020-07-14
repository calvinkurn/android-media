package com.tokopedia.home.benchmark.network_request

import android.util.Log
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home.benchmark.network_request.request.*
import kotlinx.coroutines.*
import org.junit.Rule
import org.junit.Test
import kotlin.coroutines.CoroutineContext

class HomeBenchmarkTestHomeQuery: CoroutineScope {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + masterJob

    @Test
    fun benchmark_request_HomeQueryNormal() {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val homeQueryNormalRequest = GraphqlRequest(
                HomeQueryNormal.getQuery(),
                String::class.java
        )

        val graphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

        benchmarkRule.measureRepeated {
            runBlocking {
                val response = graphqlRepository.getReseponse(listOf(homeQueryNormalRequest), cacheStrategy)
                Log.d("BenchmarkResponse", response.toString())
            }
        }
    }

    @Test
    fun benchmark_request_HomeQueryReduceQuery() {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val homeQueryReduceRequest = GraphqlRequest(
                HomeQueryReduceQuery.getQuery(),
                String::class.java
        )

        val graphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

        benchmarkRule.measureRepeated {
            runBlocking {
                val response = graphqlRepository.getReseponse(listOf(homeQueryReduceRequest), cacheStrategy)
                Log.d("BenchmarkResponse", response.toString())
            }
        }
    }

    //benchmarking each gql query

    @Test
    fun benchmark_request_TickerQuery() {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val homeTickerRequest = GraphqlRequest(
                HomeQueryTicker.getQuery(),
                String::class.java
        )

        val graphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

        benchmarkRule.measureRepeated {
            runBlocking {
                val response = graphqlRepository.getReseponse(listOf(homeTickerRequest), cacheStrategy)
                Log.d("BenchmarkResponse", response.toString())
            }
        }
    }

    @Test
    fun benchmark_request_SliderBannerQuery() {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val homeSliderBannerRequest = GraphqlRequest(
                HomeQuerySliderBanner.getQuery(),
                String::class.java
        )

        val graphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

        benchmarkRule.measureRepeated {
            runBlocking {
                val response = graphqlRepository.getReseponse(listOf(homeSliderBannerRequest), cacheStrategy)
                Log.d("BenchmarkResponse", response.toString())
            }
        }
    }

    @Test
    fun benchmark_request_DynamicIconQuery() {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val homeDynamicIconRequest = GraphqlRequest(
                HomeQueryDynamicHomeIcon.getQuery(),
                String::class.java
        )

        val graphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

        benchmarkRule.measureRepeated {
            runBlocking {
                val response = graphqlRepository.getReseponse(listOf(homeDynamicIconRequest), cacheStrategy)
                Log.d("BenchmarkResponse", response.toString())
            }
        }
    }

    @Test
    fun benchmark_request_HomeFlagQuery() {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val homeFlagRequest = GraphqlRequest(
                HomeQueryHomeFlag.getQuery(),
                String::class.java
        )

        val graphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

        benchmarkRule.measureRepeated {
            runBlocking {
                val response = graphqlRepository.getReseponse(listOf(homeFlagRequest), cacheStrategy)
                Log.d("BenchmarkResponse", response.toString())
            }
        }
    }

    @Test
    fun benchmark_request_DynamicHomeChannelQuery() {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val dynamicHomeChannelRequest = GraphqlRequest(
                HomeQueryDynamicHomeChannel.getQuery(),
                String::class.java
        )

        val graphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

        benchmarkRule.measureRepeated {
            runBlocking {
                val response = graphqlRepository.getReseponse(listOf(dynamicHomeChannelRequest), cacheStrategy)
                Log.d("BenchmarkResponse", response.toString())
            }
        }
    }

    @Test
    fun benchmark_request_SpotlightQuery() {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val spotlightRequest = GraphqlRequest(
                HomeQuerySpotlight.getQuery(),
                String::class.java
        )

        val graphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

        benchmarkRule.measureRepeated {
            runBlocking {
                val response = graphqlRepository.getReseponse(listOf(spotlightRequest), cacheStrategy)
                Log.d("BenchmarkResponse", response.toString())
            }
        }
    }
}