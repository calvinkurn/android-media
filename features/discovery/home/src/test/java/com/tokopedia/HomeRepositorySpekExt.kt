package com.tokopedia

import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.datasource.local.HomeCachedDataSource
import com.tokopedia.home.beranda.data.datasource.remote.HomeRemoteDataSource
import com.tokopedia.home.beranda.data.datasource.remote.PlayRemoteDataSource
import com.tokopedia.home.beranda.data.repository.HomeRepository
import com.tokopedia.home.beranda.data.repository.HomeRepositoryImpl
import com.tokopedia.home.beranda.data.source.HomeDataSource
import io.mockk.mockk
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.gherkin.FeatureBody

fun TestBody.createHomeRepositoryImpl(): HomeRepository {
    val getHomeDataSource by memoized<HomeDataSource>()
    val getCachedDataSource by memoized<HomeCachedDataSource>()
    val getRemoteDataSource by memoized<HomeRemoteDataSource>()
    val getPlayRemoteDataSource by memoized<PlayRemoteDataSource>()
    val getHomeDefaultDataSource by memoized<HomeDefaultDataSource>()

    return HomeRepositoryImpl(
            homeDataSource = getHomeDataSource,
            homeCachedDataSource = getCachedDataSource,
            homeRemoteDataSource = getRemoteDataSource,
            playRemoteDataSource = getPlayRemoteDataSource,
            homeDefaultDataSource = getHomeDefaultDataSource
    )
}

@Suppress("UNUSED_VARIABLE")
fun FeatureBody.createHomeRepositoryTestInstance() {
    val getHomeDataSource by memoized {
        mockk<HomeDataSource>(relaxed = true)
    }
    val getCachedDataSource by memoized {
        mockk<HomeCachedDataSource>(relaxed = true)
    }
    val getRemoteDataSource by memoized {
        mockk<HomeRemoteDataSource>(relaxed = true)
    }
    val getPlayRemoteDataSource by memoized {
        mockk<PlayRemoteDataSource>(relaxed = true)
    }
    val getHomeDefaultDataSource by memoized {
        mockk<HomeDefaultDataSource>(relaxed = true)
    }
}