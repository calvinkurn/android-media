package com.tokopedia.media.loader.test

import com.tokopedia.media.loader.BaseMeasurementTest
import com.tokopedia.media.loader.util.AssetReader
import com.tokopedia.media.loader.util.CsvUtil
import org.junit.Test

class WithoutCacheMultipleUrlTest : BaseMeasurementTest() {

    private val results = mutableListOf<CsvUtil.CsvLoader>()

    override fun fileName(): String {
        return "media_loader_load_time_multi_url.csv"
    }

    @Test
    fun load_time_measurement_without_cache_multi_url() {
        val urls = AssetReader.get(
            applicationContext,
            "image-url.txt"
        )

        // v1
        urls.forEachIndexed { index, url ->
            loadImageV1Test(
                imageUrl = url,
                skipCache = true
            ) { prop, bitmap ->
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
        urls.forEachIndexed { index, url ->
            loadImageV2Test(
                imageUrl = url,
                skipCache = true
            ) { prop, _ ->
                results[index].improvedLoadTime = prop.loadTime
            }
        }

        // save as csv
        saveResult(results)
    }
}
