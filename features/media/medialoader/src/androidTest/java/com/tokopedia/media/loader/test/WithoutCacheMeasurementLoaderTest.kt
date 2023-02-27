package com.tokopedia.media.loader.test

import com.tokopedia.media.loader.BaseMeasurementTest
import com.tokopedia.media.loader.util.CsvUtil
import org.junit.Test

class WithoutCacheMeasurementLoaderTest : BaseMeasurementTest() {

    private val results = mutableListOf<CsvUtil.CsvLoader>()

    override fun fileName(): String {
        return "media_loader_load_time_without_cache.csv"
    }

    override fun maxIteration(): Int {
        return 100
    }

    @Test
    fun loadTime_measurement_generator() {
        // v1
        loadImageV1Test(
            skipCache = true
        ) { i, prop, bitmap ->
            results.add(
                CsvUtil.CsvLoader(
                    iterationIndex = i,
                    properties = prop,
                    bitmap = bitmap
                )
            )
        }

        // idle
        clearImage()
        Thread.sleep(IDLE_DELAY)

        // v2
        loadImageV2Test(
            skipCache = true,
            results = results
        )

        // save as csv
        saveResult(results)
    }

    companion object {
        private const val IDLE_DELAY = 3000L
    }
}
