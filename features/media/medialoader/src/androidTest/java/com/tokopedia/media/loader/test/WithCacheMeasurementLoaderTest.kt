package com.tokopedia.media.loader.test

import com.tokopedia.media.loader.BaseMeasurementTest
import com.tokopedia.media.loader.util.CsvUtil
import org.junit.Test

class WithCacheMeasurementLoaderTest : BaseMeasurementTest() {

    private val results = mutableListOf<CsvUtil.CsvLoader>()

    override fun fileName(): String {
        return "media_loader_load_time_with_cache.csv"
    }

    override fun maxIteration(): Int {
        return 100
    }

    @Test
    fun loadTime_measurement_generator() {
        // v1
        loadImageV1Test { i, prop, bitmap ->
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
            results = results
        )

        // save as csv
        saveResult(results)
    }


    companion object {
        private const val IDLE_DELAY = 3000L
    }
}
