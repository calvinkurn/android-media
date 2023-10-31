package com.tokopedia.media.loader.test

import com.tokopedia.media.loader.BaseMeasurementTest
import com.tokopedia.media.loader.util.CsvUtil
import org.junit.Test

class WithCacheMeasurementLoaderTest : BaseMeasurementTest() {

    private val results = mutableListOf<CsvUtil.CsvLoader>()
    private val maxIteration = 100

    override fun fileName(): String {
        return "media_loader_load_time_with_cache.csv"
    }

    @Test
    fun load_time_measurement_with_cache() {
        // v1
        for (index in 1..maxIteration) {
            loadImageV1Test { prop, bitmap ->
                results.add(
                    CsvUtil.CsvLoader(
                        iterationIndex = index,
                        properties = prop,
                        bitmap = bitmap
                    )
                )
            }
        }

        // idle
        idle()

        // v2
        results.forEach {
            loadImageTest { prop, _ ->
                it.improvedLoadTime = prop.loadTime
            }
        }

        // save as csv
        saveComparisonResult(results)
    }
}
